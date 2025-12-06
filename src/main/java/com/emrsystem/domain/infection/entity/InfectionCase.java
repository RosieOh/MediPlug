package com.emrsystem.domain.infection.entity;

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
@Table(name = "infection_case")
@EntityListeners(AuditingEntityListener.class)
public class InfectionCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "infection_case_id")
    private Long infectionCaseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(nullable = false, length = 100)
    private String infectionType; // 감염 유형 (MRSA, VRE, C.diff, COVID-19 등)

    @Column(nullable = false, length = 200)
    private String infectionName; // 감염명

    @Column(nullable = false)
    private LocalDate diagnosedDate; // 진단일

    @Column
    private LocalDate resolvedDate; // 해소일

    @Column(nullable = false, length = 50)
    private String status; // ACTIVE, RESOLVED, MONITORING

    @Column(nullable = false, length = 50)
    private String severity; // MILD, MODERATE, SEVERE, CRITICAL

    @Column(length = 100)
    private String isolationRoom; // 격리실

    @Column(columnDefinition = "TEXT")
    private String symptoms; // 증상

    @Column(columnDefinition = "TEXT")
    private String treatment; // 치료 방법

    @Column(columnDefinition = "TEXT")
    private String preventionMeasures; // 예방 조치

    @Column(columnDefinition = "TEXT")
    private String notes; // 비고

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private InfectionCase(Patient patient, String infectionType, String infectionName, LocalDate diagnosedDate,
                         String status, String severity, String isolationRoom, String symptoms, String treatment,
                         String preventionMeasures, String notes) {
        this.patient = patient;
        this.infectionType = infectionType;
        this.infectionName = infectionName;
        this.diagnosedDate = diagnosedDate;
        this.status = status;
        this.severity = severity;
        this.isolationRoom = isolationRoom;
        this.symptoms = symptoms;
        this.treatment = treatment;
        this.preventionMeasures = preventionMeasures;
        this.notes = notes;
    }

    public static InfectionCase create(Patient patient, String infectionType, String infectionName,
                                      LocalDate diagnosedDate, String severity, String isolationRoom,
                                      String symptoms, String treatment, String preventionMeasures) {
        return new InfectionCase(patient, infectionType, infectionName, diagnosedDate, "ACTIVE", severity,
                isolationRoom, symptoms, treatment, preventionMeasures, null);
    }

    public void resolve(LocalDate resolvedDate) {
        this.resolvedDate = resolvedDate;
        this.status = "RESOLVED";
    }

    public void setMonitoring() {
        this.status = "MONITORING";
    }

    public void update(String infectionType, String infectionName, String severity, String isolationRoom,
                      String symptoms, String treatment, String preventionMeasures, String notes) {
        this.infectionType = infectionType;
        this.infectionName = infectionName;
        this.severity = severity;
        this.isolationRoom = isolationRoom;
        this.symptoms = symptoms;
        this.treatment = treatment;
        this.preventionMeasures = preventionMeasures;
        this.notes = notes;
    }

    public Long getId() {
        return this.infectionCaseId;
    }
}

