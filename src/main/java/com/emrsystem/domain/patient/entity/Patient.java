package com.emrsystem.domain.patient.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TBL_PATIENT")
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE patient SET deleted = true, updated_at = NOW() WHERE id = ?")
@Where(clause = "deleted = false")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Long patientId;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false, length = 10)
    private String gender; // M/F 등 코드값으로 관리 예정

    @Column(nullable = false, length = 30)
    private String phone;

    @Column(nullable = false, length = 300)
    private String address;

    @Column(nullable = false, length = 100, unique = true)
    @Convert(converter = com.emrsystem.core.security.crypto.EncryptedStringConverter.class)
    private String identifier; // 환자식별자(예: 병원 MRN)

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

    @Column(nullable = false)
    private boolean deleted = false;

    private Patient(String name, LocalDate birthDate, String gender, String phone, String address, String identifier,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.identifier = identifier;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Patient create(String name, LocalDate birthDate, String gender, String phone, String address, String identifier) {
        LocalDateTime now = LocalDateTime.now();
        return new Patient(name, birthDate, gender, phone, address, identifier, now, now);
    }

    public void update(String name, LocalDate birthDate, String gender, String phone, String address) {
        this.name = name;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
    }

    // Compatibility getter
    public Long getId() { return this.patientId; }
}


