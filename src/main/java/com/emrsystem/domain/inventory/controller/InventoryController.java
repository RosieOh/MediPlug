package com.emrsystem.domain.inventory.controller;

import com.emrsystem.domain.inventory.entity.InventoryThreshold;
import com.emrsystem.domain.inventory.entity.PurchaseOrder;
import com.emrsystem.domain.inventory.entity.Supplier;
import com.emrsystem.domain.inventory.facade.InventoryFacade;
import com.emrsystem.domain.inventory.request.InventoryRequests;
import com.emrsystem.domain.inventory.response.InventoryResponses;
import com.emrsystem.domain.user.enums.RoleType;
import com.emrsystem.global.common.controller.BaseController;
import com.emrsystem.global.common.dto.ApiResponse;
import com.emrsystem.global.common.dto.PageResponse;
import com.emrsystem.global.security.authorization.RequireRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Inventory Management", description = "재고 자동 주문 관리 API")
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController extends BaseController {

    private final InventoryFacade inventoryFacade;

    // Supplier endpoints
    @GetMapping("/suppliers")
    @Operation(summary = "활성 공급업체 목록 조회", description = "활성 상태인 공급업체 목록을 조회합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<List<InventoryResponses.SupplierSummary>>> getActiveSuppliers() {
        List<Supplier> suppliers = inventoryFacade.getActiveSuppliers();
        List<InventoryResponses.SupplierSummary> summaries = suppliers.stream()
                .map(InventoryResponses.SupplierSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/suppliers/{id}")
    @Operation(summary = "공급업체 조회", description = "특정 공급업체 정보를 조회합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<InventoryResponses.SupplierSummary>> getSupplier(
            @Parameter(description = "공급업체 ID") @PathVariable Long id) {
        Supplier supplier = inventoryFacade.getSupplier(id);
        return ok(InventoryResponses.SupplierSummary.of(supplier));
    }

    @PostMapping("/suppliers")
    @Operation(summary = "공급업체 등록", description = "새로운 공급업체를 등록합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<InventoryResponses.SupplierSummary>> createSupplier(
            @Valid @RequestBody InventoryRequests.CreateSupplierRequest request) {
        Supplier supplier = inventoryFacade.createSupplier(request);
        return created(InventoryResponses.SupplierSummary.of(supplier));
    }

    @PutMapping("/suppliers/{id}")
    @Operation(summary = "공급업체 수정", description = "공급업체 정보를 수정합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<InventoryResponses.SupplierSummary>> updateSupplier(
            @Parameter(description = "공급업체 ID") @PathVariable Long id,
            @Valid @RequestBody InventoryRequests.UpdateSupplierRequest request) {
        Supplier supplier = inventoryFacade.updateSupplier(id, request);
        return ok(InventoryResponses.SupplierSummary.of(supplier));
    }

    @PostMapping("/suppliers/{id}/activate")
    @Operation(summary = "공급업체 활성화", description = "공급업체를 활성화합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<Void>> activateSupplier(
            @Parameter(description = "공급업체 ID") @PathVariable Long id) {
        inventoryFacade.activateSupplier(id);
        return ok(null);
    }

    @PostMapping("/suppliers/{id}/deactivate")
    @Operation(summary = "공급업체 비활성화", description = "공급업체를 비활성화합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<Void>> deactivateSupplier(
            @Parameter(description = "공급업체 ID") @PathVariable Long id) {
        inventoryFacade.deactivateSupplier(id);
        return ok(null);
    }

    // Threshold endpoints
    @GetMapping("/thresholds/{id}")
    @Operation(summary = "재고 임계값 조회", description = "특정 약물의 재고 임계값을 조회합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<InventoryResponses.ThresholdSummary>> getThreshold(
            @Parameter(description = "임계값 ID") @PathVariable Long id) {
        InventoryThreshold threshold = inventoryFacade.getThreshold(id);
        return ok(InventoryResponses.ThresholdSummary.of(threshold));
    }

    @PostMapping("/thresholds")
    @Operation(summary = "재고 임계값 설정", description = "약물의 재고 임계값을 설정합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<InventoryResponses.ThresholdSummary>> createOrUpdateThreshold(
            @Valid @RequestBody InventoryRequests.CreateThresholdRequest request) {
        InventoryThreshold threshold = inventoryFacade.createOrUpdateThreshold(request);
        return ok(InventoryResponses.ThresholdSummary.of(threshold));
    }

    @PostMapping("/thresholds/{id}/enable-auto-reorder")
    @Operation(summary = "자동 주문 활성화", description = "자동 주문 기능을 활성화합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<Void>> enableAutoReorder(
            @Parameter(description = "임계값 ID") @PathVariable Long id) {
        inventoryFacade.enableAutoReorder(id);
        return ok(null);
    }

    @PostMapping("/thresholds/{id}/disable-auto-reorder")
    @Operation(summary = "자동 주문 비활성화", description = "자동 주문 기능을 비활성화합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<Void>> disableAutoReorder(
            @Parameter(description = "임계값 ID") @PathVariable Long id) {
        inventoryFacade.disableAutoReorder(id);
        return ok(null);
    }

    // PurchaseOrder endpoints
    @GetMapping("/purchase-orders")
    @Operation(summary = "구매 주문 목록 조회", description = "구매 주문 목록을 조회합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<PageResponse<InventoryResponses.PurchaseOrderSummary>>> getPurchaseOrders(
            @Parameter(description = "주문 상태") @RequestParam(required = false) String status,
            @Parameter(description = "페이지 정보") @PageableDefault(size = 20) Pageable pageable) {
        Page<PurchaseOrder> orders = inventoryFacade.getPurchaseOrdersByStatus(
                status != null ? status : "PENDING", pageable);
        Page<InventoryResponses.PurchaseOrderSummary> summaries = orders.map(InventoryResponses.PurchaseOrderSummary::of);
        return ok(PageResponse.of(summaries));
    }

    @GetMapping("/purchase-orders/{id}")
    @Operation(summary = "구매 주문 조회", description = "특정 구매 주문을 조회합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<InventoryResponses.PurchaseOrderSummary>> getPurchaseOrder(
            @Parameter(description = "주문 ID") @PathVariable Long id) {
        PurchaseOrder order = inventoryFacade.getPurchaseOrder(id);
        return ok(InventoryResponses.PurchaseOrderSummary.of(order));
    }

    @PostMapping("/purchase-orders")
    @Operation(summary = "구매 주문 생성", description = "새로운 구매 주문을 생성합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<InventoryResponses.PurchaseOrderSummary>> createPurchaseOrder(
            @Valid @RequestBody InventoryRequests.CreatePurchaseOrderRequest request) {
        PurchaseOrder order = inventoryFacade.createPurchaseOrder(request);
        return created(InventoryResponses.PurchaseOrderSummary.of(order));
    }

    @PostMapping("/purchase-orders/{id}/approve")
    @Operation(summary = "구매 주문 승인", description = "구매 주문을 승인합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<InventoryResponses.PurchaseOrderSummary>> approvePurchaseOrder(
            @Parameter(description = "주문 ID") @PathVariable Long id,
            @Parameter(description = "승인한 사람") @RequestParam String approvedBy) {
        PurchaseOrder order = inventoryFacade.approvePurchaseOrder(id, approvedBy);
        return ok(InventoryResponses.PurchaseOrderSummary.of(order));
    }

    @PostMapping("/purchase-orders/{id}/order")
    @Operation(summary = "구매 주문 발주", description = "승인된 주문을 발주합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<InventoryResponses.PurchaseOrderSummary>> orderPurchaseOrder(
            @Parameter(description = "주문 ID") @PathVariable Long id) {
        PurchaseOrder order = inventoryFacade.orderPurchaseOrder(id);
        return ok(InventoryResponses.PurchaseOrderSummary.of(order));
    }

    @PostMapping("/purchase-orders/{id}/receive")
    @Operation(summary = "구매 주문 납품 처리", description = "주문된 약품의 납품을 처리합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<InventoryResponses.PurchaseOrderSummary>> receivePurchaseOrder(
            @Parameter(description = "주문 ID") @PathVariable Long id,
            @Parameter(description = "납품일") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deliveryDate) {
        PurchaseOrder order = inventoryFacade.receivePurchaseOrder(id, deliveryDate);
        return ok(InventoryResponses.PurchaseOrderSummary.of(order));
    }

    @PostMapping("/purchase-orders/{id}/cancel")
    @Operation(summary = "구매 주문 취소", description = "구매 주문을 취소합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<InventoryResponses.PurchaseOrderSummary>> cancelPurchaseOrder(
            @Parameter(description = "주문 ID") @PathVariable Long id) {
        PurchaseOrder order = inventoryFacade.cancelPurchaseOrder(id);
        return ok(InventoryResponses.PurchaseOrderSummary.of(order));
    }

    // Low stock alerts
    @GetMapping("/low-stock-alerts")
    @Operation(summary = "재고 부족 알림 조회", description = "재고가 부족한 약품 목록을 조회합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<List<InventoryResponses.LowStockAlert>>> getLowStockAlerts() {
        List<InventoryResponses.LowStockAlert> alerts = inventoryFacade.getLowStockAlerts();
        return ok(alerts);
    }
}

