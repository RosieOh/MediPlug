package com.emrsystem.domain.appointment.entity;

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
@Table(name = "appointment_reminder")
@EntityListeners(AuditingEntityListener.class)
public class AppointmentReminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reminder_id")
    private Long reminderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @Column(nullable = false, length = 50)
    private String channel; // SMS, EMAIL, PUSH

    @Column(nullable = false)
    private LocalDateTime scheduledAt; // 예약된 발송 시간

    @Column
    private LocalDateTime sentAt; // 실제 발송 시간

    @Column(nullable = false, length = 50)
    private String status; // PENDING, SENT, FAILED, CANCELLED

    @Column(length = 500)
    private String recipient; // 수신자 (전화번호 또는 이메일)

    @Column(columnDefinition = "TEXT")
    private String message; // 메시지 내용

    @Column(columnDefinition = "TEXT")
    private String errorMessage; // 실패 시 오류 메시지

    @Column
    private int retryCount = 0; // 재시도 횟수

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private AppointmentReminder(Appointment appointment, String channel, LocalDateTime scheduledAt,
                                String status, String recipient, String message) {
        this.appointment = appointment;
        this.channel = channel;
        this.scheduledAt = scheduledAt;
        this.status = status;
        this.recipient = recipient;
        this.message = message;
    }

    public static AppointmentReminder create(Appointment appointment, String channel, LocalDateTime scheduledAt,
                                             String recipient, String message) {
        return new AppointmentReminder(appointment, channel, scheduledAt, "PENDING", recipient, message);
    }

    public void markAsSent(LocalDateTime sentAt) {
        this.sentAt = sentAt;
        this.status = "SENT";
    }

    public void markAsFailed(String errorMessage) {
        this.status = "FAILED";
        this.errorMessage = errorMessage;
        this.retryCount++;
    }

    public void cancel() {
        this.status = "CANCELLED";
    }

    public void incrementRetry() {
        this.retryCount++;
    }

    public Long getId() {
        return this.reminderId;
    }
}

