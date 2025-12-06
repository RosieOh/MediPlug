package com.emrsystem.domain.emr.facade;

import com.emrsystem.domain.emr.entity.ChartTemplate;
import com.emrsystem.domain.emr.entity.DiagnosisCode;
import com.emrsystem.domain.emr.entity.LabOrder;
import com.emrsystem.domain.emr.entity.LabResult;
import com.emrsystem.domain.emr.entity.MedicalCertificate;
import com.emrsystem.domain.emr.entity.MedicalImage;
import com.emrsystem.domain.emr.entity.MedicalRecord;
import com.emrsystem.domain.emr.entity.Prescription;
import com.emrsystem.domain.emr.request.EmrRequests;
import com.emrsystem.domain.emr.service.EmrService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EmrFacade {

    private final EmrService emrService;

    // Medical Record operations
    // 전체 목록은 미제공: 환자/의사 기준 목록만 제공

    public Page<MedicalRecord> getMedicalRecordsByPatient(Long patientId, Pageable pageable) {
        return emrService.getMedicalRecordsByPatient(patientId, pageable);
    }

    public Page<MedicalRecord> getMedicalRecordsByDoctor(Long doctorId, Pageable pageable) {
        return emrService.getMedicalRecordsByDoctor(doctorId, pageable);
    }

    public MedicalRecord getMedicalRecord(Long id) {
        return emrService.getMedicalRecord(id);
    }

    public MedicalRecord createMedicalRecord(EmrRequests.CreateMedicalRecordRequest request) {
        return emrService.createMedicalRecord(request);
    }

    public MedicalRecord updateMedicalRecord(Long id, EmrRequests.UpdateMedicalRecordRequest request) {
        return emrService.updateMedicalRecord(id, request);
    }

    // 상태 완료 기능 제외

    // Chart Template operations
    public List<ChartTemplate> getChartTemplates() {
        return emrService.getChartTemplates();
    }

    public ChartTemplate getChartTemplate(Long id) {
        return emrService.getChartTemplate(id);
    }

    public ChartTemplate createChartTemplate(EmrRequests.CreateChartTemplateRequest request) {
        return emrService.createChartTemplate(request);
    }

    public ChartTemplate updateChartTemplate(Long id, EmrRequests.UpdateChartTemplateRequest request) {
        return emrService.updateChartTemplate(id, request);
    }

    public void deleteChartTemplate(Long id) {
        emrService.deleteChartTemplate(id);
    }

    // Prescription operations (진료기록 기준)
    public Page<Prescription> getPrescriptionsByMedicalRecord(Long medicalRecordId, Pageable pageable) {
        return emrService.getPrescriptionsByMedicalRecord(medicalRecordId, pageable);
    }

    public Prescription getPrescription(Long id) {
        return emrService.getPrescription(id);
    }

    public Prescription createPrescription(EmrRequests.CreatePrescriptionRequest request) {
        return emrService.createPrescription(request);
    }

    // 처방 상태 전환 제외

    // Lab Order operations (진료기록 기준)
    public Page<LabOrder> getLabOrdersByMedicalRecord(Long medicalRecordId, Pageable pageable) {
        return emrService.getLabOrdersByMedicalRecord(medicalRecordId, pageable);
    }

    public LabOrder getLabOrder(Long id) {
        return emrService.getLabOrder(id);
    }

    public LabOrder createLabOrder(EmrRequests.CreateLabOrderRequest request) {
        return emrService.createLabOrder(request);
    }

    // 상태 전환 제외

    // Diagnosis Code operations
    public DiagnosisCode getDiagnosisCode(Long id) {
        return emrService.getDiagnosisCode(id);
    }

    public DiagnosisCode getDiagnosisCodeByCode(String code) {
        return emrService.getDiagnosisCodeByCode(code);
    }

    public Page<DiagnosisCode> getDiagnosisCodes(Pageable pageable) {
        return emrService.getDiagnosisCodes(pageable);
    }

    public Page<DiagnosisCode> searchDiagnosisCodes(String code, String name, Pageable pageable) {
        return emrService.searchDiagnosisCodes(code, name, pageable);
    }

    public List<DiagnosisCode> getDiagnosisCodesByCategory(String category) {
        return emrService.getDiagnosisCodesByCategory(category);
    }

    public DiagnosisCode createDiagnosisCode(EmrRequests.CreateDiagnosisCodeRequest request) {
        return emrService.createDiagnosisCode(request);
    }

    public DiagnosisCode updateDiagnosisCode(Long id, EmrRequests.UpdateDiagnosisCodeRequest request) {
        return emrService.updateDiagnosisCode(id, request);
    }

    public void deactivateDiagnosisCode(Long id) {
        emrService.deactivateDiagnosisCode(id);
    }

    public void activateDiagnosisCode(Long id) {
        emrService.activateDiagnosisCode(id);
    }

    // Lab Result operations
    public LabResult getLabResult(Long id) {
        return emrService.getLabResult(id);
    }

    public List<LabResult> getLabResultsByLabOrder(Long labOrderId) {
        return emrService.getLabResultsByLabOrder(labOrderId);
    }

    public List<LabResult> getAbnormalLabResults() {
        return emrService.getAbnormalLabResults();
    }

    public LabResult createLabResult(EmrRequests.CreateLabResultRequest request) {
        return emrService.createLabResult(request);
    }

    public LabResult updateLabResult(Long id, EmrRequests.UpdateLabResultRequest request) {
        return emrService.updateLabResult(id, request);
    }

    public void correctLabResult(Long id) {
        emrService.correctLabResult(id);
    }

    public void cancelLabResult(Long id) {
        emrService.cancelLabResult(id);
    }

    // Medical Image operations
    public MedicalImage getMedicalImage(Long id) {
        return emrService.getMedicalImage(id);
    }

    public List<MedicalImage> getMedicalImagesByPatient(Long patientId) {
        return emrService.getMedicalImagesByPatient(patientId);
    }

    public Page<MedicalImage> getMedicalImagesByPatient(Long patientId, Pageable pageable) {
        return emrService.getMedicalImagesByPatient(patientId, pageable);
    }

    public List<MedicalImage> getMedicalImagesByMedicalRecord(Long medicalRecordId) {
        return emrService.getMedicalImagesByMedicalRecord(medicalRecordId);
    }

    public List<MedicalImage> getMedicalImagesByLabOrder(Long labOrderId) {
        return emrService.getMedicalImagesByLabOrder(labOrderId);
    }

    public MedicalImage createMedicalImage(EmrRequests.CreateMedicalImageRequest request) {
        return emrService.createMedicalImage(request);
    }

    public MedicalImage addInterpretation(Long id, EmrRequests.UpdateMedicalImageInterpretationRequest request) {
        return emrService.addInterpretation(id, request);
    }

    public MedicalImage finalizeMedicalImage(Long id) {
        return emrService.finalizeMedicalImage(id);
    }

    public MedicalImage cancelMedicalImage(Long id) {
        return emrService.cancelMedicalImage(id);
    }

    public MedicalImage updateMedicalImageNotes(Long id, String notes) {
        return emrService.updateMedicalImageNotes(id, notes);
    }

    // Medical Certificate operations
    public MedicalCertificate getMedicalCertificate(Long id) {
        return emrService.getMedicalCertificate(id);
    }

    public MedicalCertificate getMedicalCertificateByNumber(String certificateNumber) {
        return emrService.getMedicalCertificateByNumber(certificateNumber);
    }

    public List<MedicalCertificate> getMedicalCertificatesByPatient(Long patientId) {
        return emrService.getMedicalCertificatesByPatient(patientId);
    }

    public Page<MedicalCertificate> getMedicalCertificatesByPatient(Long patientId, Pageable pageable) {
        return emrService.getMedicalCertificatesByPatient(patientId, pageable);
    }

    public MedicalCertificate createMedicalCertificate(EmrRequests.CreateMedicalCertificateRequest request) {
        return emrService.createMedicalCertificate(request);
    }

    public MedicalCertificate cancelMedicalCertificate(Long id) {
        return emrService.cancelMedicalCertificate(id);
    }

    public MedicalCertificate updateMedicalCertificateNotes(Long id, String notes) {
        return emrService.updateMedicalCertificateNotes(id, notes);
    }
}
