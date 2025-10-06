package com.emrsystem.domain.billing.entity;

import com.emrsystem.domain.patient.entity.Patient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "receipt")
@EntityListeners(AuditingEntityListener.class)
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receipt_id")
    private Long receiptId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(nullable = false, length = 50, unique = true)
    private String receiptNumber; // 영수증번호

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount; // 총 금액

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal insuranceAmount; // 보험금액

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal copayAmount; // 본인부담금

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal paidAmount; // 납부금액

    @Column(nullable = false, length = 50)
    private String paymentMethod; // CASH, CARD, INSURANCE, MIXED

    @Column(nullable = false, length = 50)
    private String status; // ISSUED, CANCELLED, REPRINTED

    @Column(nullable = false)
    private LocalDateTime issuedAt; // 발행일시

    @Column
    private LocalDateTime cancelledAt; // 취소일시

    @Column(length = 500)
    private String notes; // 특이사항

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private Receipt(Patient patient, String receiptNumber, BigDecimal totalAmount, BigDecimal insuranceAmount,
                   BigDecimal copayAmount, BigDecimal paidAmount, String paymentMethod, String status) {
        this.patient = patient;
        this.receiptNumber = receiptNumber;
        this.totalAmount = totalAmount;
        this.insuranceAmount = insuranceAmount;
        this.copayAmount = copayAmount;
        this.paidAmount = paidAmount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.issuedAt = LocalDateTime.now();
    }

    public static Receipt create(Patient patient, String receiptNumber, BigDecimal totalAmount, BigDecimal insuranceAmount,
                               BigDecimal copayAmount, BigDecimal paidAmount, String paymentMethod) {
        return new Receipt(patient, receiptNumber, totalAmount, insuranceAmount, copayAmount, paidAmount, paymentMethod, "ISSUED");
    }

    public void cancel() {
        this.status = "CANCELLED";
        this.cancelledAt = LocalDateTime.now();
    }

    public void reprint() {
        this.status = "REPRINTED";
    }

    public Long getId() { return this.receiptId; }
}
