package com.emrsystem.domain.prescription.response;

import com.emrsystem.domain.emr.entity.Prescription;
import com.emrsystem.domain.prescription.entity.MedicationLog;
import com.emrsystem.domain.prescription.entity.PrescriptionHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PrescriptionHistoryResponses {

    @Getter
    @NoArgsConstructor
    public static class PrescriptionSummary {
        private Long prescriptionId;
        private Long patientId;
        private String patientName;
        private Long doctorId;
        private String doctorName;
        private String drugName;
        private String drugCode;
        private String dosage;
        private String frequency;
        private String duration;
        private String unit;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static PrescriptionSummary of(Prescription prescription) {
            PrescriptionSummary summary = new PrescriptionSummary();
            summary.prescriptionId = prescription.getId();
            summary.patientId = prescription.getPatient().getId();
            summary.patientName = prescription.getPatient().getName();
            summary.doctorId = prescription.getDoctor().getId();
            summary.doctorName = prescription.getDoctor().getName();
            summary.drugName = prescription.getDrugName();
            summary.drugCode = prescription.getDrugCode();
            summary.dosage = prescription.getDosage();
            summary.frequency = prescription.getFrequency();
            summary.duration = prescription.getDuration();
            summary.unit = prescription.getUnit();
            summary.status = prescription.getStatus();
            summary.createdAt = prescription.getCreatedAt();
            summary.updatedAt = prescription.getUpdatedAt();
            return summary;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class PrescriptionHistorySummary {
        private Long historyId;
        private Long prescriptionId;
        private String actionType;
        private String changedField;
        private String oldValue;
        private String newValue;
        private String changedBy;
        private String reason;
        private LocalDateTime createdAt;

        public static PrescriptionHistorySummary of(PrescriptionHistory history) {
            return new PrescriptionHistorySummary(
                    history.getId(),
                    history.getPrescription().getId(),
                    history.getActionType(),
                    history.getChangedField(),
                    history.getOldValue(),
                    history.getNewValue(),
                    history.getChangedBy(),
                    history.getReason(),
                    history.getCreatedAt()
            );
        }
    }

    @Getter
    @AllArgsConstructor
    public static class MedicationLogSummary {
        private Long logId;
        private Long prescriptionId;
        private String drugName;
        private Long patientId;
        private String patientName;
        private LocalDate medicationDate;
        private LocalDateTime medicationTime;
        private boolean taken;
        private String recordedBy;
        private String notes;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static MedicationLogSummary of(MedicationLog log) {
            return new MedicationLogSummary(
                    log.getId(),
                    log.getPrescription().getId(),
                    log.getPrescription().getDrugName(),
                    log.getPatient().getId(),
                    log.getPatient().getName(),
                    log.getMedicationDate(),
                    log.getMedicationTime(),
                    log.isTaken(),
                    log.getRecordedBy(),
                    log.getNotes(),
                    log.getCreatedAt(),
                    log.getUpdatedAt()
            );
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DuplicatePrescriptionCheck {
        private Long patientId;
        private String patientName;
        private String drugCode;
        private boolean hasDuplicate;
        private int duplicateCount;
        private List<PrescriptionSummary> prescriptions;
    }
}

