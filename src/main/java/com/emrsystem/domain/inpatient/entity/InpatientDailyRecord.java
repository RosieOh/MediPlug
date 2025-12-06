package com.emrsystem.domain.inpatient.entity;

import com.emrsystem.domain.bed.entity.Admission;
import com.emrsystem.domain.doctor.entity.Doctor;
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
@Table(name = "inpatient_daily_record")
@EntityListeners(AuditingEntityListener.class)
public class InpatientDailyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_record_id")
    private Long dailyRecordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admission_id", nullable = false)
    private Admission admission;

    @Column(nullable = false)
    private LocalDate recordDate; // 기록일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor; // 담당 의사

    @Column(name = "patient_condition", columnDefinition = "TEXT")
    private String patientCondition; // 환자 상태

    @Column(columnDefinition = "TEXT")
    private String treatment; // 치료 내용

    @Column(columnDefinition = "TEXT")
    private String plan; // 치료 계획

    @Column(columnDefinition = "TEXT")
    private String notes; // 비고

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private InpatientDailyRecord(Admission admission, LocalDate recordDate, Doctor doctor, String patientCondition,
                                 String treatment, String plan, String notes) {
        this.admission = admission;
        this.recordDate = recordDate;
        this.doctor = doctor;
        this.patientCondition = patientCondition;
        this.treatment = treatment;
        this.plan = plan;
        this.notes = notes;
    }

    public static InpatientDailyRecord create(Admission admission, LocalDate recordDate, Doctor doctor,
                                              String patientCondition, String treatment, String plan, String notes) {
        return new InpatientDailyRecord(admission, recordDate, doctor, patientCondition, treatment, plan, notes);
    }

    public void update(String patientCondition, String treatment, String plan, String notes) {
        this.patientCondition = patientCondition;
        this.treatment = treatment;
        this.plan = plan;
        this.notes = notes;
    }

    // Compatibility getter
    public String getCondition() {
        return this.patientCondition;
    }

    public Long getId() {
        return this.dailyRecordId;
    }
}

