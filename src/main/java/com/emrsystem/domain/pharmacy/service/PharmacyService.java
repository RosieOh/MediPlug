package com.emrsystem.domain.pharmacy.service;

import com.emrsystem.domain.pharmacy.entity.DrugInteraction;
import com.emrsystem.domain.pharmacy.entity.DrugInventory;
import com.emrsystem.domain.pharmacy.entity.DrugMaster;
import com.emrsystem.domain.pharmacy.request.PharmacyRequests;
import com.emrsystem.domain.pharmacy.response.PharmacyResponses;
import com.emrsystem.domain.pharmacy.store.PharmacyStore;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PharmacyService {

    private final PharmacyStore pharmacyStore;

    // Drug Master operations
    public List<DrugMaster> getDrugMasters() {
        return pharmacyStore.findAllDrugMasters();
    }

    public Page<DrugMaster> searchDrugMastersByName(String name, Pageable pageable) {
        return pharmacyStore.findDrugMastersByNameContaining(name, pageable);
    }

    public DrugMaster getDrugMaster(Long id) {
        return pharmacyStore.findDrugMasterById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.DRUG_MASTER_NOT_FOUND));
    }

    @Transactional
    public DrugMaster createDrugMaster(PharmacyRequests.CreateDrugMasterRequest request) {
        if (pharmacyStore.existsDrugMasterByCode(request.getCode())) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION);
        }

        DrugMaster drugMaster = DrugMaster.create(request.getCode(), request.getName(), 
                request.getManufacturer(), request.getUnitPrice(), request.getDescription());
        return pharmacyStore.saveDrugMaster(drugMaster);
    }

    @Transactional
    public DrugMaster updateDrugMaster(Long id, PharmacyRequests.UpdateDrugMasterRequest request) {
        DrugMaster drugMaster = getDrugMaster(id);
        drugMaster.update(request.getName(), request.getManufacturer(), request.getUnitPrice(),
                request.getDescription());
        return pharmacyStore.saveDrugMaster(drugMaster);
    }

    @Transactional
    public void deleteDrugMaster(Long id) {
        if (!pharmacyStore.findDrugMasterById(id).isPresent()) {
            throw new CommonException(ErrorCode.DRUG_MASTER_NOT_FOUND);
        }
        pharmacyStore.deleteDrugMasterById(id);
    }

    // Drug Inventory operations
    public List<DrugInventory> getDrugInventoriesByDrugMaster(Long drugMasterId) {
        return pharmacyStore.findDrugInventoriesByDrugMaster(drugMasterId);
    }

    public List<DrugInventory> getDrugInventoriesByLocation(String location) {
        return pharmacyStore.findDrugInventoriesByLocation(location);
    }

    public DrugInventory getDrugInventory(Long id) {
        return pharmacyStore.findDrugInventoryById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.DRUG_INVENTORY_NOT_FOUND));
    }

    @Transactional
    public DrugInventory createDrugInventory(PharmacyRequests.CreateDrugInventoryRequest request) {
        DrugMaster drugMaster = getDrugMaster(request.getDrugMasterId());

        if (pharmacyStore.existsDrugInventoryByDrugMasterAndBatchNumber(request.getDrugMasterId(), request.getBatchNumber())) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION);
        }

        // unitCost는 DrugMaster의 unitPrice를 사용
        DrugInventory drugInventory = DrugInventory.create(
                drugMaster,
                request.getBatchNumber(),
                request.getQuantity(),
                request.getExpirationDate(),
                drugMaster.getUnitPrice()
        );
        return pharmacyStore.saveDrugInventory(drugInventory);
    }

    @Transactional
    public DrugInventory updateDrugInventory(Long id, PharmacyRequests.UpdateDrugInventoryRequest request) {
        DrugInventory drugInventory = getDrugInventory(id);
        // 엔티티가 필드 수정을 직접 지원하지 않으므로 수량만 조정
        int diff = request.getQuantity() - drugInventory.getQuantity();
        if (diff != 0) {
            drugInventory.adjustQuantity(diff);
        }
        return pharmacyStore.saveDrugInventory(drugInventory);
    }

    @Transactional
    public DrugInventory adjustDrugStock(PharmacyRequests.DrugStockAdjustmentRequest request) {
        DrugMaster drugMaster = getDrugMaster(request.getDrugMasterId());
        
        // Find available inventory to adjust
        List<DrugInventory> inventories = pharmacyStore.findDrugInventoriesByDrugMaster(request.getDrugMasterId());
        if (inventories.isEmpty()) {
            throw new CommonException(ErrorCode.DRUG_INVENTORY_NOT_FOUND);
        }

        // 첫 번째 인벤토리에 조정 적용 (단순 정책)
        DrugInventory inventory = inventories.get(0);
        if (request.getAdjustmentQuantity() < 0 && inventory.getAvailableQuantity() < Math.abs(request.getAdjustmentQuantity())) {
            throw new CommonException(ErrorCode.INSUFFICIENT_STOCK);
        }
        inventory.adjustQuantity(request.getAdjustmentQuantity());
        return pharmacyStore.saveDrugInventory(inventory);
    }

    @Transactional
    public void deleteDrugInventory(Long id) {
        if (!pharmacyStore.findDrugInventoryById(id).isPresent()) {
            throw new CommonException(ErrorCode.DRUG_INVENTORY_NOT_FOUND);
        }
        pharmacyStore.deleteDrugInventoryById(id);
    }

    // Stock management operations
    public List<PharmacyResponses.DrugStockSummary> getDrugStockSummary() {
        List<DrugMaster> drugMasters = pharmacyStore.findAllDrugMasters();
        List<PharmacyResponses.DrugStockSummary> summaries = new ArrayList<>();

        for (DrugMaster drugMaster : drugMasters) {
            List<DrugInventory> inventories = pharmacyStore.findDrugInventoriesByDrugMaster(drugMaster.getDrugMasterId());
            
            int totalQuantity = inventories.stream().mapToInt(DrugInventory::getQuantity).sum();
            // 단순화: 상태 개념 제거 → 모든 수량을 available로 처리
            int availableQuantity = totalQuantity;
            int reservedQuantity = 0;
            
            BigDecimal totalValue = inventories.stream()
                    .map(inv -> drugMaster.getUnitPrice().multiply(BigDecimal.valueOf(inv.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            String status = totalQuantity > 100 ? "SUFFICIENT" : totalQuantity > 20 ? "LOW" : "CRITICAL";

            summaries.add(new PharmacyResponses.DrugStockSummary(
                    drugMaster.getDrugMasterId(), drugMaster.getName(), drugMaster.getCode(),
                    totalQuantity, availableQuantity, reservedQuantity, totalValue, status));
        }

        return summaries;
    }

    public List<PharmacyResponses.DrugExpirationAlert> getDrugExpirationAlerts(PharmacyRequests.DrugExpirationAlertRequest request) {
        LocalDate alertDate = LocalDate.now().plusDays(request.getAlertDays());
        List<DrugInventory> inventories = pharmacyStore.findDrugInventoriesByExpirationDateBefore(alertDate);
        
        List<PharmacyResponses.DrugExpirationAlert> alerts = new ArrayList<>();
        
        for (DrugInventory inventory : inventories) {
            // 카테고리 필터 제거(스키마 미지원)

            long daysUntilExpiration = LocalDate.now().until(inventory.getExpirationDate()).getDays();
            String alertLevel = daysUntilExpiration <= 7 ? "CRITICAL" : 
                              daysUntilExpiration <= 30 ? "WARNING" : "INFO";

            alerts.add(new PharmacyResponses.DrugExpirationAlert(
                    inventory.getDrugInventoryId(), inventory.getDrugMaster().getName(),
                    inventory.getDrugMaster().getCode(), inventory.getBatchNumber(),
                    inventory.getExpirationDate(), (int) daysUntilExpiration,
                    inventory.getQuantity(), inventory.getLocation(), alertLevel));
        }

        return alerts;
    }

    // Drug Interaction operations
    public DrugInteraction getDrugInteraction(Long id) {
        return pharmacyStore.findDrugInteractionById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "약물 상호작용 정보를 찾을 수 없습니다."));
    }

    public List<DrugInteraction> getInteractionsByDrugId(Long drugId) {
        return pharmacyStore.findInteractionsByDrugId(drugId);
    }

    public List<DrugInteraction> checkInteractions(List<Long> drugIds) {
        List<DrugInteraction> interactions = new ArrayList<>();
        
        // 모든 약물 쌍에 대해 상호작용 체크
        for (int i = 0; i < drugIds.size(); i++) {
            for (int j = i + 1; j < drugIds.size(); j++) {
                Long drug1Id = drugIds.get(i);
                Long drug2Id = drugIds.get(j);
                
                List<DrugInteraction> found = pharmacyStore.findInteractionsBetweenDrugs(drug1Id, drug2Id);
                interactions.addAll(found);
            }
        }
        
        return interactions;
    }

    /**
     * 약물 리스트 간의 상호작용을 체크하고 심각한 상호작용이 있으면 예외를 발생시킵니다.
     * @param drugIds 약물 ID 리스트
     * @throws CommonException 심각한 상호작용(CONTRAINDICATED 또는 MAJOR)이 발견된 경우
     */
    public void validateDrugInteractions(List<Long> drugIds) {
        if (drugIds == null || drugIds.size() < 2) {
            return; // 약물이 2개 미만이면 상호작용 체크 불필요
        }

        List<DrugInteraction> interactions = checkInteractions(drugIds);
        
        for (DrugInteraction interaction : interactions) {
            if (interaction.isContraindicated()) {
                throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION,
                        String.format("금기 약물 상호작용이 발견되었습니다: %s와 %s - %s",
                                interaction.getDrug1().getDrugName(),
                                interaction.getDrug2().getDrugName(),
                                interaction.getDescription()));
            } else if (interaction.isMajor()) {
                throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION,
                        String.format("심각한 약물 상호작용이 발견되었습니다: %s와 %s - %s",
                                interaction.getDrug1().getDrugName(),
                                interaction.getDrug2().getDrugName(),
                                interaction.getDescription()));
            }
        }
    }

    @Transactional
    public DrugInteraction createDrugInteraction(PharmacyRequests.CreateDrugInteractionRequest request) {
        DrugMaster drug1 = getDrugMaster(request.getDrug1Id());
        DrugMaster drug2 = getDrugMaster(request.getDrug2Id());

        if (drug1.getDrugMasterId().equals(drug2.getDrugMasterId())) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION, "같은 약물 간의 상호작용은 등록할 수 없습니다.");
        }

        // 이미 존재하는 상호작용 체크
        if (pharmacyStore.findInteractionBetweenDrugs(drug1.getDrugMasterId(), drug2.getDrugMasterId()).isPresent()) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION, "이미 등록된 약물 상호작용입니다.");
        }

        DrugInteraction interaction = DrugInteraction.create(
                drug1, drug2,
                request.getSeverity(),
                request.getDescription(),
                request.getClinicalSignificance(),
                request.getManagement()
        );

        return pharmacyStore.saveDrugInteraction(interaction);
    }

    @Transactional
    public DrugInteraction updateDrugInteraction(Long id, PharmacyRequests.UpdateDrugInteractionRequest request) {
        DrugInteraction interaction = getDrugInteraction(id);
        interaction.update(
                request.getSeverity(),
                request.getDescription(),
                request.getClinicalSignificance(),
                request.getManagement()
        );
        return pharmacyStore.saveDrugInteraction(interaction);
    }

    @Transactional
    public void deactivateDrugInteraction(Long id) {
        DrugInteraction interaction = getDrugInteraction(id);
        interaction.deactivate();
        pharmacyStore.saveDrugInteraction(interaction);
    }

    @Transactional
    public void activateDrugInteraction(Long id) {
        DrugInteraction interaction = getDrugInteraction(id);
        interaction.activate();
        pharmacyStore.saveDrugInteraction(interaction);
    }

    @Transactional
    public void deleteDrugInteraction(Long id) {
        getDrugInteraction(id);
        pharmacyStore.deleteDrugInteractionById(id);
    }
}
