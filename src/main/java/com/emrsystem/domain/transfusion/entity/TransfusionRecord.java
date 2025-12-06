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
@Table(name = "transfusion_record")
@EntityListeners(AuditingEntityListener.class)
public class TransfusionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transfusion_record_id")
    private Long transfusionRecordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transfusion_request_id", nullable = false)
    private TransfusionRequest transfusionRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "administering_doctor_id")
    private Doctor administeringDoctor; // 수혈 시행 의사

    @Column(nullable = false, length = 50)
    private String bloodComponent; // 전혈, 적혈구, 혈소판, 혈장 등

    @Column(nullable = false, length = 50)
    private String bloodType; // 혈액형 (예: A+, B-)

    @Column(nullable = false, length = 100)
    private String bloodBagNumber; // 혈액팩 번호

    @Column(nullable = false)
    private Integer units; // 수혈 단위

    @Column(nullable = false)
    private LocalDateTime startTime; // 수혈 시작 시간

    @Column
    private LocalDateTime endTime; // 수혈 종료 시간

    @Column(length = 50)
    private String status; // IN_PROGRESS, COMPLETED, STOPPED

    @Column(columnDefinition = "TEXT")
    private String notes; // 비고

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private TransfusionRecord(TransfusionRequest transfusionRequest, Patient patient, Doctor administeringDoctor,
                             String bloodComponent, String bloodType, String bloodBagNumber, Integer units,
                             LocalDateTime startTime, String status, String notes) {
        this.transfusionRequest = transfusionRequest;
        this.patient = patient;
        this.administeringDoctor = administeringDoctor;
        this.bloodComponent = bloodComponent;
        this.bloodType = bloodType;
        this.bloodBagNumber = bloodBagNumber;
        this.units = units;
        this.startTime = startTime;
        this.status = status;
        this.notes = notes;
    }

    public static TransfusionRecord create(TransfusionRequest transfusionRequest, Patient patient,
                                          Doctor administeringDoctor, String bloodComponent, String bloodType,
                                          String bloodBagNumber, Integer units) {
        return new TransfusionRecord(transfusionRequest, patient, administeringDoctor, bloodComponent, bloodType,
                bloodBagNumber, units, LocalDateTime.now(), "IN_PROGRESS", null);
    }

    public void complete(LocalDateTime endTime) {
        this.endTime = endTime;
        this.status = "COMPLETED";
    }

    public void stop(String reason) {
        this.status = "STOPPED";
        this.notes = reason;
    }

    public void updateNotes(String notes) {
        this.notes = notes;
    }

    public Long getId() {
        return this.transfusionRecordId;
    }
}

