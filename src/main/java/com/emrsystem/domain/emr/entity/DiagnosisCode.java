package com.emrsystem.domain.emr.entity;

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
@Table(name = "diagnosis_code")
@EntityListeners(AuditingEntityListener.class)
public class DiagnosisCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diagnosis_code_id")
    private Long diagnosisCodeId;

    @Column(nullable = false, unique = true, length = 20)
    private String code; // ICD-10 코드 (예: J44.0)

    @Column(nullable = false, length = 500)
    private String name; // 진단명

    @Column(length = 100)
    private String category; // 카테고리

    @Column(columnDefinition = "TEXT")
    private String description; // 설명

    @Column(nullable = false)
    private boolean active = true; // 활성 여부

    @Column(nullable = false)
    private int version = 1; // 버전 (ICD-10, ICD-11 등)

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private DiagnosisCode(String code, String name, String category, String description, boolean active, int version,
                         LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.code = code;
        this.name = name;
        this.category = category;
        this.description = description;
        this.active = active;
        this.version = version;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static DiagnosisCode create(String code, String name, String category, String description, int version) {
        LocalDateTime now = LocalDateTime.now();
        return new DiagnosisCode(code, name, category, description, true, version, now, now);
    }

    public void update(String name, String category, String description) {
        this.name = name;
        this.category = category;
        this.description = description;
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    public Long getId() {
        return this.diagnosisCodeId;
    }
}

