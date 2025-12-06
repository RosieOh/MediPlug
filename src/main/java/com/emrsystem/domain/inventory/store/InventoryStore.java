package com.emrsystem.domain.inventory.store;

import com.emrsystem.domain.inventory.entity.InventoryThreshold;
import com.emrsystem.domain.inventory.entity.PurchaseOrder;
import com.emrsystem.domain.inventory.entity.Supplier;
import com.emrsystem.domain.inventory.repository.InventoryThresholdRepository;
import com.emrsystem.domain.inventory.repository.PurchaseOrderRepository;
import com.emrsystem.domain.inventory.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InventoryStore {

    private final SupplierRepository supplierRepository;
    private final InventoryThresholdRepository inventoryThresholdRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    // Supplier operations
    public Supplier saveSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public Optional<Supplier> findSupplierById(Long id) {
        return supplierRepository.findById(id);
    }

    public List<Supplier> findSuppliersByStatus(String status) {
        return supplierRepository.findByStatus(status);
    }

    public Optional<Supplier> findSupplierByName(String name) {
        return supplierRepository.findByName(name);
    }

    public boolean existsSupplierByName(String name) {
        return supplierRepository.existsByName(name);
    }

    // InventoryThreshold operations
    public InventoryThreshold saveThreshold(InventoryThreshold threshold) {
        return inventoryThresholdRepository.save(threshold);
    }

    public Optional<InventoryThreshold> findThresholdById(Long id) {
        return inventoryThresholdRepository.findById(id);
    }

    public Optional<InventoryThreshold> findThresholdByDrugMaster(Long drugMasterId) {
        return inventoryThresholdRepository.findByDrugMaster_DrugMasterId(drugMasterId);
    }

    public List<InventoryThreshold> findThresholdsByAutoReorderEnabled(boolean enabled) {
        return inventoryThresholdRepository.findByAutoReorderEnabled(enabled);
    }

    public boolean existsThresholdByDrugMaster(Long drugMasterId) {
        return inventoryThresholdRepository.existsByDrugMaster_DrugMasterId(drugMasterId);
    }

    // PurchaseOrder operations
    public PurchaseOrder savePurchaseOrder(PurchaseOrder order) {
        return purchaseOrderRepository.save(order);
    }

    public Optional<PurchaseOrder> findPurchaseOrderById(Long id) {
        return purchaseOrderRepository.findById(id);
    }

    public Optional<PurchaseOrder> findPurchaseOrderByOrderNumber(String orderNumber) {
        return purchaseOrderRepository.findByOrderNumber(orderNumber);
    }

    public List<PurchaseOrder> findPurchaseOrdersByStatus(String status) {
        return purchaseOrderRepository.findByStatus(status);
    }

    public Page<PurchaseOrder> findPurchaseOrdersByStatus(String status, Pageable pageable) {
        return purchaseOrderRepository.findByStatus(status, pageable);
    }

    public List<PurchaseOrder> findPurchaseOrdersByDrugMaster(Long drugMasterId) {
        return purchaseOrderRepository.findByDrugMaster_DrugMasterId(drugMasterId);
    }

    public List<PurchaseOrder> findPurchaseOrdersBySupplier(Long supplierId) {
        return purchaseOrderRepository.findBySupplier_SupplierId(supplierId);
    }

    public List<PurchaseOrder> findPurchaseOrdersByExpectedDeliveryDateBetween(LocalDate startDate, LocalDate endDate) {
        return purchaseOrderRepository.findByExpectedDeliveryDateBetween(startDate, endDate);
    }

    public boolean existsPurchaseOrderByOrderNumber(String orderNumber) {
        return purchaseOrderRepository.existsByOrderNumber(orderNumber);
    }
}

