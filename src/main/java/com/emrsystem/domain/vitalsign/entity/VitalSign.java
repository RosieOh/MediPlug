package com.emrsystem.domain.vitalsign.entity;

import com.emrsystem.domain.patient.entity.Patient;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "vital_sign")
@EntityListeners(AuditingEntityListener.class)
public class VitalSign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vital_sign_id")
    private Long vitalSignId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private LocalDateTime measuredAt; // 측정 일시

    @Column(precision = 5, scale = 2)
    private BigDecimal systolicBP; // 수축기 혈압

    @Column(precision = 5, scale = 2)
    private BigDecimal diastolicBP; // 이완기 혈압

    @Column(precision = 5, scale = 2)
    private BigDecimal heartRate; // 맥박 (bpm)

    @Column(precision = 5, scale = 2)
    private BigDecimal temperature; // 체온 (°C)

    @Column(precision = 5, scale = 2)
    private BigDecimal respiratoryRate; // 호흡수 (회/분)

    @Column(precision = 5, scale = 2)
    private BigDecimal oxygenSaturation; // 산소포화도 (%)

    @Column(precision = 5, scale = 2)
    private BigDecimal bloodSugar; // 혈당 (mg/dL)

    @Column(precision = 5, scale = 2)
    private BigDecimal painScore; // 통증 점수 (0-10)

    @Column(length = 50)
    private String measuredBy; // 측정한 사람

    @Column(length = 100)
    private String device; // 측정 기기

    @Column(columnDefinition = "TEXT")
    private String notes; // 비고

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private VitalSign(Patient patient, LocalDateTime measuredAt, BigDecimal systolicBP, BigDecimal diastolicBP,
                     BigDecimal heartRate, BigDecimal temperature, BigDecimal respiratoryRate,
                     BigDecimal oxygenSaturation, BigDecimal bloodSugar, BigDecimal painScore,
                     String measuredBy, String device, String notes) {
        this.patient = patient;
        this.measuredAt = measuredAt;
        this.systolicBP = systolicBP;
        this.diastolicBP = diastolicBP;
        this.heartRate = heartRate;
        this.temperature = temperature;
        this.respiratoryRate = respiratoryRate;
        this.oxygenSaturation = oxygenSaturation;
        this.bloodSugar = bloodSugar;
        this.painScore = painScore;
        this.measuredBy = measuredBy;
        this.device = device;
        this.notes = notes;
    }

    public static VitalSign create(Patient patient, LocalDateTime measuredAt, BigDecimal systolicBP,
                                  BigDecimal diastolicBP, BigDecimal heartRate, BigDecimal temperature,
                                  BigDecimal respiratoryRate, BigDecimal oxygenSaturation, BigDecimal bloodSugar,
                                  BigDecimal painScore, String measuredBy, String device, String notes) {
        return new VitalSign(patient, measuredAt, systolicBP, diastolicBP, heartRate, temperature,
                respiratoryRate, oxygenSaturation, bloodSugar, painScore, measuredBy, device, notes);
    }

    public void updateNotes(String notes) {
        this.notes = notes;
    }

    public Long getId() {
        return this.vitalSignId;
    }
}

