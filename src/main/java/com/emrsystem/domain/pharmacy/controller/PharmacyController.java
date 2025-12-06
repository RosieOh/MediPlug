package com.emrsystem.domain.pharmacy.controller;

import com.emrsystem.domain.pharmacy.entity.DrugInteraction;
import com.emrsystem.domain.pharmacy.entity.DrugInventory;
import com.emrsystem.domain.pharmacy.entity.DrugMaster;
import com.emrsystem.domain.pharmacy.facade.PharmacyFacade;
import com.emrsystem.domain.pharmacy.request.PharmacyRequests;
import com.emrsystem.domain.pharmacy.response.PharmacyResponses;
import com.emrsystem.global.common.controller.BaseController;
import com.emrsystem.global.common.dto.ApiResponse;
import com.emrsystem.global.common.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Pharmacy", description = "약국/재고 관리 API")
@RestController
@RequestMapping("/api/pharmacy")
@RequiredArgsConstructor
public class PharmacyController extends BaseController {

    private final PharmacyFacade pharmacyFacade;

    // Drug Master endpoints
    @GetMapping("/drug-masters")
    @Operation(summary = "약물 마스터 목록 조회", description = "약물 마스터 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<PharmacyResponses.DrugMasterSummary>>> getDrugMasters() {
        List<DrugMaster> drugMasters = pharmacyFacade.getDrugMasters();
        List<PharmacyResponses.DrugMasterSummary> summaries = drugMasters.stream()
                .map(PharmacyResponses.DrugMasterSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/drug-masters/search")
    @Operation(summary = "약물명으로 검색", description = "약물명으로 약물 마스터를 검색합니다.")
    public ResponseEntity<ApiResponse<PageResponse<PharmacyResponses.DrugMasterSummary>>> searchDrugMastersByName(
            @Parameter(description = "약물명") @RequestParam String name,
            @Parameter(description = "페이지 정보") @PageableDefault(size = 20) Pageable pageable) {
        Page<DrugMaster> drugMasters = pharmacyFacade.searchDrugMastersByName(name, pageable);
        Page<PharmacyResponses.DrugMasterSummary> summaries = drugMasters.map(PharmacyResponses.DrugMasterSummary::of);
        return ok(PageResponse.of(summaries));
    }

    @GetMapping("/drug-masters/{id}")
    @Operation(summary = "약물 마스터 상세 조회", description = "특정 약물 마스터의 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<PharmacyResponses.DrugMasterSummary>> getDrugMaster(
            @Parameter(description = "약물 마스터 ID") @PathVariable Long id) {
        DrugMaster drugMaster = pharmacyFacade.getDrugMaster(id);
        return ok(PharmacyResponses.DrugMasterSummary.of(drugMaster));
    }

    @PostMapping("/drug-masters")
    @Operation(summary = "약물 마스터 생성", description = "새로운 약물 마스터를 생성합니다.")
    public ResponseEntity<ApiResponse<PharmacyResponses.DrugMasterSummary>> createDrugMaster(
            @Valid @RequestBody PharmacyRequests.CreateDrugMasterRequest request) {
        DrugMaster drugMaster = pharmacyFacade.createDrugMaster(request);
        return created(PharmacyResponses.DrugMasterSummary.of(drugMaster));
    }

    @PutMapping("/drug-masters/{id}")
    @Operation(summary = "약물 마스터 수정", description = "기존 약물 마스터를 수정합니다.")
    public ResponseEntity<ApiResponse<PharmacyResponses.DrugMasterSummary>> updateDrugMaster(
            @Parameter(description = "약물 마스터 ID") @PathVariable Long id,
            @Valid @RequestBody PharmacyRequests.UpdateDrugMasterRequest request) {
        DrugMaster drugMaster = pharmacyFacade.updateDrugMaster(id, request);
        return ok(PharmacyResponses.DrugMasterSummary.of(drugMaster));
    }

    @DeleteMapping("/drug-masters/{id}")
    @Operation(summary = "약물 마스터 삭제", description = "약물 마스터를 삭제합니다.")
    public ResponseEntity<ApiResponse<String>> deleteDrugMaster(
            @Parameter(description = "약물 마스터 ID") @PathVariable Long id) {
        pharmacyFacade.deleteDrugMaster(id);
        return okMessage("deleted");
    }

    // Drug Inventory endpoints
    @GetMapping("/drug-inventories/drug-master/{drugMasterId}")
    @Operation(summary = "약물별 재고 조회", description = "특정 약물의 재고를 조회합니다.")
    public ResponseEntity<ApiResponse<List<PharmacyResponses.DrugInventorySummary>>> getDrugInventoriesByDrugMaster(
            @Parameter(description = "약물 마스터 ID") @PathVariable Long drugMasterId) {
        List<DrugInventory> inventories = pharmacyFacade.getDrugInventoriesByDrugMaster(drugMasterId);
        List<PharmacyResponses.DrugInventorySummary> summaries = inventories.stream()
                .map(PharmacyResponses.DrugInventorySummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/drug-inventories/location/{location}")
    @Operation(summary = "위치별 재고 조회", description = "특정 위치의 재고를 조회합니다.")
    public ResponseEntity<ApiResponse<List<PharmacyResponses.DrugInventorySummary>>> getDrugInventoriesByLocation(
            @Parameter(description = "보관위치") @PathVariable String location) {
        List<DrugInventory> inventories = pharmacyFacade.getDrugInventoriesByLocation(location);
        List<PharmacyResponses.DrugInventorySummary> summaries = inventories.stream()
                .map(PharmacyResponses.DrugInventorySummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/drug-inventories/{id}")
    @Operation(summary = "재고 상세 조회", description = "특정 재고의 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<PharmacyResponses.DrugInventorySummary>> getDrugInventory(
            @Parameter(description = "재고 ID") @PathVariable Long id) {
        DrugInventory inventory = pharmacyFacade.getDrugInventory(id);
        return ok(PharmacyResponses.DrugInventorySummary.of(inventory));
    }

    @PostMapping("/drug-inventories")
    @Operation(summary = "재고 생성", description = "새로운 재고를 생성합니다.")
    public ResponseEntity<ApiResponse<PharmacyResponses.DrugInventorySummary>> createDrugInventory(
            @Valid @RequestBody PharmacyRequests.CreateDrugInventoryRequest request) {
        DrugInventory inventory = pharmacyFacade.createDrugInventory(request);
        return created(PharmacyResponses.DrugInventorySummary.of(inventory));
    }

    @PutMapping("/drug-inventories/{id}")
    @Operation(summary = "재고 수정", description = "기존 재고를 수정합니다.")
    public ResponseEntity<ApiResponse<PharmacyResponses.DrugInventorySummary>> updateDrugInventory(
            @Parameter(description = "재고 ID") @PathVariable Long id,
            @Valid @RequestBody PharmacyRequests.UpdateDrugInventoryRequest request) {
        DrugInventory inventory = pharmacyFacade.updateDrugInventory(id, request);
        return ok(PharmacyResponses.DrugInventorySummary.of(inventory));
    }

    @PostMapping("/drug-inventories/adjust")
    @Operation(summary = "재고 조정", description = "재고를 조정합니다.")
    public ResponseEntity<ApiResponse<PharmacyResponses.DrugInventorySummary>> adjustDrugStock(
            @Valid @RequestBody PharmacyRequests.DrugStockAdjustmentRequest request) {
        DrugInventory inventory = pharmacyFacade.adjustDrugStock(request);
        return ok(PharmacyResponses.DrugInventorySummary.of(inventory));
    }

    @DeleteMapping("/drug-inventories/{id}")
    @Operation(summary = "재고 삭제", description = "재고를 삭제합니다.")
    public ResponseEntity<ApiResponse<String>> deleteDrugInventory(
            @Parameter(description = "재고 ID") @PathVariable Long id) {
        pharmacyFacade.deleteDrugInventory(id);
        return okMessage("deleted");
    }

    // Stock management endpoints
    @GetMapping("/stock-summary")
    @Operation(summary = "재고 현황 요약", description = "전체 재고 현황을 요약하여 조회합니다.")
    public ResponseEntity<ApiResponse<List<PharmacyResponses.DrugStockSummary>>> getDrugStockSummary() {
        List<PharmacyResponses.DrugStockSummary> summaries = pharmacyFacade.getDrugStockSummary();
        return ok(summaries);
    }

    @PostMapping("/expiration-alerts")
    @Operation(summary = "유통기한 알림 조회", description = "유통기한이 임박한 약물을 조회합니다.")
    public ResponseEntity<ApiResponse<List<PharmacyResponses.DrugExpirationAlert>>> getDrugExpirationAlerts(
            @Valid @RequestBody PharmacyRequests.DrugExpirationAlertRequest request) {
        List<PharmacyResponses.DrugExpirationAlert> alerts = pharmacyFacade.getDrugExpirationAlerts(request);
        return ok(alerts);
    }

    // Drug Interaction endpoints
    @GetMapping("/drug-interactions/drug/{drugId}")
    @Operation(summary = "약물별 상호작용 조회", description = "특정 약물의 상호작용 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<PharmacyResponses.DrugInteractionSummary>>> getInteractionsByDrugId(
            @Parameter(description = "약물 ID") @PathVariable Long drugId) {
        List<DrugInteraction> interactions = pharmacyFacade.getInteractionsByDrugId(drugId);
        List<PharmacyResponses.DrugInteractionSummary> summaries = interactions.stream()
                .map(PharmacyResponses.DrugInteractionSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @PostMapping("/drug-interactions/check")
    @Operation(summary = "약물 상호작용 체크", description = "여러 약물 간의 상호작용을 체크합니다.")
    public ResponseEntity<ApiResponse<List<PharmacyResponses.DrugInteractionSummary>>> checkDrugInteractions(
            @Valid @RequestBody PharmacyRequests.CheckDrugInteractionsRequest request) {
        List<DrugInteraction> interactions = pharmacyFacade.checkInteractions(request.getDrugIds());
        List<PharmacyResponses.DrugInteractionSummary> summaries = interactions.stream()
                .map(PharmacyResponses.DrugInteractionSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/drug-interactions/{id}")
    @Operation(summary = "약물 상호작용 상세 조회", description = "특정 약물 상호작용의 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<PharmacyResponses.DrugInteractionSummary>> getDrugInteraction(
            @Parameter(description = "상호작용 ID") @PathVariable Long id) {
        DrugInteraction interaction = pharmacyFacade.getDrugInteraction(id);
        return ok(PharmacyResponses.DrugInteractionSummary.of(interaction));
    }

    @PostMapping("/drug-interactions")
    @Operation(summary = "약물 상호작용 등록", description = "새로운 약물 상호작용을 등록합니다.")
    public ResponseEntity<ApiResponse<PharmacyResponses.DrugInteractionSummary>> createDrugInteraction(
            @Valid @RequestBody PharmacyRequests.CreateDrugInteractionRequest request) {
        DrugInteraction interaction = pharmacyFacade.createDrugInteraction(request);
        return created(PharmacyResponses.DrugInteractionSummary.of(interaction));
    }

    @PutMapping("/drug-interactions/{id}")
    @Operation(summary = "약물 상호작용 수정", description = "기존 약물 상호작용을 수정합니다.")
    public ResponseEntity<ApiResponse<PharmacyResponses.DrugInteractionSummary>> updateDrugInteraction(
            @Parameter(description = "상호작용 ID") @PathVariable Long id,
            @Valid @RequestBody PharmacyRequests.UpdateDrugInteractionRequest request) {
        DrugInteraction interaction = pharmacyFacade.updateDrugInteraction(id, request);
        return ok(PharmacyResponses.DrugInteractionSummary.of(interaction));
    }

    @PostMapping("/drug-interactions/{id}/deactivate")
    @Operation(summary = "약물 상호작용 비활성화", description = "약물 상호작용을 비활성화합니다.")
    public ResponseEntity<ApiResponse<String>> deactivateDrugInteraction(
            @Parameter(description = "상호작용 ID") @PathVariable Long id) {
        pharmacyFacade.deactivateDrugInteraction(id);
        return okMessage("deactivated");
    }

    @PostMapping("/drug-interactions/{id}/activate")
    @Operation(summary = "약물 상호작용 활성화", description = "약물 상호작용을 활성화합니다.")
    public ResponseEntity<ApiResponse<String>> activateDrugInteraction(
            @Parameter(description = "상호작용 ID") @PathVariable Long id) {
        pharmacyFacade.activateDrugInteraction(id);
        return okMessage("activated");
    }

    @DeleteMapping("/drug-interactions/{id}")
    @Operation(summary = "약물 상호작용 삭제", description = "약물 상호작용을 삭제합니다.")
    public ResponseEntity<ApiResponse<String>> deleteDrugInteraction(
            @Parameter(description = "상호작용 ID") @PathVariable Long id) {
        pharmacyFacade.deleteDrugInteraction(id);
        return okMessage("deleted");
    }
}
