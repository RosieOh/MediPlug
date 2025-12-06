package com.emrsystem.domain.billing.response;

import com.emrsystem.domain.billing.entity.Billing;
import com.emrsystem.domain.billing.entity.ProcedureCode;
import com.emrsystem.domain.billing.entity.Receipt;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BillingResponses {

    @Getter
    @NoArgsConstructor
    public static class ProcedureCodeSummary {
        private Long procedureCodeId;
        private String code;
        private String name;
        private BigDecimal fee;
        private String category;
        private String description;
        private boolean active;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static ProcedureCodeSummary of(ProcedureCode procedureCode) {
            ProcedureCodeSummary summary = new ProcedureCodeSummary();
            summary.procedureCodeId = procedureCode.getProcedureCodeId();
            summary.code = procedureCode.getCode();
            summary.name = procedureCode.getName();
            summary.fee = procedureCode.getFee();
            summary.category = procedureCode.getCategory();
            summary.description = procedureCode.getDescription();
            summary.active = procedureCode.isActive();
            summary.createdAt = procedureCode.getCreatedAt();
            summary.updatedAt = procedureCode.getUpdatedAt();
            return summary;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class BillingSummary {
        private Long billingId;
        private Long patientId;
        private String patientName;
        private Long appointmentId;
        private LocalDateTime billingDate;
        private BigDecimal totalAmount;
        private BigDecimal insuranceAmount;
        private BigDecimal patientAmount;
        private String billingType;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static BillingSummary of(Billing billing) {
            BillingSummary summary = new BillingSummary();
            summary.billingId = billing.getBillingId();
            summary.patientId = billing.getPatient().getPatientId();
            summary.patientName = billing.getPatient().getName();
            summary.appointmentId = billing.getAppointment() != null ? billing.getAppointment().getAppointmentId() : null;
            summary.billingDate = billing.getBillingDate();
            summary.totalAmount = billing.getTotalAmount();
            summary.insuranceAmount = billing.getInsuranceAmount();
            summary.patientAmount = billing.getPatientAmount();
            summary.billingType = billing.getBillingType();
            summary.status = billing.getStatus();
            summary.createdAt = billing.getCreatedAt();
            summary.updatedAt = billing.getUpdatedAt();
            return summary;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ReceiptSummary {
        private Long receiptId;
        private Long billingId;
        private LocalDateTime receiptDate;
        private BigDecimal amountPaid;
        private String paymentMethod;
        private String paymentReference;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static ReceiptSummary of(Receipt receipt) {
            ReceiptSummary summary = new ReceiptSummary();
            summary.receiptId = receipt.getReceiptId();
            summary.billingId = receipt.getBilling().getBillingId();
            summary.receiptDate = receipt.getReceiptDate();
            summary.amountPaid = receipt.getAmountPaid();
            summary.paymentMethod = receipt.getPaymentMethod();
            summary.paymentReference = receipt.getPaymentReference();
            summary.status = receipt.getStatus();
            summary.createdAt = receipt.getCreatedAt();
            summary.updatedAt = receipt.getUpdatedAt();
            return summary;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class BillingCalculationResult {
        private BigDecimal totalAmount;
        private BigDecimal insuranceAmount;
        private BigDecimal patientAmount;
        private String calculationDetails;

        public BillingCalculationResult(BigDecimal totalAmount, BigDecimal insuranceAmount, 
                BigDecimal patientAmount, String calculationDetails) {
            this.totalAmount = totalAmount;
            this.insuranceAmount = insuranceAmount;
            this.patientAmount = patientAmount;
            this.calculationDetails = calculationDetails;
        }
    }
}
