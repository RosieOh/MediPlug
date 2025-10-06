package com.emrsystem.domain.billing.entity;

import com.emrsystem.domain.appointment.entity.Appointment;
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
@Table(name = "billing")
@EntityListeners(AuditingEntityListener.class)
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "billing_id")
    private Long billingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "procedure_code_id", nullable = false)
    private ProcedureCode procedureCode;

    @Column(nullable = false, length = 50)
    private String billingNumber; // 청구번호

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount; // 총 금액

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal insuranceAmount; // 보험금액

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal copayAmount; // 본인부담금

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal paidAmount; // 납부금액

    @Column(nullable = false, length = 50)
    private String status; // PENDING, PAID, CANCELLED, REFUNDED

    @Column(nullable = false, length = 50)
    private String paymentMethod; // CASH, CARD, INSURANCE, MIXED

    @Column
    private LocalDateTime paidAt; // 납부일시

    @Column(length = 500)
    private String notes; // 특이사항

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private Billing(Appointment appointment, Patient patient, ProcedureCode procedureCode, String billingNumber,
                   BigDecimal totalAmount, BigDecimal insuranceAmount, BigDecimal copayAmount, String status, String paymentMethod) {
        this.appointment = appointment;
        this.patient = patient;
        this.procedureCode = procedureCode;
        this.billingNumber = billingNumber;
        this.totalAmount = totalAmount;
        this.insuranceAmount = insuranceAmount;
        this.copayAmount = copayAmount;
        this.paidAmount = BigDecimal.ZERO;
        this.status = status;
        this.paymentMethod = paymentMethod;
    }

    public static Billing create(Appointment appointment, Patient patient, ProcedureCode procedureCode, String billingNumber,
                                BigDecimal totalAmount, BigDecimal insuranceAmount, BigDecimal copayAmount, String paymentMethod) {
        return new Billing(appointment, patient, procedureCode, billingNumber, totalAmount, insuranceAmount, copayAmount, "PENDING", paymentMethod);
    }

    public void pay(BigDecimal paidAmount, String paymentMethod) {
        this.paidAmount = paidAmount;
        this.paymentMethod = paymentMethod;
        this.status = "PAID";
        this.paidAt = LocalDateTime.now();
    }

    public void cancel() {
        this.status = "CANCELLED";
    }

    public void refund() {
        this.status = "REFUNDED";
    }

    public BigDecimal getRemainingAmount() {
        return this.totalAmount.subtract(this.paidAmount);
    }

    public boolean isFullyPaid() {
        return this.paidAmount.compareTo(this.totalAmount) >= 0;
    }

    public Long getId() { return this.billingId; }
}
