package com.emrsystem.domain.prescription.entity;

import com.emrsystem.domain.emr.entity.Prescription;
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
@Table(name = "medication_log")
@EntityListeners(AuditingEntityListener.class)
public class MedicationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private LocalDate medicationDate; // 복용일

    @Column(nullable = false)
    private LocalDateTime medicationTime; // 복용 시각

    @Column(nullable = false)
    private boolean taken = false; // 복용 여부

    @Column(length = 200)
    private String recordedBy; // 기록한 사람 (환자 또는 의료진)

    @Column(columnDefinition = "TEXT")
    private String notes; // 비고

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private MedicationLog(Prescription prescription, Patient patient, LocalDate medicationDate,
                         LocalDateTime medicationTime, boolean taken, String recordedBy, String notes) {
        this.prescription = prescription;
        this.patient = patient;
        this.medicationDate = medicationDate;
        this.medicationTime = medicationTime;
        this.taken = taken;
        this.recordedBy = recordedBy;
        this.notes = notes;
    }

    public static MedicationLog create(Prescription prescription, Patient patient, LocalDate medicationDate,
                                       LocalDateTime medicationTime, boolean taken, String recordedBy) {
        return new MedicationLog(prescription, patient, medicationDate, medicationTime, taken, recordedBy, null);
    }

    public void markAsTaken() {
        this.taken = true;
    }

    public void markAsNotTaken() {
        this.taken = false;
    }

    public void updateNotes(String notes) {
        this.notes = notes;
    }

    public Long getId() {
        return this.logId;
    }
}

