package com.emrsystem.domain.transfusion.response;

import com.emrsystem.domain.transfusion.entity.TransfusionRecord;
import com.emrsystem.domain.transfusion.entity.TransfusionReaction;
import com.emrsystem.domain.transfusion.entity.TransfusionRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TransfusionResponses {

    @Getter
    @NoArgsConstructor
    public static class TransfusionRequestSummary {
        private Long transfusionRequestId;
        private Long patientId;
        private String patientName;
        private Long requestingDoctorId;
        private String requestingDoctorName;
        private Long approvingDoctorId;
        private String approvingDoctorName;
        private String bloodComponent;
        private Integer units;
        private String status;
        private LocalDateTime approvedAt;
        private String reason;
        private String rejectionReason;
        private String notes;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static TransfusionRequestSummary of(TransfusionRequest request) {
            TransfusionRequestSummary summary = new TransfusionRequestSummary();
            summary.transfusionRequestId = request.getId();
            summary.patientId = request.getPatient().getId();
            summary.patientName = request.getPatient().getName();
            summary.requestingDoctorId = request.getRequestingDoctor().getId();
            summary.requestingDoctorName = request.getRequestingDoctor().getName();
            summary.approvingDoctorId = request.getApprovingDoctor() != null ? request.getApprovingDoctor().getId() : null;
            summary.approvingDoctorName = request.getApprovingDoctor() != null ? request.getApprovingDoctor().getName() : null;
            summary.bloodComponent = request.getBloodComponent();
            summary.units = request.getUnits();
            summary.status = request.getStatus();
            summary.approvedAt = request.getApprovedAt();
            summary.reason = request.getReason();
            summary.rejectionReason = request.getRejectionReason();
            summary.notes = request.getNotes();
            summary.createdAt = request.getCreatedAt();
            summary.updatedAt = request.getUpdatedAt();
            return summary;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class TransfusionRecordSummary {
        private Long transfusionRecordId;
        private Long transfusionRequestId;
        private Long patientId;
        private String patientName;
        private Long administeringDoctorId;
        private String administeringDoctorName;
        private String bloodComponent;
        private String bloodType;
        private String bloodBagNumber;
        private Integer units;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String status;
        private String notes;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static TransfusionRecordSummary of(TransfusionRecord record) {
            return new TransfusionRecordSummary(
                    record.getId(),
                    record.getTransfusionRequest().getId(),
                    record.getPatient().getId(),
                    record.getPatient().getName(),
                    record.getAdministeringDoctor() != null ? record.getAdministeringDoctor().getId() : null,
                    record.getAdministeringDoctor() != null ? record.getAdministeringDoctor().getName() : null,
                    record.getBloodComponent(),
                    record.getBloodType(),
                    record.getBloodBagNumber(),
                    record.getUnits(),
                    record.getStartTime(),
                    record.getEndTime(),
                    record.getStatus(),
                    record.getNotes(),
                    record.getCreatedAt(),
                    record.getUpdatedAt()
            );
        }
    }

    @Getter
    @AllArgsConstructor
    public static class TransfusionReactionSummary {
        private Long reactionId;
        private Long transfusionRecordId;
        private String reactionType;
        private String severity;
        private LocalDateTime occurredAt;
        private String symptoms;
        private String treatment;
        private String notes;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static TransfusionReactionSummary of(TransfusionReaction reaction) {
            return new TransfusionReactionSummary(
                    reaction.getId(),
                    reaction.getTransfusionRecord().getId(),
                    reaction.getReactionType(),
                    reaction.getSeverity(),
                    reaction.getOccurredAt(),
                    reaction.getSymptoms(),
                    reaction.getTreatment(),
                    reaction.getNotes(),
                    reaction.getCreatedAt(),
                    reaction.getUpdatedAt()
            );
        }
    }
}

