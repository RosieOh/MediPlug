package com.emrsystem.domain.emr.entity;

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
@Table(name = "medical_record")
@EntityListeners(AuditingEntityListener.class)
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medical_record_id")
    private Long medicalRecordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String chiefComplaint; // 주소

    @Column(columnDefinition = "TEXT")
    private String presentIllness; // 현병력

    @Column(columnDefinition = "TEXT")
    private String pastHistory; // 과거력

    @Column(columnDefinition = "TEXT")
    private String physicalExamination; // 신체검사

    @Column(columnDefinition = "TEXT")
    private String diagnosis; // 진단

    @Column(columnDefinition = "TEXT")
    private String treatment; // 치료

    @Column(columnDefinition = "TEXT")
    private String notes; // 특이사항

    @Column(nullable = false, length = 50)
    private String status; // DRAFT, FINALIZED, AMENDED

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private MedicalRecord(Appointment appointment, Patient patient, Doctor doctor, String chiefComplaint,
                         String presentIllness, String pastHistory, String physicalExamination,
                         String diagnosis, String treatment, String notes, String status) {
        this.appointment = appointment;
        this.patient = patient;
        this.doctor = doctor;
        this.chiefComplaint = chiefComplaint;
        this.presentIllness = presentIllness;
        this.pastHistory = pastHistory;
        this.physicalExamination = physicalExamination;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.notes = notes;
        this.status = status;
    }

    public static MedicalRecord create(Appointment appointment, Patient patient, Doctor doctor, String chiefComplaint) {
        return new MedicalRecord(appointment, patient, doctor, chiefComplaint, null, null, null, null, null, null, "DRAFT");
    }

    public void updateContent(String presentIllness, String pastHistory, String physicalExamination,
                             String diagnosis, String treatment, String notes) {
        this.presentIllness = presentIllness;
        this.pastHistory = pastHistory;
        this.physicalExamination = physicalExamination;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.notes = notes;
    }

    public void finalize() {
        this.status = "FINALIZED";
    }

    public void amend() {
        this.status = "AMENDED";
    }

    public Long getId() { return this.medicalRecordId; }

    // Compatibility getter expected by responses
    public java.time.LocalDateTime getRecordDate() { return this.createdAt; }
}
