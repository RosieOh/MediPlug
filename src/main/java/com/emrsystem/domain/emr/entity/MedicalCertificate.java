package com.emrsystem.domain.emr.entity;

import com.emrsystem.domain.doctor.entity.Doctor;
import com.emrsystem.domain.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "medical_certificate")
@EntityListeners(AuditingEntityListener.class)
public class MedicalCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certificate_id")
    private Long certificateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_record_id")
    private MedicalRecord medicalRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(nullable = false, length = 50)
    private String certificateType; // DIAGNOSIS, MEDICAL_OPINION, DISCHARGE_SUMMARY, etc.

    @Column(nullable = false, length = 100)
    private String certificateNumber; // 증명서 번호

    @Column(nullable = false)
    private LocalDate issueDate; // 발급일

    @Column(nullable = false)
    private LocalDate effectiveDate; // 유효기간 시작일

    @Column
    private LocalDate expiryDate; // 유효기간 종료일

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 증명서 내용

    @Column(columnDefinition = "TEXT")
    private String diagnosis; // 진단명

    @Column(columnDefinition = "TEXT")
    private String purpose; // 발급 목적

    @Column(nullable = false, length = 50)
    private String status; // ISSUED, CANCELLED, EXPIRED

    @Column(columnDefinition = "TEXT")
    private String notes; // 비고

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private MedicalCertificate(Patient patient, MedicalRecord medicalRecord, Doctor doctor, String certificateType,
                              String certificateNumber, LocalDate issueDate, LocalDate effectiveDate, LocalDate expiryDate,
                              String content, String diagnosis, String purpose, String status, String notes,
                              LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.patient = patient;
        this.medicalRecord = medicalRecord;
        this.doctor = doctor;
        this.certificateType = certificateType;
        this.certificateNumber = certificateNumber;
        this.issueDate = issueDate;
        this.effectiveDate = effectiveDate;
        this.expiryDate = expiryDate;
        this.content = content;
        this.diagnosis = diagnosis;
        this.purpose = purpose;
        this.status = status;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static MedicalCertificate create(Patient patient, MedicalRecord medicalRecord, Doctor doctor,
                                           String certificateType, String certificateNumber, LocalDate issueDate,
                                           LocalDate effectiveDate, LocalDate expiryDate, String content,
                                           String diagnosis, String purpose) {
        LocalDateTime now = LocalDateTime.now();
        return new MedicalCertificate(patient, medicalRecord, doctor, certificateType, certificateNumber, issueDate,
                effectiveDate, expiryDate, content, diagnosis, purpose, "ISSUED", null, now, now);
    }

    public void cancel() {
        this.status = "CANCELLED";
    }

    public void expire() {
        this.status = "EXPIRED";
    }

    public void updateNotes(String notes) {
        this.notes = notes;
    }

    public Long getId() {
        return this.certificateId;
    }
}

