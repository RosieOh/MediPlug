package com.emrsystem.domain.emr.service;

import com.emrsystem.domain.appointment.entity.Appointment;
import com.emrsystem.domain.appointment.store.AppointmentStore;
import com.emrsystem.domain.doctor.entity.Doctor;
import com.emrsystem.domain.doctor.store.DoctorStore;
import com.emrsystem.domain.emr.entity.ChartTemplate;
import com.emrsystem.domain.emr.entity.DiagnosisCode;
import com.emrsystem.domain.emr.entity.LabOrder;
import com.emrsystem.domain.emr.entity.LabResult;
import com.emrsystem.domain.emr.entity.MedicalCertificate;
import com.emrsystem.domain.emr.entity.MedicalImage;
import com.emrsystem.domain.emr.entity.MedicalRecord;
import com.emrsystem.domain.emr.entity.Prescription;
import com.emrsystem.domain.emr.request.EmrRequests;
import com.emrsystem.domain.emr.store.EmrStore;
import com.emrsystem.domain.notification.facade.NotificationFacade;
import com.emrsystem.domain.patient.entity.Patient;
import com.emrsystem.domain.patient.service.PatientAllergyService;
import com.emrsystem.domain.patient.store.PatientStore;
import com.emrsystem.domain.pharmacy.entity.DrugInteraction;
import com.emrsystem.domain.pharmacy.entity.DrugMaster;
import com.emrsystem.domain.pharmacy.service.PharmacyService;
import com.emrsystem.domain.pharmacy.store.PharmacyStore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmrService {

    private final EmrStore emrStore;
    private final PatientStore patientStore;
    private final DoctorStore doctorStore;
    private final AppointmentStore appointmentStore;
    private final PharmacyStore pharmacyStore;
    private final PharmacyService pharmacyService;
    private final PatientAllergyService allergyService;
    private final NotificationFacade notificationFacade;
    private final ObjectMapper objectMapper;

    // Medical Record operations
    // 목록 전체 조회는 스펙 축소: 환자/의사 기준 목록만 지원

    public Page<MedicalRecord> getMedicalRecordsByPatient(Long patientId, Pageable pageable) {
        return emrStore.findMedicalRecordsByPatient(patientId, pageable);
    }

    public Page<MedicalRecord> getMedicalRecordsByDoctor(Long doctorId, Pageable pageable) {
        return emrStore.findMedicalRecordsByDoctor(doctorId, pageable);
    }

    public MedicalRecord getMedicalRecord(Long id) {
        return emrStore.findMedicalRecordById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.MEDICAL_RECORD_NOT_FOUND));
    }

    @Transactional
    public MedicalRecord createMedicalRecord(EmrRequests.CreateMedicalRecordRequest request) {
        Patient patient = patientStore.findById(request.getPatientId())
                .orElseThrow(() -> new CommonException(ErrorCode.PATIENT_NOT_FOUND));
        Doctor doctor = doctorStore.findById(request.getDoctorId())
                .orElseThrow(() -> new CommonException(ErrorCode.DOCTOR_NOT_FOUND));

        MedicalRecord medicalRecord = MedicalRecord.create(null, patient, doctor, request.getContent());
        return emrStore.saveMedicalRecord(medicalRecord);
    }

    @Transactional
    public MedicalRecord updateMedicalRecord(Long id, EmrRequests.UpdateMedicalRecordRequest request) {
        MedicalRecord medicalRecord = getMedicalRecord(id);
        medicalRecord.updateContent(null, null, null, null, null, request.getContent());
        return emrStore.saveMedicalRecord(medicalRecord);
    }

    @Transactional
    // 상태 관리 기능은 스펙 축소로 제외

    // Chart Template operations
    // 단순 목록은 findAll 사용 권장(컨트롤러에서 처리) / 코드 조회만 스토어 노출

    public ChartTemplate getChartTemplate(Long id) {
        return emrStore.findChartTemplateById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.CHART_TEMPLATE_NOT_FOUND));
    }

    @Transactional
    public ChartTemplate createChartTemplate(EmrRequests.CreateChartTemplateRequest request) {
        if (emrStore.existsChartTemplateByCode(request.getCode())) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION);
        }

        ChartTemplate template = ChartTemplate.create(request.getName(), "GENERAL", request.getCode(), request.getContent(), null);
        return emrStore.saveChartTemplate(template);
    }

    @Transactional
    public ChartTemplate updateChartTemplate(Long id, EmrRequests.UpdateChartTemplateRequest request) {
        ChartTemplate template = getChartTemplate(id);
        template.update(request.getName(), "GENERAL", request.getContent(), null);
        return emrStore.saveChartTemplate(template);
    }

    @Transactional
    public void deleteChartTemplate(Long id) {
        if (!emrStore.findChartTemplateById(id).isPresent()) {
            throw new CommonException(ErrorCode.CHART_TEMPLATE_NOT_FOUND);
        }
        emrStore.deleteChartTemplateById(id);
    }

    public List<ChartTemplate> getChartTemplates() {
        return emrStore.findAllChartTemplates();
    }

    // Prescription operations
    // 처방전 조회는 진료기록 기준으로 단순화

    public Prescription getPrescription(Long id) {
        return emrStore.findPrescriptionById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.PRESCRIPTION_NOT_FOUND));
    }

    @Transactional
    public Prescription createPrescription(EmrRequests.CreatePrescriptionRequest request) {
        MedicalRecord record = getMedicalRecord(request.getMedicalRecordId());
        Patient patient = record.getPatient();
        Doctor doctor = record.getDoctor();
        
        // Appointment 찾기 (Prescription은 Appointment와 연결됨)
        var appointment = appointmentStore.findById(record.getAppointment().getAppointmentId())
                .orElseThrow(() -> new CommonException(ErrorCode.APPOINTMENT_NOT_FOUND));

        // DrugMaster 찾기
        DrugMaster drugMaster = pharmacyStore.findDrugMasterByCode(request.getDrugCode())
                .orElseThrow(() -> new CommonException(ErrorCode.DRUG_MASTER_NOT_FOUND, 
                        "약물을 찾을 수 없습니다: " + request.getDrugCode()));

        // 1. 알레르기 체크
        if (allergyService.hasSevereDrugAllergy(patient.getPatientId(), drugMaster.getDrugName())) {
            // 심각한 알레르기: 예외 발생 및 알림 생성
            String message = String.format("환자에게 %s에 대한 심각한 알레르기가 있습니다. 처방할 수 없습니다.", drugMaster.getDrugName());
            sendDrugAllergyAlert(doctor, patient, drugMaster.getDrugName(), "SEVERE", message);
            throw new CommonException(ErrorCode.DRUG_ALLERGY_SEVERE, message);
        } else if (allergyService.hasDrugAllergy(patient.getPatientId(), drugMaster.getDrugName())) {
            // 경고 수준 알레르기: 알림 생성하고 처방은 진행
            String message = String.format("환자에게 %s에 대한 알레르기가 있습니다. 처방 시 주의가 필요합니다.", drugMaster.getDrugName());
            sendDrugAllergyAlert(doctor, patient, drugMaster.getDrugName(), "WARNING", message);
        }

        // 2. 현재 처방 중인 약물들과의 상호작용 체크
        // 같은 진료기록의 다른 처방전들을 조회
        var existingPrescriptions = emrStore.findPrescriptionsByMedicalRecord(
                request.getMedicalRecordId(), 
                org.springframework.data.domain.Pageable.unpaged()
        ).getContent();
        
        if (!existingPrescriptions.isEmpty()) {
            // 기존 처방 약물들의 drugCode를 수집
            List<Long> drugIds = new ArrayList<>();
            drugIds.add(drugMaster.getDrugMasterId());
            
            for (Prescription existingPrescription : existingPrescriptions) {
                DrugMaster existingDrug = pharmacyStore.findDrugMasterByCode(existingPrescription.getDrugCode())
                        .orElse(null);
                if (existingDrug != null) {
                    drugIds.add(existingDrug.getDrugMasterId());
                }
            }
            
            // 약물 상호작용 체크 및 알림 생성
            checkAndNotifyDrugInteractions(doctor, patient, drugIds, drugMaster);
        }

        // 3. 처방전 생성
        Prescription prescription = Prescription.create(
                appointment,
                patient,
                doctor,
                request.getDrugName(),
                request.getDrugCode(),
                request.getDosage(),
                request.getFrequency(),
                request.getDuration(),
                request.getUnit() != null ? request.getUnit() : "정",
                request.getNotes(),
                drugMaster.getSideEffects()
        );

        return emrStore.savePrescription(prescription);
    }

    @Transactional
    // 상태 전환(조제/취소)은 스펙 축소로 제외

    // 처방전 조회(진료기록 기준)
    public org.springframework.data.domain.Page<Prescription> getPrescriptionsByMedicalRecord(Long medicalRecordId, org.springframework.data.domain.Pageable pageable) {
        return emrStore.findPrescriptionsByMedicalRecord(medicalRecordId, pageable);
    }

    // Lab Order operations (검사오더 조회는 진료기록/상태 기준)

    public LabOrder getLabOrder(Long id) {
        return emrStore.findLabOrderById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.LAB_ORDER_NOT_FOUND));
    }

    @Transactional
    public LabOrder createLabOrder(EmrRequests.CreateLabOrderRequest request) {
        MedicalRecord record = getMedicalRecord(request.getMedicalRecordId());
        // Simplified: not supported in current model
        throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION);
    }

    @Transactional
    public org.springframework.data.domain.Page<LabOrder> getLabOrdersByMedicalRecord(Long medicalRecordId, org.springframework.data.domain.Pageable pageable) {
        return emrStore.findLabOrdersByMedicalRecord(medicalRecordId, pageable);
    }

    // 수집/완료/취소 등 상태 전환은 스펙 축소로 제외

    // Diagnosis Code operations
    public DiagnosisCode getDiagnosisCode(Long id) {
        return emrStore.findDiagnosisCodeById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "진단 코드를 찾을 수 없습니다."));
    }

    public DiagnosisCode getDiagnosisCodeByCode(String code) {
        return emrStore.findDiagnosisCodeByCode(code)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "진단 코드를 찾을 수 없습니다: " + code));
    }

    public Page<DiagnosisCode> getDiagnosisCodes(Pageable pageable) {
        return emrStore.findDiagnosisCodes(pageable);
    }

    public Page<DiagnosisCode> searchDiagnosisCodes(String code, String name, Pageable pageable) {
        return emrStore.searchDiagnosisCodes(code, name, pageable);
    }

    public List<DiagnosisCode> getDiagnosisCodesByCategory(String category) {
        return emrStore.findDiagnosisCodesByCategory(category);
    }

    @Transactional
    public DiagnosisCode createDiagnosisCode(EmrRequests.CreateDiagnosisCodeRequest request) {
        if (emrStore.existsDiagnosisCodeByCode(request.getCode())) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION, "이미 존재하는 진단 코드입니다: " + request.getCode());
        }

        DiagnosisCode diagnosisCode = DiagnosisCode.create(
                request.getCode(),
                request.getName(),
                request.getCategory(),
                request.getDescription(),
                request.getVersion() != null ? request.getVersion() : 10
        );

        return emrStore.saveDiagnosisCode(diagnosisCode);
    }

    @Transactional
    public DiagnosisCode updateDiagnosisCode(Long id, EmrRequests.UpdateDiagnosisCodeRequest request) {
        DiagnosisCode diagnosisCode = getDiagnosisCode(id);
        diagnosisCode.update(request.getName(), request.getCategory(), request.getDescription());
        return emrStore.saveDiagnosisCode(diagnosisCode);
    }

    @Transactional
    public void deactivateDiagnosisCode(Long id) {
        DiagnosisCode diagnosisCode = getDiagnosisCode(id);
        diagnosisCode.deactivate();
        emrStore.saveDiagnosisCode(diagnosisCode);
    }

    @Transactional
    public void activateDiagnosisCode(Long id) {
        DiagnosisCode diagnosisCode = getDiagnosisCode(id);
        diagnosisCode.activate();
        emrStore.saveDiagnosisCode(diagnosisCode);
    }

    // Lab Result operations
    public LabResult getLabResult(Long id) {
        return emrStore.findLabResultById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "검사 결과를 찾을 수 없습니다."));
    }

    public List<LabResult> getLabResultsByLabOrder(Long labOrderId) {
        return emrStore.findLabResultsByLabOrder(labOrderId);
    }

    public List<LabResult> getAbnormalLabResults() {
        return emrStore.findAbnormalLabResults(List.of("H", "L", "A"));
    }

    @Transactional
    public LabResult createLabResult(EmrRequests.CreateLabResultRequest request) {
        LabOrder labOrder = getLabOrder(request.getLabOrderId());

        LabResult labResult = LabResult.create(
                labOrder,
                request.getTestItemName(),
                request.getTestItemCode(),
                request.getResultValue(),
                request.getUnit(),
                request.getReferenceRange(),
                request.getAbnormalFlag() != null ? request.getAbnormalFlag() : "N",
                request.getNotes()
        );

        return emrStore.saveLabResult(labResult);
    }

    @Transactional
    public LabResult updateLabResult(Long id, EmrRequests.UpdateLabResultRequest request) {
        LabResult labResult = getLabResult(id);
        labResult.update(
                request.getResultValue(),
                request.getUnit(),
                request.getReferenceRange(),
                request.getAbnormalFlag(),
                request.getNotes()
        );
        return emrStore.saveLabResult(labResult);
    }

    @Transactional
    public void correctLabResult(Long id) {
        LabResult labResult = getLabResult(id);
        labResult.correct();
        emrStore.saveLabResult(labResult);
    }

    @Transactional
    public void cancelLabResult(Long id) {
        LabResult labResult = getLabResult(id);
        labResult.cancel();
        emrStore.saveLabResult(labResult);
    }

    // Medical Image operations
    public MedicalImage getMedicalImage(Long id) {
        return emrStore.findMedicalImageById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "의료 영상을 찾을 수 없습니다."));
    }

    public List<MedicalImage> getMedicalImagesByPatient(Long patientId) {
        return emrStore.findMedicalImagesByPatient(patientId);
    }

    public Page<MedicalImage> getMedicalImagesByPatient(Long patientId, Pageable pageable) {
        return emrStore.findMedicalImagesByPatient(patientId, pageable);
    }

    public List<MedicalImage> getMedicalImagesByMedicalRecord(Long medicalRecordId) {
        return emrStore.findMedicalImagesByMedicalRecord(medicalRecordId);
    }

    public List<MedicalImage> getMedicalImagesByLabOrder(Long labOrderId) {
        return emrStore.findMedicalImagesByLabOrder(labOrderId);
    }

    @Transactional
    public MedicalImage createMedicalImage(EmrRequests.CreateMedicalImageRequest request) {
        Patient patient = patientStore.findById(request.getPatientId())
                .orElseThrow(() -> new CommonException(ErrorCode.PATIENT_NOT_FOUND));

        MedicalRecord medicalRecord = null;
        if (request.getMedicalRecordId() != null) {
            medicalRecord = getMedicalRecord(request.getMedicalRecordId());
        }

        LabOrder labOrder = null;
        if (request.getLabOrderId() != null) {
            labOrder = getLabOrder(request.getLabOrderId());
        }

        MedicalImage medicalImage = MedicalImage.create(
                patient,
                medicalRecord,
                labOrder,
                request.getImageType(),
                request.getModality(),
                request.getFilePath(),
                request.getFileName(),
                request.getFileSize(),
                request.getMimeType(),
                request.getStudyDate(),
                request.getStudyDescription()
        );

        return emrStore.saveMedicalImage(medicalImage);
    }

    @Transactional
    public MedicalImage addInterpretation(Long id, EmrRequests.UpdateMedicalImageInterpretationRequest request) {
        MedicalImage medicalImage = getMedicalImage(id);
        Doctor radiologist = doctorStore.findById(request.getRadiologistId())
                .orElseThrow(() -> new CommonException(ErrorCode.DOCTOR_NOT_FOUND));

        medicalImage.addInterpretation(radiologist, request.getInterpretation());
        return emrStore.saveMedicalImage(medicalImage);
    }

    @Transactional
    public MedicalImage finalizeMedicalImage(Long id) {
        MedicalImage medicalImage = getMedicalImage(id);
        medicalImage.finalize();
        return emrStore.saveMedicalImage(medicalImage);
    }

    @Transactional
    public MedicalImage cancelMedicalImage(Long id) {
        MedicalImage medicalImage = getMedicalImage(id);
        medicalImage.cancel();
        return emrStore.saveMedicalImage(medicalImage);
    }

    @Transactional
    public MedicalImage updateMedicalImageNotes(Long id, String notes) {
        MedicalImage medicalImage = getMedicalImage(id);
        medicalImage.updateNotes(notes);
        return emrStore.saveMedicalImage(medicalImage);
    }

    // Medical Certificate operations
    public MedicalCertificate getMedicalCertificate(Long id) {
        return emrStore.findMedicalCertificateById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "진단서/소견서를 찾을 수 없습니다."));
    }

    public MedicalCertificate getMedicalCertificateByNumber(String certificateNumber) {
        return emrStore.findMedicalCertificateByNumber(certificateNumber)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "진단서/소견서를 찾을 수 없습니다."));
    }

    public List<MedicalCertificate> getMedicalCertificatesByPatient(Long patientId) {
        return emrStore.findMedicalCertificatesByPatient(patientId);
    }

    public Page<MedicalCertificate> getMedicalCertificatesByPatient(Long patientId, Pageable pageable) {
        return emrStore.findMedicalCertificatesByPatient(patientId, pageable);
    }

    @Transactional
    public MedicalCertificate createMedicalCertificate(EmrRequests.CreateMedicalCertificateRequest request) {
        Patient patient = patientStore.findById(request.getPatientId())
                .orElseThrow(() -> new CommonException(ErrorCode.PATIENT_NOT_FOUND));

        Doctor doctor = doctorStore.findById(request.getDoctorId())
                .orElseThrow(() -> new CommonException(ErrorCode.DOCTOR_NOT_FOUND));

        MedicalRecord medicalRecord = null;
        if (request.getMedicalRecordId() != null) {
            medicalRecord = getMedicalRecord(request.getMedicalRecordId());
        }

        // 증명서 번호 생성 (예: CERT-YYYYMMDD-XXXX)
        String certificateNumber = "CERT-" + LocalDate.now().toString().replace("-", "") + "-" + System.currentTimeMillis() % 10000;

        // 중복 체크
        while (emrStore.existsMedicalCertificateByNumber(certificateNumber)) {
            certificateNumber = "CERT-" + LocalDate.now().toString().replace("-", "") + "-" + System.currentTimeMillis() % 10000;
        }

        MedicalCertificate certificate = MedicalCertificate.create(
                patient,
                medicalRecord,
                doctor,
                request.getCertificateType(),
                certificateNumber,
                request.getIssueDate(),
                request.getEffectiveDate(),
                request.getExpiryDate(),
                request.getContent(),
                request.getDiagnosis(),
                request.getPurpose()
        );

        return emrStore.saveMedicalCertificate(certificate);
    }

    @Transactional
    public MedicalCertificate cancelMedicalCertificate(Long id) {
        MedicalCertificate certificate = getMedicalCertificate(id);
        certificate.cancel();
        return emrStore.saveMedicalCertificate(certificate);
    }

    @Transactional
    public MedicalCertificate updateMedicalCertificateNotes(Long id, String notes) {
        MedicalCertificate certificate = getMedicalCertificate(id);
        certificate.updateNotes(notes);
        return emrStore.saveMedicalCertificate(certificate);
    }

    /**
     * 약물 알레르기 경고 알림 전송
     */
    private void sendDrugAllergyAlert(Doctor doctor, Patient patient, String drugName, String severity, String message) {
        try {
            String payload = objectMapper.writeValueAsString(java.util.Map.of(
                    "patientId", patient.getPatientId(),
                    "patientName", patient.getName(),
                    "drugName", drugName,
                    "severity", severity,
                    "message", message
            ));
            
            // 의사에게 SMS 알림 전송
            notificationFacade.enqueueNotification("SMS", doctor.getPhone(), "DRUG_ALLERGY_ALERT", payload);
            
            // 의사에게 EMAIL 알림도 전송 (이메일 주소가 있다면)
            // notificationFacade.enqueueNotification("EMAIL", doctor.getEmail(), "DRUG_ALLERGY_ALERT", payload);
        } catch (Exception e) {
            // 알림 전송 실패는 처방 프로세스를 중단하지 않음
            // 로깅만 수행 (실제 운영 환경에서는 로깅 추가)
        }
    }

    /**
     * 약물 상호작용 체크 및 경고 알림 전송
     */
    private void checkAndNotifyDrugInteractions(Doctor doctor, Patient patient, List<Long> drugIds, DrugMaster newDrug) {
        if (drugIds == null || drugIds.size() < 2) {
            return;
        }

        List<DrugInteraction> interactions = pharmacyService.checkInteractions(drugIds);
        
        for (DrugInteraction interaction : interactions) {
            if (interaction.isContraindicated() || interaction.isMajor()) {
                // 심각한 상호작용: 예외 발생 및 알림 생성
                String message = String.format("심각한 약물 상호작용이 발견되었습니다: %s와 %s - %s",
                        interaction.getDrug1().getDrugName(),
                        interaction.getDrug2().getDrugName(),
                        interaction.getDescription());
                sendDrugInteractionAlert(doctor, patient, interaction, "SEVERE", message);
                throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION, message);
            } else {
                // 경고 수준 상호작용: 알림 생성하고 처방은 진행
                String message = String.format("약물 상호작용 경고: %s와 %s - %s",
                        interaction.getDrug1().getDrugName(),
                        interaction.getDrug2().getDrugName(),
                        interaction.getDescription());
                sendDrugInteractionAlert(doctor, patient, interaction, "WARNING", message);
            }
        }
    }

    /**
     * 약물 상호작용 경고 알림 전송
     */
    private void sendDrugInteractionAlert(Doctor doctor, Patient patient, DrugInteraction interaction, String severity, String message) {
        try {
            String payload = objectMapper.writeValueAsString(java.util.Map.of(
                    "patientId", patient.getPatientId(),
                    "patientName", patient.getName(),
                    "drug1Name", interaction.getDrug1().getDrugName(),
                    "drug2Name", interaction.getDrug2().getDrugName(),
                    "severity", severity,
                    "interactionSeverity", interaction.getSeverity(),
                    "description", interaction.getDescription(),
                    "clinicalSignificance", interaction.getClinicalSignificance() != null ? interaction.getClinicalSignificance() : "",
                    "management", interaction.getManagement() != null ? interaction.getManagement() : "",
                    "message", message
            ));
            
            // 의사에게 SMS 알림 전송
            notificationFacade.enqueueNotification("SMS", doctor.getPhone(), "DRUG_INTERACTION_ALERT", payload);
            
            // 의사에게 EMAIL 알림도 전송 (이메일 주소가 있다면)
            // notificationFacade.enqueueNotification("EMAIL", doctor.getEmail(), "DRUG_INTERACTION_ALERT", payload);
        } catch (Exception e) {
            // 알림 전송 실패는 처방 프로세스를 중단하지 않음
            // 로깅만 수행 (실제 운영 환경에서는 로깅 추가)
        }
    }
}
