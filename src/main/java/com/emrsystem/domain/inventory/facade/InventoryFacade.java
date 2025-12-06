package com.emrsystem.domain.inventory.facade;

import com.emrsystem.domain.inventory.entity.InventoryThreshold;
import com.emrsystem.domain.inventory.entity.PurchaseOrder;
import com.emrsystem.domain.inventory.entity.Supplier;
import com.emrsystem.domain.inventory.request.InventoryRequests;
import com.emrsystem.domain.inventory.response.InventoryResponses;
import com.emrsystem.domain.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InventoryFacade {

    private final InventoryService inventoryService;

    // Supplier operations
    public List<Supplier> getActiveSuppliers() {
        return inventoryService.getActiveSuppliers();
    }

    public Supplier getSupplier(Long id) {
        return inventoryService.getSupplier(id);
    }

    public Supplier createSupplier(InventoryRequests.CreateSupplierRequest request) {
        return inventoryService.createSupplier(request);
    }

    public Supplier updateSupplier(Long id, InventoryRequests.UpdateSupplierRequest request) {
        return inventoryService.updateSupplier(id, request);
    }

    public void activateSupplier(Long id) {
        inventoryService.activateSupplier(id);
    }

    public void deactivateSupplier(Long id) {
        inventoryService.deactivateSupplier(id);
    }

    // Threshold operations
    public InventoryThreshold getThreshold(Long id) {
        return inventoryService.getThreshold(id);
    }

    public InventoryThreshold createOrUpdateThreshold(InventoryRequests.CreateThresholdRequest request) {
        return inventoryService.createOrUpdateThreshold(request);
    }

    public void enableAutoReorder(Long id) {
        inventoryService.enableAutoReorder(id);
    }

    public void disableAutoReorder(Long id) {
        inventoryService.disableAutoReorder(id);
    }

    // PurchaseOrder operations
    public PurchaseOrder getPurchaseOrder(Long id) {
        return inventoryService.getPurchaseOrder(id);
    }

    public Page<PurchaseOrder> getPurchaseOrdersByStatus(String status, Pageable pageable) {
        return inventoryService.getPurchaseOrdersByStatus(status, pageable);
    }

    public List<PurchaseOrder> getPendingPurchaseOrders() {
        return inventoryService.getPendingPurchaseOrders();
    }

    public PurchaseOrder createPurchaseOrder(InventoryRequests.CreatePurchaseOrderRequest request) {
        return inventoryService.createPurchaseOrder(request);
    }

    public PurchaseOrder approvePurchaseOrder(Long id, String approvedBy) {
        return inventoryService.approvePurchaseOrder(id, approvedBy);
    }

    public PurchaseOrder orderPurchaseOrder(Long id) {
        return inventoryService.orderPurchaseOrder(id);
    }

    public PurchaseOrder receivePurchaseOrder(Long id, LocalDate deliveryDate) {
        return inventoryService.receivePurchaseOrder(id, deliveryDate);
    }

    public PurchaseOrder cancelPurchaseOrder(Long id) {
        return inventoryService.cancelPurchaseOrder(id);
    }

    public List<InventoryResponses.LowStockAlert> getLowStockAlerts() {
        return inventoryService.getLowStockAlerts();
    }
}

