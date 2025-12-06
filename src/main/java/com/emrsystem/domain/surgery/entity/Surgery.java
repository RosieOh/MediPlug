package com.emrsystem.domain.surgery.entity;

import com.emrsystem.domain.appointment.entity.Appointment;
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
@Table(name = "surgery")
@EntityListeners(AuditingEntityListener.class)
public class Surgery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "surgery_id")
    private Long surgeryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Column(nullable = false, length = 200)
    private String surgeryName; // 수술명

    @Column(length = 50)
    private String surgeryCode; // 수술 코드 (KCD-7)

    @Column(nullable = false)
    private LocalDateTime scheduledDateTime; // 예정 일시

    @Column
    private LocalDateTime actualStartDateTime; // 실제 시작 일시

    @Column
    private LocalDateTime actualEndDateTime; // 실제 종료 일시

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surgeon_id", nullable = false)
    private Doctor surgeon; // 주의사

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anesthesiologist_id")
    private Doctor anesthesiologist; // 마취의

    @Column(length = 50)
    private String anesthesiaType; // 마취 방법 (전신마취, 국소마취, 척추마취 등)

    @Column(length = 50)
    private String surgeryRoom; // 수술실

    @Column(nullable = false, length = 50)
    private String status; // SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED, POSTPONED

    @Column(columnDefinition = "TEXT")
    private String preOpDiagnosis; // 수술 전 진단

    @Column(columnDefinition = "TEXT")
    private String postOpDiagnosis; // 수술 후 진단

    @Column(name = "surgery_procedure", columnDefinition = "TEXT")
    private String surgeryProcedure; // 수술 절차

    @Column(columnDefinition = "TEXT")
    private String findings; // 소견

    @Column(columnDefinition = "TEXT")
    private String complications; // 합병증

    @Column(columnDefinition = "TEXT")
    private String notes; // 비고

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private Surgery(Patient patient, Appointment appointment, String surgeryName, String surgeryCode,
                   LocalDateTime scheduledDateTime, Doctor surgeon, Doctor anesthesiologist, String anesthesiaType,
                   String surgeryRoom, String status, String preOpDiagnosis) {
        this.patient = patient;
        this.appointment = appointment;
        this.surgeryName = surgeryName;
        this.surgeryCode = surgeryCode;
        this.scheduledDateTime = scheduledDateTime;
        this.surgeon = surgeon;
        this.anesthesiologist = anesthesiologist;
        this.anesthesiaType = anesthesiaType;
        this.surgeryRoom = surgeryRoom;
        this.status = status;
        this.preOpDiagnosis = preOpDiagnosis;
    }

    public static Surgery create(Patient patient, Appointment appointment, String surgeryName, String surgeryCode,
                                LocalDateTime scheduledDateTime, Doctor surgeon, Doctor anesthesiologist,
                                String anesthesiaType, String surgeryRoom, String preOpDiagnosis) {
        return new Surgery(patient, appointment, surgeryName, surgeryCode, scheduledDateTime, surgeon,
                anesthesiologist, anesthesiaType, surgeryRoom, "SCHEDULED", preOpDiagnosis);
    }

    public void start(LocalDateTime startDateTime) {
        this.actualStartDateTime = startDateTime;
        this.status = "IN_PROGRESS";
    }

    public void complete(LocalDateTime endDateTime, String postOpDiagnosis, String surgeryProcedure, String findings) {
        this.actualEndDateTime = endDateTime;
        this.status = "COMPLETED";
        this.postOpDiagnosis = postOpDiagnosis;
        this.surgeryProcedure = surgeryProcedure;
        this.findings = findings;
    }

    // Compatibility getter
    public String getProcedure() {
        return this.surgeryProcedure;
    }

    public void cancel(String reason) {
        this.status = "CANCELLED";
        this.notes = reason;
    }

    public void postpone(LocalDateTime newDateTime, String reason) {
        this.scheduledDateTime = newDateTime;
        this.status = "POSTPONED";
        this.notes = reason;
    }

    public void addComplication(String complication) {
        this.complications = this.complications != null 
                ? this.complications + "\n" + complication 
                : complication;
    }

    public void updateNotes(String notes) {
        this.notes = notes;
    }

    public Long getId() {
        return this.surgeryId;
    }
}

