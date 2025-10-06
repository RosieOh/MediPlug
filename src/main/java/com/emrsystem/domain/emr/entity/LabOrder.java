package com.emrsystem.domain.emr.entity;

import com.emrsystem.domain.appointment.entity.Appointment;
import com.emrsystem.domain.doctor.entity.Doctor;
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
@Table(name = "lab_order")
@EntityListeners(AuditingEntityListener.class)
public class LabOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lab_order_id")
    private Long labOrderId;

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
    private String testName; // 검사명

    @Column(nullable = false, length = 50)
    private String testCode; // 검사코드

    @Column(nullable = false, length = 50)
    private String category; // 혈액, 소변, 영상 등

    @Column(columnDefinition = "TEXT")
    private String clinicalInfo; // 임상정보

    @Column(columnDefinition = "TEXT")
    private String instructions; // 검사 지시사항

    @Column(nullable = false, length = 50)
    private String priority; // ROUTINE, URGENT, STAT

    @Column(nullable = false, length = 50)
    private String status; // ORDERED, COLLECTED, IN_PROGRESS, COMPLETED, CANCELLED

    @Column
    private LocalDateTime scheduledAt; // 검사 예정일시

    @Column
    private LocalDateTime collectedAt; // 검체 채취일시

    @Column
    private LocalDateTime completedAt; // 검사 완료일시

    @Column(columnDefinition = "TEXT")
    private String result; // 검사 결과

    @Column(columnDefinition = "TEXT")
    private String interpretation; // 결과 해석

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private LabOrder(Appointment appointment, Patient patient, Doctor doctor, String testName, String testCode,
                    String category, String clinicalInfo, String instructions, String priority, String status) {
        this.appointment = appointment;
        this.patient = patient;
        this.doctor = doctor;
        this.testName = testName;
        this.testCode = testCode;
        this.category = category;
        this.clinicalInfo = clinicalInfo;
        this.instructions = instructions;
        this.priority = priority;
        this.status = status;
    }

    public static LabOrder create(Appointment appointment, Patient patient, Doctor doctor, String testName,
                                 String testCode, String category, String clinicalInfo, String instructions, String priority) {
        return new LabOrder(appointment, patient, doctor, testName, testCode, category, clinicalInfo, instructions, priority, "ORDERED");
    }

    public void collect(LocalDateTime collectedAt) {
        this.status = "COLLECTED";
        this.collectedAt = collectedAt;
    }

    public void startProgress() {
        this.status = "IN_PROGRESS";
    }

    public void complete(String result, String interpretation) {
        this.status = "COMPLETED";
        this.result = result;
        this.interpretation = interpretation;
        this.completedAt = LocalDateTime.now();
    }

    public void cancel() {
        this.status = "CANCELLED";
    }

    public void schedule(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public Long getId() { return this.labOrderId; }
}
