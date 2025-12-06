package com.emrsystem.domain.billing.entity;

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
@Table(name = "procedure_code")
@EntityListeners(AuditingEntityListener.class)
public class ProcedureCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "procedure_code_id")
    private Long procedureCodeId;

    @Column(nullable = false, length = 20, unique = true)
    private String code; // 행위코드

    @Column(nullable = false, length = 200)
    private String name; // 행위명

    @Column(nullable = false, length = 100)
    private String category; // 진료과목

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice; // 단가

    @Column(nullable = false, length = 50)
    private String unit; // 단위 (회, 일, 월 등)

    @Column(length = 500)
    private String description; // 설명

    @Column(nullable = false)
    private boolean active = true; // 활성화 여부

    @Column(nullable = false)
    private int version = 1; // 버전

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private ProcedureCode(String code, String name, String category, BigDecimal unitPrice, String unit, String description) {
        this.code = code;
        this.name = name;
        this.category = category;
        this.unitPrice = unitPrice;
        this.unit = unit;
        this.description = description;
    }

    public static ProcedureCode create(String code, String name, String category, BigDecimal unitPrice, String unit, String description) {
        return new ProcedureCode(code, name, category, unitPrice, unit, description);
    }

    public void update(String name, String category, BigDecimal unitPrice, String unit, String description) {
        this.name = name;
        this.category = category;
        this.unitPrice = unitPrice;
        this.unit = unit;
        this.description = description;
        this.version++;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public Long getId() { return this.procedureCodeId; }

    // Compatibility getter
    public java.math.BigDecimal getFee() { return this.unitPrice; }
}
