package com.emrsystem.domain.pharmacy.entity;

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
@Table(name = "drug_master")
@EntityListeners(AuditingEntityListener.class)
public class DrugMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drug_master_id")
    private Long drugMasterId;

    @Column(nullable = false, length = 50, unique = true)
    private String drugCode; // 약물코드

    @Column(nullable = false, length = 200)
    private String drugName; // 약물명

    @Column(nullable = false, length = 100)
    private String genericName; // 일반명

    @Column(nullable = false, length = 50)
    private String manufacturer; // 제조사

    @Column(nullable = false, length = 50)
    private String category; // 약물분류

    @Column(nullable = false, length = 50)
    private String dosageForm; // 제형 (정, 캡슐, 시럽 등)

    @Column(nullable = false, length = 50)
    private String strength; // 함량

    @Column(nullable = false, length = 50)
    private String unit; // 단위

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice; // 단가

    @Column(nullable = false)
    private int shelfLife; // 유통기한 (일)

    @Column(nullable = false)
    private boolean prescriptionRequired = true; // 처방전 필요 여부

    @Column(nullable = false)
    private boolean controlled = false; // 마약류 여부

    @Column(columnDefinition = "TEXT")
    private String sideEffects; // 부작용

    @Column(columnDefinition = "TEXT")
    private String contraindications; // 금기사항

    @Column(columnDefinition = "TEXT")
    private String storageConditions; // 보관조건

    @Column(nullable = false)
    private boolean active = true; // 활성화 여부

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private DrugMaster(String drugCode, String drugName, String genericName, String manufacturer, String category,
                      String dosageForm, String strength, String unit, BigDecimal unitPrice, int shelfLife,
                      boolean prescriptionRequired, boolean controlled, String sideEffects, String contraindications,
                      String storageConditions) {
        this.drugCode = drugCode;
        this.drugName = drugName;
        this.genericName = genericName;
        this.manufacturer = manufacturer;
        this.category = category;
        this.dosageForm = dosageForm;
        this.strength = strength;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.shelfLife = shelfLife;
        this.prescriptionRequired = prescriptionRequired;
        this.controlled = controlled;
        this.sideEffects = sideEffects;
        this.contraindications = contraindications;
        this.storageConditions = storageConditions;
    }

    public static DrugMaster create(String drugCode, String drugName, String genericName,
                                    String manufacturer, String category, String dosageForm, String strength,
                                    String unit, BigDecimal unitPrice, int shelfLife, boolean prescriptionRequired,
                                    boolean controlled, String sideEffects, String contraindications, String storageConditions) {
        return new DrugMaster(drugCode, drugName, genericName, manufacturer, category, dosageForm, strength, unit,
                unitPrice, shelfLife, prescriptionRequired, controlled, sideEffects, contraindications, storageConditions);
    }

    public void update(String drugName, String genericName, String manufacturer, String category, String dosageForm,
                       String strength, String unit, BigDecimal unitPrice, int shelfLife, boolean prescriptionRequired,
                       boolean controlled, String sideEffects, String contraindications, String storageConditions) {
        this.drugName = drugName;
        this.genericName = genericName;
        this.manufacturer = manufacturer;
        this.category = category;
        this.dosageForm = dosageForm;
        this.strength = strength;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.shelfLife = shelfLife;
        this.prescriptionRequired = prescriptionRequired;
        this.controlled = controlled;
        this.sideEffects = sideEffects;
        this.contraindications = contraindications;
        this.storageConditions = storageConditions;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public Long getId() { return this.drugMasterId; }

    // Compatibility getters for services expecting code/name
    public String getCode() { return this.drugCode; }
    public String getName() { return this.drugName; }

    // Compatibility factory matching service usage
    public static DrugMaster create(String code, String name, String manufacturer, BigDecimal unitPrice, String description) {
        // Map minimal fields; leave optional fields with defaults
        return new DrugMaster(code, name, name, manufacturer, null, null, null, null, unitPrice, 0, false, false, description, null, null);
    }

    public void update(String name, String manufacturer, BigDecimal unitPrice, String description) {
        this.drugName = name;
        this.manufacturer = manufacturer;
        this.unitPrice = unitPrice;
        this.sideEffects = description;
    }
}
