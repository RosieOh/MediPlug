package com.emrsystem.domain.inventory.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InventoryRequests {

    @Getter
    @NoArgsConstructor
    public static class CreateSupplierRequest {
        @NotBlank(message = "공급업체명은 필수입니다.")
        @Size(max = 200, message = "공급업체명은 200자를 초과할 수 없습니다.")
        private String name;

        @Size(max = 100, message = "담당자는 100자를 초과할 수 없습니다.")
        private String contactPerson;

        @Size(max = 50, message = "전화번호는 50자를 초과할 수 없습니다.")
        private String phone;

        @Size(max = 100, message = "이메일은 100자를 초과할 수 없습니다.")
        private String email;

        @Size(max = 200, message = "주소는 200자를 초과할 수 없습니다.")
        private String address;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateSupplierRequest {
        @NotBlank(message = "공급업체명은 필수입니다.")
        @Size(max = 200, message = "공급업체명은 200자를 초과할 수 없습니다.")
        private String name;

        @Size(max = 100, message = "담당자는 100자를 초과할 수 없습니다.")
        private String contactPerson;

        @Size(max = 50, message = "전화번호는 50자를 초과할 수 없습니다.")
        private String phone;

        @Size(max = 100, message = "이메일은 100자를 초과할 수 없습니다.")
        private String email;

        @Size(max = 200, message = "주소는 200자를 초과할 수 없습니다.")
        private String address;

        private String notes;
    }

    @Getter
    @NoArgsConstructor
    public static class CreateThresholdRequest {
        @NotNull(message = "약물 ID는 필수입니다.")
        private Long drugMasterId;

        @NotNull(message = "최소 재고량은 필수입니다.")
        private int minimumQuantity;

        @NotNull(message = "재주문 수량은 필수입니다.")
        private int reorderQuantity;

        private boolean autoReorderEnabled = true;

        private String notes;
    }

    @Getter
    @NoArgsConstructor
    public static class CreatePurchaseOrderRequest {
        @NotNull(message = "약물 ID는 필수입니다.")
        private Long drugMasterId;

        @NotNull(message = "공급업체 ID는 필수입니다.")
        private Long supplierId;

        @NotNull(message = "주문 수량은 필수입니다.")
        private int quantity;

        @NotNull(message = "단가는 필수입니다.")
        private BigDecimal unitPrice;

        private LocalDate expectedDeliveryDate;

        @NotBlank(message = "주문한 사람은 필수입니다.")
        @Size(max = 200, message = "주문한 사람은 200자를 초과할 수 없습니다.")
        private String orderedBy;
    }
}

