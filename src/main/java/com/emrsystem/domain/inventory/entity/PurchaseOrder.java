package com.emrsystem.domain.inventory.entity;

import com.emrsystem.domain.pharmacy.entity.DrugMaster;
import jakarta.persistence.*;
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
@Table(name = "purchase_order")
@EntityListeners(AuditingEntityListener.class)
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(nullable = false, unique = true, length = 50)
    private String orderNumber; // 주문 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drug_master_id", nullable = false)
    private DrugMaster drugMaster;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(nullable = false)
    private int quantity; // 주문 수량

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice; // 단가

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount; // 총 금액

    @Column(nullable = false, length = 50)
    private String status; // PENDING, APPROVED, ORDERED, RECEIVED, CANCELLED

    @Column
    private LocalDate expectedDeliveryDate; // 예상 납품일

    @Column
    private LocalDate actualDeliveryDate; // 실제 납품일

    @Column(length = 200)
    private String orderedBy; // 주문한 사람

    @Column(length = 200)
    private String approvedBy; // 승인한 사람

    @Column
    private LocalDateTime orderedAt; // 주문 일시

    @Column
    private LocalDateTime approvedAt; // 승인 일시

    @Column(columnDefinition = "TEXT")
    private String notes; // 비고

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private PurchaseOrder(String orderNumber, DrugMaster drugMaster, Supplier supplier, int quantity,
                         BigDecimal unitPrice, BigDecimal totalAmount, String status, String orderedBy) {
        this.orderNumber = orderNumber;
        this.drugMaster = drugMaster;
        this.supplier = supplier;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderedBy = orderedBy;
    }

    public static PurchaseOrder create(String orderNumber, DrugMaster drugMaster, Supplier supplier,
                                      int quantity, BigDecimal unitPrice, String orderedBy) {
        BigDecimal totalAmount = unitPrice.multiply(BigDecimal.valueOf(quantity));
        return new PurchaseOrder(orderNumber, drugMaster, supplier, quantity, unitPrice, totalAmount, "PENDING", orderedBy);
    }

    public void approve(String approvedBy) {
        this.status = "APPROVED";
        this.approvedBy = approvedBy;
        this.approvedAt = LocalDateTime.now();
    }

    public void order() {
        this.status = "ORDERED";
        this.orderedAt = LocalDateTime.now();
    }

    public void receive(LocalDate deliveryDate) {
        this.status = "RECEIVED";
        this.actualDeliveryDate = deliveryDate;
    }

    public void cancel() {
        this.status = "CANCELLED";
    }

    public void updateExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public void updateNotes(String notes) {
        this.notes = notes;
    }

    public Long getId() {
        return this.orderId;
    }
}

