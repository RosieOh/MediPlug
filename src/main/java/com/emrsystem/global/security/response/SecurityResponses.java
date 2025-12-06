package com.emrsystem.global.security.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class SecurityResponses {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TwoFactorSetup {
        private String secretKey;
        private String qrCodeUri;
        private List<String> backupCodes;
    }

    @Getter
    @NoArgsConstructor
    public static class SessionSummary {
        private String sessionId;
        private String username;
        private String createdAt;
        private String lastAccessedAt;
        private String ipAddress;
        private String userAgent;

        public static SessionSummary of(com.emrsystem.global.security.entity.UserSession session) {
            SessionSummary summary = new SessionSummary();
            summary.sessionId = session.getSessionId();
            summary.username = session.getUsername();
            summary.createdAt = session.getCreatedAt().toString();
            summary.lastAccessedAt = session.getLastAccessedAt().toString();
            summary.ipAddress = session.getIpAddress();
            summary.userAgent = session.getUserAgent();
            return summary;
        }
    }
}

