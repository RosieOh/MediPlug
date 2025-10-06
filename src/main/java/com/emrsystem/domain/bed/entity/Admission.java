package com.emrsystem.domain.bed.entity;

import com.emrsystem.domain.patient.entity.Patient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "admission")
@EntityListeners(AuditingEntityListener.class)
public class Admission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admission_id")
    private Long admissionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bed_id", nullable = false)
    private Bed bed;

    @Column(nullable = false, length = 50)
    private String admissionNumber; // 입원번호

    @Column(nullable = false)
    private LocalDateTime admissionDate; // 입원일시

    @Column
    private LocalDateTime dischargeDate; // 퇴원일시

    @Column(nullable = false, length = 50)
    private String status; // ADMITTED, DISCHARGED, TRANSFERRED

    @Column(nullable = false, length = 100)
    private String admissionReason; // 입원사유

    @Column(length = 500)
    private String dischargeSummary; // 퇴원요약

    @Column(length = 500)
    private String notes; // 특이사항

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private Admission(Patient patient, Bed bed, String admissionNumber, LocalDateTime admissionDate,
                     String admissionReason, String status) {
        this.patient = patient;
        this.bed = bed;
        this.admissionNumber = admissionNumber;
        this.admissionDate = admissionDate;
        this.admissionReason = admissionReason;
        this.status = status;
    }

    public static Admission create(Patient patient, Bed bed, String admissionNumber, LocalDateTime admissionDate,
                                 String admissionReason) {
        return new Admission(patient, bed, admissionNumber, admissionDate, admissionReason, "ADMITTED");
    }

    public void discharge(LocalDateTime dischargeDate, String dischargeSummary) {
        this.dischargeDate = dischargeDate;
        this.dischargeSummary = dischargeSummary;
        this.status = "DISCHARGED";
        this.bed.release();
    }

    public void transfer(Bed newBed) {
        this.bed.release();
        this.bed = newBed;
        this.bed.occupy();
        this.status = "TRANSFERRED";
    }

    public void updateNotes(String notes) {
        this.notes = notes;
    }

    public boolean isActive() {
        return "ADMITTED".equals(this.status);
    }

    public Long getId() { return this.admissionId; }
}
