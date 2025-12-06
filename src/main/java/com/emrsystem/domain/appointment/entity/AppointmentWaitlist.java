package com.emrsystem.domain.appointment.entity;

import com.emrsystem.domain.doctor.entity.Doctor;
import com.emrsystem.domain.patient.entity.Patient;
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
@Table(name = "appointment_waitlist")
@EntityListeners(AuditingEntityListener.class)
public class AppointmentWaitlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "waitlist_id")
    private Long waitlistId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private LocalDateTime preferredStartAt; // 희망 시작 시간

    @Column(nullable = false)
    private LocalDateTime preferredEndAt; // 희망 종료 시간

    @Column(nullable = false, length = 50)
    private String status; // PENDING, NOTIFIED, CANCELLED, FULFILLED

    @Column(nullable = false)
    private int priority; // 우선순위 (낮을수록 높음)

    @Column(columnDefinition = "TEXT")
    private String reason; // 대기 목록 등록 사유

    @Column(columnDefinition = "TEXT")
    private String notes; // 비고

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment; // 대기 목록에서 생성된 예약

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private AppointmentWaitlist(Patient patient, Doctor doctor, LocalDateTime preferredStartAt,
                               LocalDateTime preferredEndAt, String status, int priority, String reason, String notes) {
        this.patient = patient;
        this.doctor = doctor;
        this.preferredStartAt = preferredStartAt;
        this.preferredEndAt = preferredEndAt;
        this.status = status;
        this.priority = priority;
        this.reason = reason;
        this.notes = notes;
    }

    public static AppointmentWaitlist create(Patient patient, Doctor doctor, LocalDateTime preferredStartAt,
                                            LocalDateTime preferredEndAt, int priority, String reason) {
        return new AppointmentWaitlist(patient, doctor, preferredStartAt, preferredEndAt, "PENDING", priority, reason, null);
    }

    public void markAsNotified() {
        this.status = "NOTIFIED";
    }

    public void fulfill(Appointment appointment) {
        this.appointment = appointment;
        this.status = "FULFILLED";
    }

    public void cancel() {
        this.status = "CANCELLED";
    }

    public void updatePriority(int priority) {
        this.priority = priority;
    }

    public void updateNotes(String notes) {
        this.notes = notes;
    }

    public Long getId() {
        return this.waitlistId;
    }
}

