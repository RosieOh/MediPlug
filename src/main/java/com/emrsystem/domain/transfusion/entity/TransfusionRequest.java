package com.emrsystem.domain.transfusion.entity;

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
@Table(name = "transfusion_request")
@EntityListeners(AuditingEntityListener.class)
public class TransfusionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transfusion_request_id")
    private Long transfusionRequestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requesting_doctor_id", nullable = false)
    private Doctor requestingDoctor;

    @Column(nullable = false, length = 50)
    private String bloodComponent; // 전혈, 적혈구, 혈소판, 혈장 등

    @Column(nullable = false)
    private Integer units; // 수혈 단위

    @Column(nullable = false, length = 50)
    private String status; // PENDING, APPROVED, REJECTED, COMPLETED, CANCELLED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approving_doctor_id")
    private Doctor approvingDoctor; // 승인 의사

    @Column
    private LocalDateTime approvedAt; // 승인 일시

    @Column(columnDefinition = "TEXT")
    private String reason; // 수혈 사유

    @Column(columnDefinition = "TEXT")
    private String rejectionReason; // 거부 사유

    @Column(columnDefinition = "TEXT")
    private String notes; // 비고

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private TransfusionRequest(Patient patient, Doctor requestingDoctor, String bloodComponent, Integer units,
                              String status, String reason, String notes) {
        this.patient = patient;
        this.requestingDoctor = requestingDoctor;
        this.bloodComponent = bloodComponent;
        this.units = units;
        this.status = status;
        this.reason = reason;
        this.notes = notes;
    }

    public static TransfusionRequest create(Patient patient, Doctor requestingDoctor, String bloodComponent,
                                           Integer units, String reason) {
        return new TransfusionRequest(patient, requestingDoctor, bloodComponent, units, "PENDING", reason, null);
    }

    public void approve(Doctor approvingDoctor) {
        this.approvingDoctor = approvingDoctor;
        this.approvedAt = LocalDateTime.now();
        this.status = "APPROVED";
    }

    public void reject(String rejectionReason) {
        this.rejectionReason = rejectionReason;
        this.status = "REJECTED";
    }

    public void complete() {
        this.status = "COMPLETED";
    }

    public void cancel() {
        this.status = "CANCELLED";
    }

    public void updateNotes(String notes) {
        this.notes = notes;
    }

    public Long getId() {
        return this.transfusionRequestId;
    }
}

