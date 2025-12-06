package com.emrsystem.domain.integration.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class IntegrationResponses {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IntegrationStatus {
        private String systemType; // HL7_FHIR, LIS, PACS, PHARMACY, INSURANCE
        private boolean connected;
        private LocalDateTime lastSyncTime;
        private String status; // CONNECTED, DISCONNECTED, NOT_CONFIGURED, ERROR
        private String message;
    }
}

