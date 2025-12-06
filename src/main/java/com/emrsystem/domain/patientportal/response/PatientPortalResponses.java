package com.emrsystem.domain.patientportal.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PatientPortalResponses {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatientSummary {
        private Long patientId;
        private String patientName;
        private long medicalRecordCount;
        private long appointmentCount;
        private long prescriptionCount;
    }
}

