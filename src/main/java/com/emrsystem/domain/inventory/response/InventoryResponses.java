package com.emrsystem.domain.inventory.response;

import com.emrsystem.domain.inventory.entity.InventoryThreshold;
import com.emrsystem.domain.inventory.entity.PurchaseOrder;
import com.emrsystem.domain.inventory.entity.Supplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class InventoryResponses {

    @Getter
    @NoArgsConstructor
    public static class SupplierSummary {
        private Long supplierId;
        private String name;
        private String contactPerson;
        private String phone;
        private String email;
        private String address;
        private String status;
        private String notes;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static SupplierSummary of(Supplier supplier) {
            SupplierSummary summary = new SupplierSummary();
            summary.supplierId = supplier.getId();
            summary.name = supplier.getName();
            summary.contactPerson = supplier.getContactPerson();
            summary.phone = supplier.getPhone();
            summary.email = supplier.getEmail();
            summary.address = supplier.getAddress();
            summary.status = supplier.getStatus();
            summary.notes = supplier.getNotes();
            summary.createdAt = supplier.getCreatedAt();
            summary.updatedAt = supplier.getUpdatedAt();
            return summary;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ThresholdSummary {
        private Long thresholdId;
        private Long drugMasterId;
        private String drugName;
        private int minimumQuantity;
        private int reorderQuantity;
        private boolean autoReorderEnabled;
        private String notes;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static ThresholdSummary of(InventoryThreshold threshold) {
            ThresholdSummary summary = new ThresholdSummary();
            summary.thresholdId = threshold.getId();
            summary.drugMasterId = threshold.getDrugMaster().getDrugMasterId();
            summary.drugName = threshold.getDrugMaster().getDrugName();
            summary.minimumQuantity = threshold.getMinimumQuantity();
            summary.reorderQuantity = threshold.getReorderQuantity();
            summary.autoReorderEnabled = threshold.isAutoReorderEnabled();
            summary.notes = threshold.getNotes();
            summary.createdAt = threshold.getCreatedAt();
            summary.updatedAt = threshold.getUpdatedAt();
            return summary;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class PurchaseOrderSummary {
        private Long orderId;
        private String orderNumber;
        private Long drugMasterId;
        private String drugName;
        private Long supplierId;
        private String supplierName;
        private int quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalAmount;
        private String status;
        private LocalDate expectedDeliveryDate;
        private LocalDate actualDeliveryDate;
        private String orderedBy;
        private String approvedBy;
        private LocalDateTime orderedAt;
        private LocalDateTime approvedAt;
        private String notes;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static PurchaseOrderSummary of(PurchaseOrder order) {
            PurchaseOrderSummary summary = new PurchaseOrderSummary();
            summary.orderId = order.getId();
            summary.orderNumber = order.getOrderNumber();
            summary.drugMasterId = order.getDrugMaster().getDrugMasterId();
            summary.drugName = order.getDrugMaster().getDrugName();
            summary.supplierId = order.getSupplier().getId();
            summary.supplierName = order.getSupplier().getName();
            summary.quantity = order.getQuantity();
            summary.unitPrice = order.getUnitPrice();
            summary.totalAmount = order.getTotalAmount();
            summary.status = order.getStatus();
            summary.expectedDeliveryDate = order.getExpectedDeliveryDate();
            summary.actualDeliveryDate = order.getActualDeliveryDate();
            summary.orderedBy = order.getOrderedBy();
            summary.approvedBy = order.getApprovedBy();
            summary.orderedAt = order.getOrderedAt();
            summary.approvedAt = order.getApprovedAt();
            summary.notes = order.getNotes();
            summary.createdAt = order.getCreatedAt();
            summary.updatedAt = order.getUpdatedAt();
            return summary;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LowStockAlert {
        private Long drugMasterId;
        private String drugName;
        private int currentQuantity;
        private int minimumQuantity;
        private int reorderQuantity;
    }
}

