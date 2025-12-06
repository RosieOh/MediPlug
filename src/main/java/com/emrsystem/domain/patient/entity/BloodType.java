package com.emrsystem.domain.patient.entity;

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
@Table(name = "blood_type")
@EntityListeners(AuditingEntityListener.class)
public class BloodType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blood_type_id")
    private Long bloodTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false, unique = true)
    private Patient patient;

    @Column(nullable = false, length = 10)
    private String aboType; // A, B, AB, O

    @Column(nullable = false, length = 10)
    private String rhType; // +, -

    @Column(length = 200)
    private String testedAt; // 검사 기관

    @Column
    private java.time.LocalDate testedDate; // 검사일

    @Column(columnDefinition = "TEXT")
    private String notes; // 비고

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private BloodType(Patient patient, String aboType, String rhType, String testedAt,
                     java.time.LocalDate testedDate, String notes) {
        this.patient = patient;
        this.aboType = aboType;
        this.rhType = rhType;
        this.testedAt = testedAt;
        this.testedDate = testedDate;
        this.notes = notes;
    }

    public static BloodType create(Patient patient, String aboType, String rhType, String testedAt,
                                   java.time.LocalDate testedDate, String notes) {
        return new BloodType(patient, aboType, rhType, testedAt, testedDate, notes);
    }

    public void update(String aboType, String rhType, String testedAt, java.time.LocalDate testedDate, String notes) {
        this.aboType = aboType;
        this.rhType = rhType;
        this.testedAt = testedAt;
        this.testedDate = testedDate;
        this.notes = notes;
    }

    public String getFullBloodType() {
        return aboType + rhType;
    }

    public Long getId() {
        return this.bloodTypeId;
    }
}

