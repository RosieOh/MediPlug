package com.emrsystem.domain.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "notification_template")
public class NotificationTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_template_id")
    private Long notificationTemplateId;

    @Column(nullable = false, length = 50)
    private String channel; // SMS/EMAIL

    @Column(nullable = false, length = 100, unique = true)
    private String code; // APPT_CREATED 등

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body; // 템플릿 본문

    private NotificationTemplate(String channel, String code, String title, String body) {
        this.channel = channel;
        this.code = code;
        this.title = title;
        this.body = body;
    }

    public static NotificationTemplate of(String channel, String code, String title, String body) {
        return new NotificationTemplate(channel, code, title, body);
    }

    public void update(String channel, String title, String body) {
        this.channel = channel;
        this.title = title;
        this.body = body;
    }

    public Long getId() { return this.notificationTemplateId; }

    // Compatibility for responses
    public java.time.LocalDateTime getCreatedAt() { return null; }
    public java.time.LocalDateTime getUpdatedAt() { return null; }
}


