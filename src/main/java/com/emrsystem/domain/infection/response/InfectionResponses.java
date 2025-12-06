package com.emrsystem.domain.infection.response;

import com.emrsystem.domain.infection.entity.InfectionCase;
import com.emrsystem.domain.infection.entity.InfectionPrevention;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class InfectionResponses {

    @Getter
    @NoArgsConstructor
    public static class InfectionCaseSummary {
        private Long infectionCaseId;
        private Long patientId;
        private String patientName;
        private String infectionType;
        private String infectionName;
        private LocalDate diagnosedDate;
        private LocalDate resolvedDate;
        private String status;
        private String severity;
        private String isolationRoom;
        private String symptoms;
        private String treatment;
        private String preventionMeasures;
        private String notes;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static InfectionCaseSummary of(InfectionCase infectionCase) {
            InfectionCaseSummary summary = new InfectionCaseSummary();
            summary.infectionCaseId = infectionCase.getId();
            summary.patientId = infectionCase.getPatient().getId();
            summary.patientName = infectionCase.getPatient().getName();
            summary.infectionType = infectionCase.getInfectionType();
            summary.infectionName = infectionCase.getInfectionName();
            summary.diagnosedDate = infectionCase.getDiagnosedDate();
            summary.resolvedDate = infectionCase.getResolvedDate();
            summary.status = infectionCase.getStatus();
            summary.severity = infectionCase.getSeverity();
            summary.isolationRoom = infectionCase.getIsolationRoom();
            summary.symptoms = infectionCase.getSymptoms();
            summary.treatment = infectionCase.getTreatment();
            summary.preventionMeasures = infectionCase.getPreventionMeasures();
            summary.notes = infectionCase.getNotes();
            summary.createdAt = infectionCase.getCreatedAt();
            summary.updatedAt = infectionCase.getUpdatedAt();
            return summary;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class InfectionPreventionSummary {
        private Long preventionId;
        private Long infectionCaseId;
        private String measure;
        private LocalDateTime implementedAt;
        private String implementedBy;
        private boolean active;
        private String notes;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static InfectionPreventionSummary of(InfectionPrevention prevention) {
            return new InfectionPreventionSummary(
                    prevention.getId(),
                    prevention.getInfectionCase().getId(),
                    prevention.getMeasure(),
                    prevention.getImplementedAt(),
                    prevention.getImplementedBy(),
                    prevention.isActive(),
                    prevention.getNotes(),
                    prevention.getCreatedAt(),
                    prevention.getUpdatedAt()
            );
        }
    }

    @Getter
    @NoArgsConstructor
    public static class InfectionCaseDetail {
        private InfectionCaseSummary infectionCase;
        private List<InfectionPreventionSummary> preventions;

        public static InfectionCaseDetail of(InfectionCase infectionCase, List<InfectionPrevention> preventions) {
            InfectionCaseDetail detail = new InfectionCaseDetail();
            detail.infectionCase = InfectionCaseSummary.of(infectionCase);
            detail.preventions = preventions.stream()
                    .map(InfectionPreventionSummary::of)
                    .collect(Collectors.toList());
            return detail;
        }
    }
}

