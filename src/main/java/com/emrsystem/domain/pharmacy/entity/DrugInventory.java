package com.emrsystem.domain.pharmacy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "drug_inventory")
@EntityListeners(AuditingEntityListener.class)
public class DrugInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drug_inventory_id")
    private Long drugInventoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drug_master_id", nullable = false)
    private DrugMaster drugMaster;

    @Column(nullable = false, length = 50)
    private String batchNumber; // 배치(LOT) 번호

    @Column(nullable = false)
    private int quantity; // 수량

    @Column(nullable = false)
    private int availableQuantity; // 가용 수량

    @Column(nullable = false)
    private LocalDate expirationDate; // 유통기한

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitCost; // 단위 원가

    @Column(length = 100)
    private String location; // 보관 위치

    @Column(nullable = false, length = 50)
    private String status; // AVAILABLE, EXPIRED, RECALLED, QUARANTINE

    @Column(length = 500)
    private String notes; // 특이사항

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private DrugInventory(DrugMaster drugMaster, String batchNumber, int quantity, LocalDate expirationDate,
                         BigDecimal unitCost, String status, String location, String notes) {
        this.drugMaster = drugMaster;
        this.batchNumber = batchNumber;
        this.quantity = quantity;
        this.availableQuantity = quantity;
        this.expirationDate = expirationDate;
        this.unitCost = unitCost;
        this.status = status;
        this.location = location;
        this.notes = notes;
    }

    public static DrugInventory create(DrugMaster drugMaster,
                                       String batchNumber,
                                       @NotNull(message = "수량은 필수입니다.") Integer quantity,
                                       @NotNull(message = "유통기한은 필수입니다.") LocalDate expirationDate,
                                       @NotNull BigDecimal unitCost) {
        return new DrugInventory(drugMaster, batchNumber, quantity, expirationDate, unitCost, "AVAILABLE", null, null);
    }

    public void adjustQuantity(int adjustment) {
        this.quantity += adjustment;
        this.availableQuantity += adjustment;
    }

    public void reserve(int quantity) {
        if (this.availableQuantity < quantity) {
            throw new IllegalStateException("Insufficient available quantity");
        }
        this.availableQuantity -= quantity;
    }

    public void release(int quantity) {
        this.availableQuantity += quantity;
    }

    public void expire() {
        this.status = "EXPIRED";
    }

    public void recall() {
        this.status = "RECALLED";
    }

    public void quarantine() {
        this.status = "QUARANTINE";
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(this.expirationDate);
    }

    public boolean isExpiringSoon(int days) {
        return LocalDate.now().plusDays(days).isAfter(this.expirationDate);
    }

    public Long getId() { return this.drugInventoryId; }

    // Compatibility getter for legacy code
    public String getLotNumber() { return this.batchNumber; }
}
