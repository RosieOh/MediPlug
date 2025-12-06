package com.emrsystem.domain.emr.response;

import com.emrsystem.domain.emr.entity.ChartTemplate;
import com.emrsystem.domain.emr.entity.DiagnosisCode;
import com.emrsystem.domain.emr.entity.LabOrder;
import com.emrsystem.domain.emr.entity.LabResult;
import com.emrsystem.domain.emr.entity.MedicalCertificate;
import com.emrsystem.domain.emr.entity.MedicalImage;
import com.emrsystem.domain.emr.entity.MedicalRecord;
import com.emrsystem.domain.emr.entity.Prescription;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EmrResponses {

    @Getter
    @NoArgsConstructor
    public static class MedicalRecordSummary {
        private Long medicalRecordId;
        private Long patientId;
        private String patientName;
        private Long doctorId;
        private String doctorName;
        private LocalDateTime recordDate;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static MedicalRecordSummary of(MedicalRecord record) {
            MedicalRecordSummary summary = new MedicalRecordSummary();
            summary.medicalRecordId = record.getMedicalRecordId();
            summary.patientId = record.getPatient().getPatientId();
            summary.patientName = record.getPatient().getName();
            summary.doctorId = record.getDoctor().getDoctorId();
            summary.doctorName = record.getDoctor().getName();
            summary.recordDate = record.getRecordDate();
            summary.createdAt = record.getCreatedAt();
            summary.updatedAt = record.getUpdatedAt();
            return summary;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ChartTemplateSummary {
        private Long chartTemplateId;
        private String code;
        private String name;
        private int version;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static ChartTemplateSummary of(ChartTemplate template) {
            ChartTemplateSummary summary = new ChartTemplateSummary();
            summary.chartTemplateId = template.getChartTemplateId();
            summary.code = template.getCode();
            summary.name = template.getName();
            summary.version = template.getVersion();
            summary.createdAt = template.getCreatedAt();
            summary.updatedAt = template.getUpdatedAt();
            return summary;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class PrescriptionSummary {
        private Long prescriptionId;
        private Long medicalRecordId;
        private String drugName;
        private String dosage;
        private String frequency;
        private String duration;
        private String notes;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static PrescriptionSummary of(Prescription prescription) {
            PrescriptionSummary summary = new PrescriptionSummary();
            summary.prescriptionId = prescription.getPrescriptionId();
            summary.medicalRecordId = prescription.getMedicalRecord().getMedicalRecordId();
            summary.drugName = prescription.getDrugName();
            summary.dosage = prescription.getDosage();
            summary.frequency = prescription.getFrequency();
            summary.duration = prescription.getDuration();
            summary.notes = prescription.getNotes();
            summary.createdAt = prescription.getCreatedAt();
            summary.updatedAt = prescription.getUpdatedAt();
            return summary;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class LabOrderSummary {
        private Long labOrderId;
        private String testName;
        private String status;
        private Long medicalRecordId;
        private LocalDateTime orderDate;
        private LocalDateTime resultDate;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static LabOrderSummary of(LabOrder labOrder) {
            LabOrderSummary summary = new LabOrderSummary();
            summary.labOrderId = labOrder.getLabOrderId();
            summary.testName = labOrder.getTestName();
            summary.status = labOrder.getStatus();
            summary.medicalRecordId = labOrder.getMedicalRecord().getMedicalRecordId();
            summary.orderDate = labOrder.getOrderDate();
            summary.resultDate = labOrder.getResultDate();
            summary.createdAt = labOrder.getCreatedAt();
            summary.updatedAt = labOrder.getUpdatedAt();
            return summary;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class DiagnosisCodeSummary {
        private Long diagnosisCodeId;
        private String code;
        private String name;
        private String category;
        private String description;
        private boolean active;
        private int version;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static DiagnosisCodeSummary of(DiagnosisCode diagnosisCode) {
            DiagnosisCodeSummary summary = new DiagnosisCodeSummary();
            summary.diagnosisCodeId = diagnosisCode.getId();
            summary.code = diagnosisCode.getCode();
            summary.name = diagnosisCode.getName();
            summary.category = diagnosisCode.getCategory();
            summary.description = diagnosisCode.getDescription();
            summary.active = diagnosisCode.isActive();
            summary.version = diagnosisCode.getVersion();
            summary.createdAt = diagnosisCode.getCreatedAt();
            summary.updatedAt = diagnosisCode.getUpdatedAt();
            return summary;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class LabResultSummary {
        private Long labResultId;
        private Long labOrderId;
        private String testItemName;
        private String testItemCode;
        private String resultValue;
        private String unit;
        private String referenceRange;
        private String abnormalFlag;
        private String status;
        private String notes;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static LabResultSummary of(LabResult labResult) {
            LabResultSummary summary = new LabResultSummary();
            summary.labResultId = labResult.getId();
            summary.labOrderId = labResult.getLabOrder().getLabOrderId();
            summary.testItemName = labResult.getTestItemName();
            summary.testItemCode = labResult.getTestItemCode();
            summary.resultValue = labResult.getResultValue();
            summary.unit = labResult.getUnit();
            summary.referenceRange = labResult.getReferenceRange();
            summary.abnormalFlag = labResult.getAbnormalFlag();
            summary.status = labResult.getStatus();
            summary.notes = labResult.getNotes();
            summary.createdAt = labResult.getCreatedAt();
            summary.updatedAt = labResult.getUpdatedAt();
            return summary;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class MedicalImageSummary {
        private Long imageId;
        private Long patientId;
        private Long medicalRecordId;
        private Long labOrderId;
        private String imageType;
        private String modality;
        private String fileName;
        private Long fileSize;
        private String mimeType;
        private LocalDateTime studyDate;
        private String studyDescription;
        private String interpretation;
        private Long radiologistId;
        private String radiologistName;
        private String status;
        private String notes;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static MedicalImageSummary of(MedicalImage image) {
            MedicalImageSummary summary = new MedicalImageSummary();
            summary.imageId = image.getId();
            summary.patientId = image.getPatient().getId();
            summary.medicalRecordId = image.getMedicalRecord() != null ? image.getMedicalRecord().getId() : null;
            summary.labOrderId = image.getLabOrder() != null ? image.getLabOrder().getId() : null;
            summary.imageType = image.getImageType();
            summary.modality = image.getModality();
            summary.fileName = image.getFileName();
            summary.fileSize = image.getFileSize();
            summary.mimeType = image.getMimeType();
            summary.studyDate = image.getStudyDate();
            summary.studyDescription = image.getStudyDescription();
            summary.interpretation = image.getInterpretation();
            summary.radiologistId = image.getRadiologist() != null ? image.getRadiologist().getId() : null;
            summary.radiologistName = image.getRadiologist() != null ? image.getRadiologist().getName() : null;
            summary.status = image.getStatus();
            summary.notes = image.getNotes();
            summary.createdAt = image.getCreatedAt();
            summary.updatedAt = image.getUpdatedAt();
            return summary;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class MedicalCertificateSummary {
        private Long certificateId;
        private Long patientId;
        private String patientName;
        private Long medicalRecordId;
        private Long doctorId;
        private String doctorName;
        private String certificateType;
        private String certificateNumber;
        private LocalDate issueDate;
        private LocalDate effectiveDate;
        private LocalDate expiryDate;
        private String content;
        private String diagnosis;
        private String purpose;
        private String status;
        private String notes;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static MedicalCertificateSummary of(MedicalCertificate certificate) {
            MedicalCertificateSummary summary = new MedicalCertificateSummary();
            summary.certificateId = certificate.getId();
            summary.patientId = certificate.getPatient().getId();
            summary.patientName = certificate.getPatient().getName();
            summary.medicalRecordId = certificate.getMedicalRecord() != null ? certificate.getMedicalRecord().getId() : null;
            summary.doctorId = certificate.getDoctor().getId();
            summary.doctorName = certificate.getDoctor().getName();
            summary.certificateType = certificate.getCertificateType();
            summary.certificateNumber = certificate.getCertificateNumber();
            summary.issueDate = certificate.getIssueDate();
            summary.effectiveDate = certificate.getEffectiveDate();
            summary.expiryDate = certificate.getExpiryDate();
            summary.content = certificate.getContent();
            summary.diagnosis = certificate.getDiagnosis();
            summary.purpose = certificate.getPurpose();
            summary.status = certificate.getStatus();
            summary.notes = certificate.getNotes();
            summary.createdAt = certificate.getCreatedAt();
            summary.updatedAt = certificate.getUpdatedAt();
            return summary;
        }
    }
}
