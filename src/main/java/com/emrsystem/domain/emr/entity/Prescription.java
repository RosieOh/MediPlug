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
@Table(name = "prescription")
@EntityListeners(AuditingEntityListener.class)
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prescription_id")
    private Long prescriptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(nullable = false, length = 100)
    private String drugName; // 약물명

    @Column(nullable = false, length = 50)
    private String drugCode; // 약물코드

    @Column(nullable = false, length = 200)
    private String dosage; // 용량

    @Column(nullable = false, length = 100)
    private String frequency; // 복용횟수

    @Column(nullable = false, length = 100)
    private String duration; // 복용기간

    @Column(nullable = false, length = 50)
    private String unit; // 단위 (정, ml, mg 등)

    @Column(columnDefinition = "TEXT")
    private String instructions; // 복용법

    @Column(length = 500)
    private String sideEffects; // 부작용

    @Column(nullable = false, length = 50)
    private String status; // PRESCRIBED, DISPENSED, CANCELLED

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private Prescription(Appointment appointment, Patient patient, Doctor doctor, String drugName, String drugCode,
                        String dosage, String frequency, String duration, String unit, String instructions,
                        String sideEffects, String status) {
        this.appointment = appointment;
        this.patient = patient;
        this.doctor = doctor;
        this.drugName = drugName;
        this.drugCode = drugCode;
        this.dosage = dosage;
        this.frequency = frequency;
        this.duration = duration;
        this.unit = unit;
        this.instructions = instructions;
        this.sideEffects = sideEffects;
        this.status = status;
    }

    public static Prescription create(Appointment appointment, Patient patient, Doctor doctor, String drugName,
                                     String drugCode, String dosage, String frequency, String duration, String unit,
                                     String instructions, String sideEffects) {
        return new Prescription(appointment, patient, doctor, drugName, drugCode, dosage, frequency, duration, unit,
                instructions, sideEffects, "PRESCRIBED");
    }

    public void dispense() {
        this.status = "DISPENSED";
    }

    public void cancel() {
        this.status = "CANCELLED";
    }

    public void updateDosage(String dosage, String frequency, String duration, String instructions) {
        this.dosage = dosage;
        this.frequency = frequency;
        this.duration = duration;
        this.instructions = instructions;
    }

    public Long getId() { return this.prescriptionId; }

    // Compatibility shims for responses expecting these getters
    public String getNotes() { return this.instructions; }
    public com.emrsystem.domain.emr.entity.MedicalRecord getMedicalRecord() { return null; }
}
