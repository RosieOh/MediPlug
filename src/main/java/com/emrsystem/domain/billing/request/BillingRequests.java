package com.emrsystem.domain.billing.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BillingRequests {

    @Getter
    @NoArgsConstructor
    public static class CreateProcedureCodeRequest {
        @NotBlank(message = "행위코드는 필수입니다.")
        @Size(max = 20, message = "행위코드는 20자를 초과할 수 없습니다.")
        private String code;

        @NotBlank(message = "행위명은 필수입니다.")
        @Size(max = 200, message = "행위명은 200자를 초과할 수 없습니다.")
        private String name;

        @NotNull(message = "가격은 필수입니다.")
        private BigDecimal price;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateProcedureCodeRequest {
        @NotBlank(message = "행위명은 필수입니다.")
        @Size(max = 200, message = "행위명은 200자를 초과할 수 없습니다.")
        private String name;

        @NotNull(message = "가격은 필수입니다.")
        private BigDecimal price;
    }

    @Getter
    @NoArgsConstructor
    public static class CreateBillingRequest {
        @NotNull(message = "환자 ID는 필수입니다.")
        private Long patientId;

        private Long appointmentId;

        @NotNull(message = "청구일은 필수입니다.")
        private LocalDateTime billingDate;

        @NotNull(message = "총 금액은 필수입니다.")
        private BigDecimal totalAmount;

        @NotNull(message = "보험금액은 필수입니다.")
        private BigDecimal insuranceAmount;

        @NotNull(message = "본인부담금은 필수입니다.")
        private BigDecimal patientAmount;

        // 단순화: 타입/내역/비고 제거
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateBillingRequest {
        @NotNull(message = "총 금액은 필수입니다.")
        private BigDecimal totalAmount;

        @NotNull(message = "보험금액은 필수입니다.")
        private BigDecimal insuranceAmount;

        @NotNull(message = "본인부담금은 필수입니다.")
        private BigDecimal patientAmount;

        // 단순화: 타입/내역/비고 제거
    }

    @Getter
    @NoArgsConstructor
    public static class CreateReceiptRequest {
        @NotNull(message = "청구 ID는 필수입니다.")
        private Long billingId;

        @NotNull(message = "영수증 발행일은 필수입니다.")
        private LocalDateTime receiptDate;

        @NotNull(message = "결제 금액은 필수입니다.")
        private BigDecimal amountPaid;

        @NotBlank(message = "결제 방법은 필수입니다.")
        @Size(max = 50, message = "결제 방법은 50자를 초과할 수 없습니다.")
        private String paymentMethod;

        // 단순화: 참조번호/비고 제거
    }

    @Getter
    @NoArgsConstructor
    public static class CalculateBillingRequest {
        @NotNull(message = "환자 ID는 필수입니다.")
        private Long patientId;

        private Long appointmentId;

        @NotNull(message = "청구일은 필수입니다.")
        private LocalDateTime billingDate;

        // 단순화: 유형/내역 제거
    }
}
