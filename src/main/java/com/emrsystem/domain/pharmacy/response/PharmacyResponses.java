package com.emrsystem.domain.pharmacy.response;

import com.emrsystem.domain.pharmacy.entity.DrugInteraction;
import com.emrsystem.domain.pharmacy.entity.DrugInventory;
import com.emrsystem.domain.pharmacy.entity.DrugMaster;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PharmacyResponses {

    @Getter
    @NoArgsConstructor
    public static class DrugMasterSummary {
        private Long drugMasterId;
        private String drugCode;
        private String drugName;
        private BigDecimal unitPrice;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static DrugMasterSummary of(DrugMaster drugMaster) {
            DrugMasterSummary summary = new DrugMasterSummary();
            summary.drugMasterId = drugMaster.getDrugMasterId();
            summary.drugCode = drugMaster.getDrugCode();
            summary.drugName = drugMaster.getDrugName();
            summary.unitPrice = drugMaster.getUnitPrice();
            summary.createdAt = drugMaster.getCreatedAt();
            summary.updatedAt = drugMaster.getUpdatedAt();
            return summary;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class DrugInventorySummary {
        private Long drugInventoryId;
        private Long drugMasterId;
        private String drugName;
        private String drugCode;
        private String lotNumber;
        private LocalDate expirationDate;
        private Integer quantity;
        private Integer availableQuantity;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static DrugInventorySummary of(DrugInventory drugInventory) {
            DrugInventorySummary summary = new DrugInventorySummary();
            summary.drugInventoryId = drugInventory.getDrugInventoryId();
            summary.drugMasterId = drugInventory.getDrugMaster().getDrugMasterId();
            summary.drugName = drugInventory.getDrugMaster().getDrugName();
            summary.drugCode = drugInventory.getDrugMaster().getDrugCode();
            summary.lotNumber = drugInventory.getLotNumber();
            summary.expirationDate = drugInventory.getExpirationDate();
            summary.quantity = drugInventory.getQuantity();
            summary.availableQuantity = drugInventory.getAvailableQuantity();
            summary.status = drugInventory.getStatus();
            summary.createdAt = drugInventory.getCreatedAt();
            summary.updatedAt = drugInventory.getUpdatedAt();
            return summary;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class DrugStockSummary {
        private Long drugMasterId;
        private String drugName;
        private String drugCode;
        private Integer totalQuantity;
        private Integer availableQuantity;
        private Integer reservedQuantity;
        private BigDecimal totalValue;
        private String status;

        public DrugStockSummary(Long drugMasterId, String drugName, String drugCode, 
                Integer totalQuantity, Integer availableQuantity, Integer reservedQuantity, 
                BigDecimal totalValue, String status) {
            this.drugMasterId = drugMasterId;
            this.drugName = drugName;
            this.drugCode = drugCode;
            this.totalQuantity = totalQuantity;
            this.availableQuantity = availableQuantity;
            this.reservedQuantity = reservedQuantity;
            this.totalValue = totalValue;
            this.status = status;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class DrugExpirationAlert {
        private Long drugInventoryId;
        private String drugName;
        private String drugCode;
        private String batchNumber;
        private LocalDate expirationDate;
        private Integer daysUntilExpiration;
        private Integer quantity;
        private String location;
        private String alertLevel;

        public DrugExpirationAlert(Long drugInventoryId, String drugName, String drugCode, 
                String batchNumber, LocalDate expirationDate, Integer daysUntilExpiration, 
                Integer quantity, String location, String alertLevel) {
            this.drugInventoryId = drugInventoryId;
            this.drugName = drugName;
            this.drugCode = drugCode;
            this.batchNumber = batchNumber;
            this.expirationDate = expirationDate;
            this.daysUntilExpiration = daysUntilExpiration;
            this.quantity = quantity;
            this.location = location;
            this.alertLevel = alertLevel;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class DrugInteractionSummary {
        private Long interactionId;
        private Long drug1Id;
        private String drug1Name;
        private String drug1Code;
        private Long drug2Id;
        private String drug2Name;
        private String drug2Code;
        private String severity;
        private String description;
        private String clinicalSignificance;
        private String management;
        private boolean active;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static DrugInteractionSummary of(DrugInteraction interaction) {
            DrugInteractionSummary summary = new DrugInteractionSummary();
            summary.interactionId = interaction.getId();
            summary.drug1Id = interaction.getDrug1().getDrugMasterId();
            summary.drug1Name = interaction.getDrug1().getDrugName();
            summary.drug1Code = interaction.getDrug1().getDrugCode();
            summary.drug2Id = interaction.getDrug2().getDrugMasterId();
            summary.drug2Name = interaction.getDrug2().getDrugName();
            summary.drug2Code = interaction.getDrug2().getDrugCode();
            summary.severity = interaction.getSeverity();
            summary.description = interaction.getDescription();
            summary.clinicalSignificance = interaction.getClinicalSignificance();
            summary.management = interaction.getManagement();
            summary.active = interaction.isActive();
            summary.createdAt = interaction.getCreatedAt();
            summary.updatedAt = interaction.getUpdatedAt();
            return summary;
        }
    }
}
