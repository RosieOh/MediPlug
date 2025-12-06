package com.emrsystem.domain.inventory.service;

import com.emrsystem.domain.inventory.entity.InventoryThreshold;
import com.emrsystem.domain.inventory.entity.PurchaseOrder;
import com.emrsystem.domain.inventory.entity.Supplier;
import com.emrsystem.domain.inventory.request.InventoryRequests;
import com.emrsystem.domain.inventory.response.InventoryResponses;
import com.emrsystem.domain.inventory.store.InventoryStore;
import com.emrsystem.domain.pharmacy.entity.DrugMaster;
import com.emrsystem.domain.pharmacy.entity.DrugInventory;
import com.emrsystem.domain.pharmacy.store.PharmacyStore;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventoryService {

    private final InventoryStore inventoryStore;
    private final PharmacyStore pharmacyStore;

    // Supplier operations
    public List<Supplier> getActiveSuppliers() {
        return inventoryStore.findSuppliersByStatus("ACTIVE");
    }

    public Supplier getSupplier(Long id) {
        return inventoryStore.findSupplierById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "공급업체를 찾을 수 없습니다."));
    }

    @Transactional
    public Supplier createSupplier(InventoryRequests.CreateSupplierRequest request) {
        if (inventoryStore.existsSupplierByName(request.getName())) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION, "이미 존재하는 공급업체명입니다.");
        }

        Supplier supplier = Supplier.create(
                request.getName(),
                request.getContactPerson(),
                request.getPhone(),
                request.getEmail(),
                request.getAddress()
        );

        return inventoryStore.saveSupplier(supplier);
    }

    @Transactional
    public Supplier updateSupplier(Long id, InventoryRequests.UpdateSupplierRequest request) {
        Supplier supplier = getSupplier(id);
        supplier.update(
                request.getName(),
                request.getContactPerson(),
                request.getPhone(),
                request.getEmail(),
                request.getAddress(),
                request.getNotes()
        );
        return inventoryStore.saveSupplier(supplier);
    }

    @Transactional
    public void activateSupplier(Long id) {
        Supplier supplier = getSupplier(id);
        supplier.activate();
        inventoryStore.saveSupplier(supplier);
    }

    @Transactional
    public void deactivateSupplier(Long id) {
        Supplier supplier = getSupplier(id);
        supplier.deactivate();
        inventoryStore.saveSupplier(supplier);
    }

    // InventoryThreshold operations
    public InventoryThreshold getThreshold(Long id) {
        return inventoryStore.findThresholdById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "재고 임계값을 찾을 수 없습니다."));
    }

    public InventoryThreshold getThresholdByDrugMaster(Long drugMasterId) {
        return inventoryStore.findThresholdByDrugMaster(drugMasterId)
                .orElse(null);
    }

    @Transactional
    public InventoryThreshold createOrUpdateThreshold(InventoryRequests.CreateThresholdRequest request) {
        DrugMaster drugMaster = pharmacyStore.findDrugMasterById(request.getDrugMasterId())
                .orElseThrow(() -> new CommonException(ErrorCode.DRUG_MASTER_NOT_FOUND));

        InventoryThreshold threshold = inventoryStore.findThresholdByDrugMaster(request.getDrugMasterId())
                .orElse(null);

        if (threshold == null) {
            threshold = InventoryThreshold.create(
                    drugMaster,
                    request.getMinimumQuantity(),
                    request.getReorderQuantity()
            );
        } else {
            threshold.update(
                    request.getMinimumQuantity(),
                    request.getReorderQuantity(),
                    request.isAutoReorderEnabled(),
                    request.getNotes()
            );
        }

        return inventoryStore.saveThreshold(threshold);
    }

    @Transactional
    public void enableAutoReorder(Long id) {
        InventoryThreshold threshold = getThreshold(id);
        threshold.enableAutoReorder();
        inventoryStore.saveThreshold(threshold);
    }

    @Transactional
    public void disableAutoReorder(Long id) {
        InventoryThreshold threshold = getThreshold(id);
        threshold.disableAutoReorder();
        inventoryStore.saveThreshold(threshold);
    }

    // PurchaseOrder operations
    public PurchaseOrder getPurchaseOrder(Long id) {
        return inventoryStore.findPurchaseOrderById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "구매 주문을 찾을 수 없습니다."));
    }

    public Page<PurchaseOrder> getPurchaseOrdersByStatus(String status, Pageable pageable) {
        return inventoryStore.findPurchaseOrdersByStatus(status, pageable);
    }

    public List<PurchaseOrder> getPendingPurchaseOrders() {
        return inventoryStore.findPurchaseOrdersByStatus("PENDING");
    }

    @Transactional
    public PurchaseOrder createPurchaseOrder(InventoryRequests.CreatePurchaseOrderRequest request) {
        DrugMaster drugMaster = pharmacyStore.findDrugMasterById(request.getDrugMasterId())
                .orElseThrow(() -> new CommonException(ErrorCode.DRUG_MASTER_NOT_FOUND));

        Supplier supplier = getSupplier(request.getSupplierId());

        String orderNumber = generateOrderNumber();
        if (inventoryStore.existsPurchaseOrderByOrderNumber(orderNumber)) {
            orderNumber = generateOrderNumber(); // 재시도
        }

        PurchaseOrder order = PurchaseOrder.create(
                orderNumber,
                drugMaster,
                supplier,
                request.getQuantity(),
                request.getUnitPrice(),
                request.getOrderedBy()
        );

        if (request.getExpectedDeliveryDate() != null) {
            order.updateExpectedDeliveryDate(request.getExpectedDeliveryDate());
        }

        return inventoryStore.savePurchaseOrder(order);
    }

    @Transactional
    public PurchaseOrder approvePurchaseOrder(Long id, String approvedBy) {
        PurchaseOrder order = getPurchaseOrder(id);
        if (!"PENDING".equals(order.getStatus())) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION, "승인할 수 없는 주문 상태입니다.");
        }
        order.approve(approvedBy);
        return inventoryStore.savePurchaseOrder(order);
    }

    @Transactional
    public PurchaseOrder orderPurchaseOrder(Long id) {
        PurchaseOrder order = getPurchaseOrder(id);
        if (!"APPROVED".equals(order.getStatus())) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION, "주문할 수 없는 상태입니다.");
        }
        order.order();
        return inventoryStore.savePurchaseOrder(order);
    }

    @Transactional
    public PurchaseOrder receivePurchaseOrder(Long id, LocalDate deliveryDate) {
        PurchaseOrder order = getPurchaseOrder(id);
        if (!"ORDERED".equals(order.getStatus())) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION, "납품 처리할 수 없는 상태입니다.");
        }
        order.receive(deliveryDate);

        // 재고에 추가
        DrugInventory inventory = DrugInventory.create(
                order.getDrugMaster(),
                "BATCH-" + System.currentTimeMillis(),
                order.getQuantity(),
                LocalDate.now().plusMonths(12), // 기본 유통기한 12개월
                order.getUnitPrice()
        );
        pharmacyStore.saveDrugInventory(inventory);

        return inventoryStore.savePurchaseOrder(order);
    }

    @Transactional
    public PurchaseOrder cancelPurchaseOrder(Long id) {
        PurchaseOrder order = getPurchaseOrder(id);
        if ("RECEIVED".equals(order.getStatus()) || "CANCELLED".equals(order.getStatus())) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION, "취소할 수 없는 주문 상태입니다.");
        }
        order.cancel();
        return inventoryStore.savePurchaseOrder(order);
    }

    // Auto reorder check
    @Scheduled(cron = "0 0 9 * * *") // 매일 오전 9시 실행
    @Transactional
    public void checkAndCreateAutoOrders() {
        log.info("Starting auto reorder check...");
        List<InventoryThreshold> thresholds = inventoryStore.findThresholdsByAutoReorderEnabled(true);

        for (InventoryThreshold threshold : thresholds) {
            List<DrugInventory> inventories = pharmacyStore.findDrugInventoriesByDrugMaster(
                    threshold.getDrugMaster().getDrugMasterId());

            int totalQuantity = inventories.stream()
                    .filter(inv -> "AVAILABLE".equals(inv.getStatus()))
                    .mapToInt(DrugInventory::getAvailableQuantity)
                    .sum();

            if (threshold.isBelowThreshold(totalQuantity)) {
                log.info("Low stock detected for drug: {}, current: {}, threshold: {}",
                        threshold.getDrugMaster().getDrugName(), totalQuantity, threshold.getMinimumQuantity());

                // 기본 공급업체가 있다면 자동 주문 생성 (간단한 구현)
                List<Supplier> suppliers = inventoryStore.findSuppliersByStatus("ACTIVE");
                if (!suppliers.isEmpty()) {
                    Supplier defaultSupplier = suppliers.get(0);
                    String orderNumber = generateOrderNumber();
                    PurchaseOrder order = PurchaseOrder.create(
                            orderNumber,
                            threshold.getDrugMaster(),
                            defaultSupplier,
                            threshold.getReorderQuantity(),
                            threshold.getDrugMaster().getUnitPrice(),
                            "SYSTEM"
                    );
                    inventoryStore.savePurchaseOrder(order);
                    log.info("Auto order created: {}", orderNumber);
                }
            }
        }
        log.info("Auto reorder check completed.");
    }

    public List<InventoryResponses.LowStockAlert> getLowStockAlerts() {
        List<InventoryThreshold> thresholds = inventoryStore.findThresholdsByAutoReorderEnabled(true);
        return thresholds.stream()
                .map(threshold -> {
                    List<DrugInventory> inventories = pharmacyStore.findDrugInventoriesByDrugMaster(
                            threshold.getDrugMaster().getDrugMasterId());
                    int totalQuantity = inventories.stream()
                            .filter(inv -> "AVAILABLE".equals(inv.getStatus()))
                            .mapToInt(DrugInventory::getAvailableQuantity)
                            .sum();

                    if (threshold.isBelowThreshold(totalQuantity)) {
                        return InventoryResponses.LowStockAlert.builder()
                                .drugMasterId(threshold.getDrugMaster().getDrugMasterId())
                                .drugName(threshold.getDrugMaster().getDrugName())
                                .currentQuantity(totalQuantity)
                                .minimumQuantity(threshold.getMinimumQuantity())
                                .reorderQuantity(threshold.getReorderQuantity())
                                .build();
                    }
                    return null;
                })
                .filter(alert -> alert != null)
                .collect(Collectors.toList());
    }

    private String generateOrderNumber() {
        return "PO-" + System.currentTimeMillis();
    }
}

