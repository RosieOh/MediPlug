package com.emrsystem.domain.emr.store;

import com.emrsystem.domain.emr.entity.ChartTemplate;
import com.emrsystem.domain.emr.entity.DiagnosisCode;
import com.emrsystem.domain.emr.entity.LabOrder;
import com.emrsystem.domain.emr.entity.LabResult;
import com.emrsystem.domain.emr.entity.MedicalCertificate;
import com.emrsystem.domain.emr.entity.MedicalImage;
import com.emrsystem.domain.emr.entity.MedicalRecord;
import com.emrsystem.domain.emr.entity.Prescription;
import com.emrsystem.domain.emr.repository.ChartTemplateRepository;
import com.emrsystem.domain.emr.repository.DiagnosisCodeRepository;
import com.emrsystem.domain.emr.repository.LabOrderRepository;
import com.emrsystem.domain.emr.repository.LabResultRepository;
import com.emrsystem.domain.emr.repository.MedicalCertificateRepository;
import com.emrsystem.domain.emr.repository.MedicalImageRepository;
import com.emrsystem.domain.emr.repository.MedicalRecordRepository;
import com.emrsystem.domain.emr.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EmrStore {

    private final MedicalRecordRepository medicalRecordRepository;
    private final ChartTemplateRepository chartTemplateRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final LabOrderRepository labOrderRepository;
    private final LabResultRepository labResultRepository;
    private final MedicalImageRepository medicalImageRepository;
    private final MedicalCertificateRepository medicalCertificateRepository;
    private final DiagnosisCodeRepository diagnosisCodeRepository;

    // Medical Record operations
    public MedicalRecord saveMedicalRecord(MedicalRecord medicalRecord) {
        return medicalRecordRepository.save(medicalRecord);
    }

    public Optional<MedicalRecord> findMedicalRecordById(Long id) {
        return medicalRecordRepository.findById(id);
    }

    public Page<MedicalRecord> findMedicalRecordsByPatient(Long patientId, Pageable pageable) {
        return medicalRecordRepository.findByPatient_PatientId(patientId, pageable);
    }

    public Page<MedicalRecord> findMedicalRecordsByDoctor(Long doctorId, Pageable pageable) {
        return medicalRecordRepository.findByDoctor_DoctorId(doctorId, pageable);
    }

    public Page<MedicalRecord> findMedicalRecordsByAppointment(Long appointmentId, Pageable pageable) {
        throw new UnsupportedOperationException("find by appointment is not supported in Option A schema");
    }

    public Page<MedicalRecord> findMedicalRecordsByStatus(String status, Pageable pageable) {
        throw new UnsupportedOperationException("find by status is not supported in Option A schema");
    }

    // Chart Template operations
    public ChartTemplate saveChartTemplate(ChartTemplate chartTemplate) {
        return chartTemplateRepository.save(chartTemplate);
    }

    public Optional<ChartTemplate> findChartTemplateById(Long id) {
        return chartTemplateRepository.findById(id);
    }

    public List<ChartTemplate> findAllChartTemplates() {
        return chartTemplateRepository.findAll();
    }

    public Optional<ChartTemplate> findChartTemplateByCode(String code) {
        return chartTemplateRepository.findByCode(code);
    }

    public boolean existsChartTemplateByCode(String code) {
        return chartTemplateRepository.existsByCode(code);
    }

    public void deleteChartTemplateById(Long id) {
        chartTemplateRepository.deleteById(id);
    }

    // Prescription operations
    public Prescription savePrescription(Prescription prescription) {
        return prescriptionRepository.save(prescription);
    }

    public Optional<Prescription> findPrescriptionById(Long id) {
        return prescriptionRepository.findById(id);
    }

    public Page<Prescription> findPrescriptionsByMedicalRecord(Long medicalRecordId, Pageable pageable) {
        return prescriptionRepository.findByMedicalRecord_MedicalRecordId(medicalRecordId, pageable);
    }

    // Lab Order operations
    public LabOrder saveLabOrder(LabOrder labOrder) {
        return labOrderRepository.save(labOrder);
    }

    public Optional<LabOrder> findLabOrderById(Long id) {
        return labOrderRepository.findById(id);
    }

    public Page<LabOrder> findLabOrdersByStatus(String status, Pageable pageable) {
        return labOrderRepository.findByStatus(status, pageable);
    }

    public Page<LabOrder> findLabOrdersByMedicalRecord(Long medicalRecordId, Pageable pageable) {
        return labOrderRepository.findByMedicalRecord_MedicalRecordId(medicalRecordId, pageable);
    }

    // Diagnosis Code operations
    public DiagnosisCode saveDiagnosisCode(DiagnosisCode diagnosisCode) {
        return diagnosisCodeRepository.save(diagnosisCode);
    }

    public Optional<DiagnosisCode> findDiagnosisCodeById(Long id) {
        return diagnosisCodeRepository.findById(id);
    }

    public Optional<DiagnosisCode> findDiagnosisCodeByCode(String code) {
        return diagnosisCodeRepository.findByCode(code);
    }

    public Page<DiagnosisCode> findDiagnosisCodes(Pageable pageable) {
        return diagnosisCodeRepository.findByActiveTrue(pageable);
    }

    public Page<DiagnosisCode> searchDiagnosisCodes(String code, String name, Pageable pageable) {
        return diagnosisCodeRepository.findByCodeContainingIgnoreCaseOrNameContainingIgnoreCase(code, name, pageable);
    }

    public List<DiagnosisCode> findDiagnosisCodesByCategory(String category) {
        return diagnosisCodeRepository.findByCategoryAndActiveTrue(category);
    }

    public boolean existsDiagnosisCodeByCode(String code) {
        return diagnosisCodeRepository.existsByCode(code);
    }

    // Lab Result operations
    public LabResult saveLabResult(LabResult labResult) {
        return labResultRepository.save(labResult);
    }

    public Optional<LabResult> findLabResultById(Long id) {
        return labResultRepository.findById(id);
    }

    public List<LabResult> findLabResultsByLabOrder(Long labOrderId) {
        return labResultRepository.findByLabOrder_LabOrderId(labOrderId);
    }

    public List<LabResult> findLabResultsByLabOrderAndStatus(Long labOrderId, String status) {
        return labResultRepository.findByLabOrder_LabOrderIdAndStatus(labOrderId, status);
    }

    public List<LabResult> findAbnormalLabResults(List<String> abnormalFlags) {
        return labResultRepository.findByAbnormalFlagIn(abnormalFlags);
    }

    // Medical Image operations
    public MedicalImage saveMedicalImage(MedicalImage medicalImage) {
        return medicalImageRepository.save(medicalImage);
    }

    public Optional<MedicalImage> findMedicalImageById(Long id) {
        return medicalImageRepository.findById(id);
    }

    public List<MedicalImage> findMedicalImagesByPatient(Long patientId) {
        return medicalImageRepository.findByPatient_PatientId(patientId);
    }

    public Page<MedicalImage> findMedicalImagesByPatient(Long patientId, Pageable pageable) {
        return medicalImageRepository.findByPatient_PatientId(patientId, pageable);
    }

    public List<MedicalImage> findMedicalImagesByMedicalRecord(Long medicalRecordId) {
        return medicalImageRepository.findByMedicalRecord_MedicalRecordId(medicalRecordId);
    }

    public List<MedicalImage> findMedicalImagesByLabOrder(Long labOrderId) {
        return medicalImageRepository.findByLabOrder_LabOrderId(labOrderId);
    }

    public List<MedicalImage> findMedicalImagesByImageTypeAndStatus(String imageType, String status) {
        return medicalImageRepository.findByImageTypeAndStatus(imageType, status);
    }

    public List<MedicalImage> findMedicalImagesByStatus(String status) {
        return medicalImageRepository.findByStatus(status);
    }

    // Medical Certificate operations
    public MedicalCertificate saveMedicalCertificate(MedicalCertificate medicalCertificate) {
        return medicalCertificateRepository.save(medicalCertificate);
    }

    public Optional<MedicalCertificate> findMedicalCertificateById(Long id) {
        return medicalCertificateRepository.findById(id);
    }

    public Optional<MedicalCertificate> findMedicalCertificateByNumber(String certificateNumber) {
        return medicalCertificateRepository.findByCertificateNumber(certificateNumber);
    }

    public List<MedicalCertificate> findMedicalCertificatesByPatient(Long patientId) {
        return medicalCertificateRepository.findByPatient_PatientId(patientId);
    }

    public Page<MedicalCertificate> findMedicalCertificatesByPatient(Long patientId, Pageable pageable) {
        return medicalCertificateRepository.findByPatient_PatientId(patientId, pageable);
    }

    public List<MedicalCertificate> findMedicalCertificatesByTypeAndStatus(String certificateType, String status) {
        return medicalCertificateRepository.findByCertificateTypeAndStatus(certificateType, status);
    }

    public List<MedicalCertificate> findMedicalCertificatesByStatus(String status) {
        return medicalCertificateRepository.findByStatus(status);
    }

    public boolean existsMedicalCertificateByNumber(String certificateNumber) {
        return medicalCertificateRepository.findByCertificateNumber(certificateNumber).isPresent();
    }
}
