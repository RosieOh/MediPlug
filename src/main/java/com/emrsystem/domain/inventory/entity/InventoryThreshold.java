package com.emrsystem.domain.inventory.entity;

import com.emrsystem.domain.pharmacy.entity.DrugMaster;
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
@Table(name = "inventory_threshold")
@EntityListeners(AuditingEntityListener.class)
public class InventoryThreshold {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "threshold_id")
    private Long thresholdId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drug_master_id", nullable = false)
    private DrugMaster drugMaster;

    @Column(nullable = false)
    private int minimumQuantity; // 최소 재고량

    @Column(nullable = false)
    private int reorderQuantity; // 재주문 수량

    @Column(nullable = false)
    private boolean autoReorderEnabled; // 자동 주문 활성화 여부

    @Column(length = 200)
    private String notes; // 비고

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private InventoryThreshold(DrugMaster drugMaster, int minimumQuantity, int reorderQuantity,
                              boolean autoReorderEnabled, String notes) {
        this.drugMaster = drugMaster;
        this.minimumQuantity = minimumQuantity;
        this.reorderQuantity = reorderQuantity;
        this.autoReorderEnabled = autoReorderEnabled;
        this.notes = notes;
    }

    public static InventoryThreshold create(DrugMaster drugMaster, int minimumQuantity, int reorderQuantity) {
        return new InventoryThreshold(drugMaster, minimumQuantity, reorderQuantity, true, null);
    }

    public void update(int minimumQuantity, int reorderQuantity, boolean autoReorderEnabled, String notes) {
        this.minimumQuantity = minimumQuantity;
        this.reorderQuantity = reorderQuantity;
        this.autoReorderEnabled = autoReorderEnabled;
        this.notes = notes;
    }

    public void enableAutoReorder() {
        this.autoReorderEnabled = true;
    }

    public void disableAutoReorder() {
        this.autoReorderEnabled = false;
    }

    public boolean isBelowThreshold(int currentQuantity) {
        return currentQuantity <= minimumQuantity;
    }

    public Long getId() {
        return this.thresholdId;
    }
}

