package com.emrsystem.domain.pharmacy.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PharmacyRequests {

    @Getter
    @NoArgsConstructor
    public static class CreateDrugMasterRequest {
        @NotBlank(message = "약물코드는 필수입니다.")
        @Size(max = 50, message = "약물코드는 50자를 초과할 수 없습니다.")
        private String code;

        @NotBlank(message = "약물명은 필수입니다.")
        @Size(max = 200, message = "약물명은 200자를 초과할 수 없습니다.")
        private String name;

        @NotBlank(message = "제조사는 필수입니다.")
        @Size(max = 200, message = "제조사는 200자를 초과할 수 없습니다.")
        private String manufacturer;

        @NotNull(message = "단가는 필수입니다.")
        private BigDecimal unitPrice;

        @Size(max = 1000, message = "설명은 1000자를 초과할 수 없습니다.")
        private String description;

        @NotBlank(message = "분류는 필수입니다.")
        @Size(max = 100, message = "분류는 100자를 초과할 수 없습니다.")
        private String category;

        @NotBlank(message = "단위는 필수입니다.")
        @Size(max = 50, message = "단위는 50자를 초과할 수 없습니다.")
        private String unit;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateDrugMasterRequest {
        @NotBlank(message = "약물명은 필수입니다.")
        @Size(max = 200, message = "약물명은 200자를 초과할 수 없습니다.")
        private String name;

        @NotBlank(message = "제조사는 필수입니다.")
        @Size(max = 200, message = "제조사는 200자를 초과할 수 없습니다.")
        private String manufacturer;

        @NotNull(message = "단가는 필수입니다.")
        private BigDecimal unitPrice;

        @Size(max = 1000, message = "설명은 1000자를 초과할 수 없습니다.")
        private String description;

        @NotBlank(message = "분류는 필수입니다.")
        @Size(max = 100, message = "분류는 100자를 초과할 수 없습니다.")
        private String category;

        @NotBlank(message = "단위는 필수입니다.")
        @Size(max = 50, message = "단위는 50자를 초과할 수 없습니다.")
        private String unit;
    }

    @Getter
    @NoArgsConstructor
    public static class CreateDrugInventoryRequest {
        @NotNull(message = "약물 ID는 필수입니다.")
        private Long drugMasterId;

        @NotBlank(message = "배치번호는 필수입니다.")
        @Size(max = 100, message = "배치번호는 100자를 초과할 수 없습니다.")
        private String batchNumber;

        @NotNull(message = "유통기한은 필수입니다.")
        private LocalDate expirationDate;

        @NotNull(message = "수량은 필수입니다.")
        private Integer quantity;

        @Size(max = 100, message = "보관위치는 100자를 초과할 수 없습니다.")
        private String location;

        @Size(max = 500, message = "비고는 500자를 초과할 수 없습니다.")
        private String notes;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateDrugInventoryRequest {
        @NotBlank(message = "배치번호는 필수입니다.")
        @Size(max = 100, message = "배치번호는 100자를 초과할 수 없습니다.")
        private String batchNumber;

        @NotNull(message = "유통기한은 필수입니다.")
        private LocalDate expirationDate;

        @NotNull(message = "수량은 필수입니다.")
        private Integer quantity;

        @Size(max = 100, message = "보관위치는 100자를 초과할 수 없습니다.")
        private String location;

        @Size(max = 500, message = "비고는 500자를 초과할 수 없습니다.")
        private String notes;
    }

    @Getter
    @NoArgsConstructor
    public static class DrugStockAdjustmentRequest {
        @NotNull(message = "약물 ID는 필수입니다.")
        private Long drugMasterId;

        @NotNull(message = "조정 수량은 필수입니다.")
        private Integer adjustmentQuantity;

        @NotBlank(message = "조정 사유는 필수입니다.")
        @Size(max = 200, message = "조정 사유는 200자를 초과할 수 없습니다.")
        private String reason;

        @Size(max = 500, message = "비고는 500자를 초과할 수 없습니다.")
        private String notes;
    }

    @Getter
    @NoArgsConstructor
    public static class DrugExpirationAlertRequest {
        @NotNull(message = "알림 일수는 필수입니다.")
        private Integer alertDays;

        @Size(max = 100, message = "카테고리는 100자를 초과할 수 없습니다.")
        private String category;
    }

    @Getter
    @NoArgsConstructor
    public static class CreateDrugInteractionRequest {
        @NotNull(message = "약물1 ID는 필수입니다.")
        private Long drug1Id;

        @NotNull(message = "약물2 ID는 필수입니다.")
        private Long drug2Id;

        @NotBlank(message = "심각도는 필수입니다.")
        @Size(max = 50, message = "심각도는 50자를 초과할 수 없습니다.")
        private String severity; // MINOR, MODERATE, MAJOR, CONTRAINDICATED

        @NotBlank(message = "설명은 필수입니다.")
        private String description;

        private String clinicalSignificance;

        private String management;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateDrugInteractionRequest {
        @NotBlank(message = "심각도는 필수입니다.")
        @Size(max = 50, message = "심각도는 50자를 초과할 수 없습니다.")
        private String severity;

        @NotBlank(message = "설명은 필수입니다.")
        private String description;

        private String clinicalSignificance;

        private String management;
    }

    @Getter
    @NoArgsConstructor
    public static class CheckDrugInteractionsRequest {
        @NotNull(message = "약물 ID 리스트는 필수입니다.")
        private java.util.List<Long> drugIds;
    }
}
