package com.emrsystem.domain.notification.entity;

import com.emrsystem.domain.notification.enums.NotificationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "notification_job")
public class NotificationJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_job_id")
    private Long notificationJobId;

    @Column(nullable = false, length = 50)
    private String channel; // SMS/EMAIL

    @Column(nullable = false, length = 200)
    private String recipient;

    @Column(nullable = false, length = 100)
    private String templateCode;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload; // JSON 치환값

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NotificationStatus status;

    @Column(nullable = false)
    private int retryCount;

    @Column(nullable = false)
    private LocalDateTime nextAttemptAt;

    @Column
    private String lastError;

    private NotificationJob(String channel, String recipient, String templateCode, String payload,
                             NotificationStatus status, int retryCount, LocalDateTime nextAttemptAt) {
        this.channel = channel;
        this.recipient = recipient;
        this.templateCode = templateCode;
        this.payload = payload;
        this.status = status;
        this.retryCount = retryCount;
        this.nextAttemptAt = nextAttemptAt;
    }

    public static NotificationJob pending(String channel, String recipient, String templateCode, String payload) {
        return new NotificationJob(channel, recipient, templateCode, payload, NotificationStatus.PENDING, 0, LocalDateTime.now());
    }

    public void markRetry(String error) {
        this.status = NotificationStatus.RETRYING;
        this.retryCount += 1;
        this.lastError = error;
        this.nextAttemptAt = LocalDateTime.now().plusMinutes(Math.min(30, retryCount * 2));
    }

    public void markSent() { this.status = NotificationStatus.SENT; }
    public void markFailed(String error) { this.status = NotificationStatus.FAILED; this.lastError = error; }

    // Compatibility for responses
    public String getErrorMessage() { return this.lastError; }
    public java.time.LocalDateTime getCreatedAt() { return null; }
    public java.time.LocalDateTime getUpdatedAt() { return null; }
}


