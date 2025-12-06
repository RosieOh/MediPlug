package com.emrsystem.domain.vitalsign.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "vital_sign_alert")
@EntityListeners(AuditingEntityListener.class)
public class VitalSignAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alert_id")
    private Long alertId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vital_sign_id", nullable = false)
    private VitalSign vitalSign;

    @Column(nullable = false, length = 50)
    private String alertType; // HIGH_BP, LOW_BP, HIGH_TEMP, LOW_TEMP, HIGH_HR, LOW_HR, LOW_SPO2 등

    @Column(nullable = false, length = 50)
    private String severity; // WARNING, CRITICAL

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message; // 알림 메시지

    @Column(nullable = false)
    private boolean acknowledged = false; // 확인 여부

    @Column(length = 200)
    private String acknowledgedBy; // 확인한 사람

    @Column
    private LocalDateTime acknowledgedAt; // 확인 일시

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private VitalSignAlert(VitalSign vitalSign, String alertType, String severity, String message) {
        this.vitalSign = vitalSign;
        this.alertType = alertType;
        this.severity = severity;
        this.message = message;
    }

    public static VitalSignAlert create(VitalSign vitalSign, String alertType, String severity, String message) {
        return new VitalSignAlert(vitalSign, alertType, severity, message);
    }

    public void acknowledge(String acknowledgedBy) {
        this.acknowledged = true;
        this.acknowledgedBy = acknowledgedBy;
        this.acknowledgedAt = LocalDateTime.now();
    }

    public Long getId() {
        return this.alertId;
    }
}

