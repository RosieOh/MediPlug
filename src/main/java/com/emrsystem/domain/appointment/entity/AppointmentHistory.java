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
@Table(name = "appointment_history")
@EntityListeners(AuditingEntityListener.class)
public class AppointmentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long historyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @Column(nullable = false, length = 50)
    private String actionType; // CREATED, RESCHEDULED, CANCELLED, COMPLETED, NO_SHOW

    @Column(length = 200)
    private String changedField; // 변경된 필드명

    @Column(length = 500)
    private String oldValue; // 이전 값

    @Column(length = 500)
    private String newValue; // 새 값

    @Column(length = 200)
    private String changedBy; // 변경한 사람

    @Column(columnDefinition = "TEXT")
    private String reason; // 변경 사유

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private AppointmentHistory(Appointment appointment, String actionType, String changedField,
                              String oldValue, String newValue, String changedBy, String reason) {
        this.appointment = appointment;
        this.actionType = actionType;
        this.changedField = changedField;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.changedBy = changedBy;
        this.reason = reason;
    }

    public static AppointmentHistory create(Appointment appointment, String actionType, String changedBy) {
        return new AppointmentHistory(appointment, actionType, null, null, null, changedBy, null);
    }

    public static AppointmentHistory createChange(Appointment appointment, String changedField,
                                                  String oldValue, String newValue, String changedBy, String reason) {
        return new AppointmentHistory(appointment, "RESCHEDULED", changedField, oldValue, newValue, changedBy, reason);
    }

    public Long getId() {
        return this.historyId;
    }
}

