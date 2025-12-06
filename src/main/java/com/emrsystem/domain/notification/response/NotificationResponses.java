package com.emrsystem.domain.notification.response;

import com.emrsystem.domain.notification.entity.NotificationJob;
import com.emrsystem.domain.notification.entity.NotificationTemplate;
import com.emrsystem.domain.notification.enums.NotificationStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class NotificationResponses {

    @Getter
    @NoArgsConstructor
    public static class TemplateSummary {
        private Long notificationTemplateId;
        private String channel;
        private String code;
        private String title;
        private String body;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static TemplateSummary of(NotificationTemplate template) {
            TemplateSummary summary = new TemplateSummary();
            summary.notificationTemplateId = template.getNotificationTemplateId();
            summary.channel = template.getChannel();
            summary.code = template.getCode();
            summary.title = template.getTitle();
            summary.body = template.getBody();
            summary.createdAt = template.getCreatedAt();
            summary.updatedAt = template.getUpdatedAt();
            return summary;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class JobSummary {
        private Long notificationJobId;
        private String channel;
        private String recipient;
        private String templateCode;
        private String payload;
        private NotificationStatus status;
        private int retryCount;
        private String errorMessage;
        private LocalDateTime nextAttemptAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static JobSummary of(NotificationJob job) {
            JobSummary summary = new JobSummary();
            summary.notificationJobId = job.getNotificationJobId();
            summary.channel = job.getChannel();
            summary.recipient = job.getRecipient();
            summary.templateCode = job.getTemplateCode();
            summary.payload = job.getPayload();
            summary.status = job.getStatus();
            summary.retryCount = job.getRetryCount();
            summary.errorMessage = job.getErrorMessage();
            summary.nextAttemptAt = job.getNextAttemptAt();
            summary.createdAt = job.getCreatedAt();
            summary.updatedAt = job.getUpdatedAt();
            return summary;
        }
    }
}
