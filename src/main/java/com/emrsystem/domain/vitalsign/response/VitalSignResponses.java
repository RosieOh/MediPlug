package com.emrsystem.domain.vitalsign.response;

import com.emrsystem.domain.vitalsign.entity.VitalSign;
import com.emrsystem.domain.vitalsign.entity.VitalSignAlert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class VitalSignResponses {

    @Getter
    @NoArgsConstructor
    public static class VitalSignSummary {
        private Long vitalSignId;
        private Long patientId;
        private String patientName;
        private LocalDateTime measuredAt;
        private BigDecimal systolicBP;
        private BigDecimal diastolicBP;
        private BigDecimal heartRate;
        private BigDecimal temperature;
        private BigDecimal respiratoryRate;
        private BigDecimal oxygenSaturation;
        private BigDecimal bloodSugar;
        private BigDecimal painScore;
        private String measuredBy;
        private String device;
        private String notes;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static VitalSignSummary of(VitalSign vitalSign) {
            VitalSignSummary summary = new VitalSignSummary();
            summary.vitalSignId = vitalSign.getId();
            summary.patientId = vitalSign.getPatient().getId();
            summary.patientName = vitalSign.getPatient().getName();
            summary.measuredAt = vitalSign.getMeasuredAt();
            summary.systolicBP = vitalSign.getSystolicBP();
            summary.diastolicBP = vitalSign.getDiastolicBP();
            summary.heartRate = vitalSign.getHeartRate();
            summary.temperature = vitalSign.getTemperature();
            summary.respiratoryRate = vitalSign.getRespiratoryRate();
            summary.oxygenSaturation = vitalSign.getOxygenSaturation();
            summary.bloodSugar = vitalSign.getBloodSugar();
            summary.painScore = vitalSign.getPainScore();
            summary.measuredBy = vitalSign.getMeasuredBy();
            summary.device = vitalSign.getDevice();
            summary.notes = vitalSign.getNotes();
            summary.createdAt = vitalSign.getCreatedAt();
            summary.updatedAt = vitalSign.getUpdatedAt();
            return summary;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class VitalSignAlertSummary {
        private Long alertId;
        private Long vitalSignId;
        private String alertType;
        private String severity;
        private String message;
        private boolean acknowledged;
        private String acknowledgedBy;
        private LocalDateTime acknowledgedAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static VitalSignAlertSummary of(VitalSignAlert alert) {
            return new VitalSignAlertSummary(
                    alert.getId(),
                    alert.getVitalSign().getId(),
                    alert.getAlertType(),
                    alert.getSeverity(),
                    alert.getMessage(),
                    alert.isAcknowledged(),
                    alert.getAcknowledgedBy(),
                    alert.getAcknowledgedAt(),
                    alert.getCreatedAt(),
                    alert.getUpdatedAt()
            );
        }
    }
}

