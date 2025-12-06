package com.emrsystem.domain.patient.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "patient_allergy")
@EntityListeners(AuditingEntityListener.class)
public class PatientAllergy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "allergy_id")
    private Long allergyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(nullable = false, length = 50)
    private String allergenType; // DRUG, FOOD, ENVIRONMENTAL, OTHER

    @Column(nullable = false, length = 200)
    private String allergenName; // 알레르기 유발 물질명

    @Column(nullable = false, length = 50)
    private String severity; // MILD, MODERATE, SEVERE, LIFE_THREATENING

    @Column(columnDefinition = "TEXT")
    private String reaction; // 반응 증상

    @Column
    private LocalDate diagnosedDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false)
    private boolean active = true; // 활성 여부

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(nullable = false, updatable = false, length = 100)
    private String createdBy;

    @LastModifiedBy
    @Column(nullable = false, length = 100)
    private String updatedBy;

    private PatientAllergy(Patient patient, String allergenType, String allergenName, String severity,
                          String reaction, LocalDate diagnosedDate, String notes, boolean active,
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.patient = patient;
        this.allergenType = allergenType;
        this.allergenName = allergenName;
        this.severity = severity;
        this.reaction = reaction;
        this.diagnosedDate = diagnosedDate;
        this.notes = notes;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static PatientAllergy create(Patient patient, String allergenType, String allergenName, String severity,
                                       String reaction, LocalDate diagnosedDate, String notes) {
        LocalDateTime now = LocalDateTime.now();
        return new PatientAllergy(patient, allergenType, allergenName, severity, reaction, diagnosedDate, notes, true, now, now);
    }

    public void update(String allergenType, String allergenName, String severity, String reaction,
                      LocalDate diagnosedDate, String notes) {
        this.allergenType = allergenType;
        this.allergenName = allergenName;
        this.severity = severity;
        this.reaction = reaction;
        this.diagnosedDate = diagnosedDate;
        this.notes = notes;
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    public boolean isSevere() {
        return "SEVERE".equals(severity) || "LIFE_THREATENING".equals(severity);
    }

    public Long getId() {
        return this.allergyId;
    }
}

