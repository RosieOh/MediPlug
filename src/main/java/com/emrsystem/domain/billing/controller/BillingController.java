package com.emrsystem.domain.billing.controller;

import com.emrsystem.domain.billing.entity.Billing;
import com.emrsystem.domain.billing.entity.ProcedureCode;
import com.emrsystem.domain.billing.entity.Receipt;
import com.emrsystem.domain.billing.facade.BillingFacade;
import com.emrsystem.domain.billing.request.BillingRequests;
import com.emrsystem.domain.billing.response.BillingResponses;
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

@Tag(name = "Billing", description = "청구/정산 관리 API")
@RestController
@RequestMapping("/api/billing")
@RequiredArgsConstructor
public class BillingController extends BaseController {

    private final BillingFacade billingFacade;

    // Procedure Code endpoints
    @GetMapping("/procedure-codes")
    @Operation(summary = "행위코드 목록 조회", description = "행위코드 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<BillingResponses.ProcedureCodeSummary>>> getProcedureCodes() {
        List<ProcedureCode> procedureCodes = billingFacade.getProcedureCodes();
        List<BillingResponses.ProcedureCodeSummary> summaries = procedureCodes.stream()
                .map(BillingResponses.ProcedureCodeSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @PostMapping("/procedure-codes")
    @Operation(summary = "행위코드 생성", description = "새로운 행위코드를 생성합니다.")
    public ResponseEntity<ApiResponse<BillingResponses.ProcedureCodeSummary>> createProcedureCode(
            @Valid @RequestBody BillingRequests.CreateProcedureCodeRequest request) {
        ProcedureCode procedureCode = billingFacade.createProcedureCode(request);
        return created(BillingResponses.ProcedureCodeSummary.of(procedureCode));
    }

    @PutMapping("/procedure-codes/{id}")
    @Operation(summary = "행위코드 수정", description = "기존 행위코드를 수정합니다.")
    public ResponseEntity<ApiResponse<BillingResponses.ProcedureCodeSummary>> updateProcedureCode(
            @Parameter(description = "행위코드 ID") @PathVariable Long id,
            @Valid @RequestBody BillingRequests.UpdateProcedureCodeRequest request) {
        ProcedureCode procedureCode = billingFacade.updateProcedureCode(id, request);
        return ok(BillingResponses.ProcedureCodeSummary.of(procedureCode));
    }

    @DeleteMapping("/procedure-codes/{id}")
    @Operation(summary = "행위코드 삭제", description = "행위코드를 삭제합니다.")
    public ResponseEntity<ApiResponse<String>> deleteProcedureCode(
            @Parameter(description = "행위코드 ID") @PathVariable Long id) {
        billingFacade.deleteProcedureCode(id);
        return okMessage("deleted");
    }

    // Billing endpoints
    @GetMapping("/billings/patient/{patientId}")
    @Operation(summary = "환자별 청구 내역 조회", description = "특정 환자의 청구 내역을 조회합니다.")
    public ResponseEntity<ApiResponse<PageResponse<BillingResponses.BillingSummary>>> getBillingsByPatient(
            @Parameter(description = "환자 ID") @PathVariable Long patientId,
            @Parameter(description = "페이지 정보") @PageableDefault(size = 20) Pageable pageable) {
        Page<Billing> billings = billingFacade.getBillingsByPatient(patientId, pageable);
        Page<BillingResponses.BillingSummary> summaries = billings.map(BillingResponses.BillingSummary::of);
        return ok(PageResponse.of(summaries));
    }


    @GetMapping("/billings/{id}")
    @Operation(summary = "청구 내역 상세 조회", description = "특정 청구 내역의 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<BillingResponses.BillingSummary>> getBilling(
            @Parameter(description = "청구 ID") @PathVariable Long id) {
        Billing billing = billingFacade.getBilling(id);
        return ok(BillingResponses.BillingSummary.of(billing));
    }

    @PostMapping("/billings")
    @Operation(summary = "청구 내역 생성", description = "새로운 청구 내역을 생성합니다.")
    public ResponseEntity<ApiResponse<BillingResponses.BillingSummary>> createBilling(
            @Valid @RequestBody BillingRequests.CreateBillingRequest request) {
        Billing billing = billingFacade.createBilling(request);
        return created(BillingResponses.BillingSummary.of(billing));
    }

    @PutMapping("/billings/{id}")
    @Operation(summary = "청구 내역 수정", description = "기존 청구 내역을 수정합니다.")
    public ResponseEntity<ApiResponse<BillingResponses.BillingSummary>> updateBilling(
            @Parameter(description = "청구 ID") @PathVariable Long id,
            @Valid @RequestBody BillingRequests.UpdateBillingRequest request) {
        Billing billing = billingFacade.updateBilling(id, request);
        return ok(BillingResponses.BillingSummary.of(billing));
    }

    @PostMapping("/billings/{id}/pay")
    @Operation(summary = "청구 결제", description = "청구 내역을 결제 완료 상태로 변경합니다.")
    public ResponseEntity<ApiResponse<BillingResponses.BillingSummary>> payBilling(
            @Parameter(description = "청구 ID") @PathVariable Long id) {
        Billing billing = billingFacade.payBilling(id);
        return ok(BillingResponses.BillingSummary.of(billing));
    }

    @PostMapping("/billings/{id}/cancel")
    @Operation(summary = "청구 취소", description = "청구 내역을 취소 상태로 변경합니다.")
    public ResponseEntity<ApiResponse<BillingResponses.BillingSummary>> cancelBilling(
            @Parameter(description = "청구 ID") @PathVariable Long id) {
        Billing billing = billingFacade.cancelBilling(id);
        return ok(BillingResponses.BillingSummary.of(billing));
    }

    // Receipt endpoints
    @GetMapping("/receipts/{id}")
    @Operation(summary = "영수증 상세 조회", description = "특정 영수증의 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<BillingResponses.ReceiptSummary>> getReceipt(
            @Parameter(description = "영수증 ID") @PathVariable Long id) {
        Receipt receipt = billingFacade.getReceipt(id);
        return ok(BillingResponses.ReceiptSummary.of(receipt));
    }

    @GetMapping("/receipts/billing/{billingId}")
    @Operation(summary = "청구별 영수증 조회", description = "특정 청구의 영수증을 조회합니다.")
    public ResponseEntity<ApiResponse<BillingResponses.ReceiptSummary>> getReceiptByBillingId(
            @Parameter(description = "청구 ID") @PathVariable Long billingId) {
        Receipt receipt = billingFacade.getReceiptByBillingId(billingId);
        return ok(BillingResponses.ReceiptSummary.of(receipt));
    }

    @PostMapping("/receipts")
    @Operation(summary = "영수증 생성", description = "새로운 영수증을 생성합니다.")
    public ResponseEntity<ApiResponse<BillingResponses.ReceiptSummary>> createReceipt(
            @Valid @RequestBody BillingRequests.CreateReceiptRequest request) {
        Receipt receipt = billingFacade.createReceipt(request);
        return created(BillingResponses.ReceiptSummary.of(receipt));
    }

    @PostMapping("/receipts/{id}/cancel")
    @Operation(summary = "영수증 취소", description = "영수증을 취소 상태로 변경합니다.")
    public ResponseEntity<ApiResponse<BillingResponses.ReceiptSummary>> cancelReceipt(
            @Parameter(description = "영수증 ID") @PathVariable Long id) {
        Receipt receipt = billingFacade.cancelReceipt(id);
        return ok(BillingResponses.ReceiptSummary.of(receipt));
    }

    // Billing calculation endpoint
    @PostMapping("/calculate")
    @Operation(summary = "청구 금액 계산", description = "청구 금액을 계산합니다.")
    public ResponseEntity<ApiResponse<BillingResponses.BillingCalculationResult>> calculateBilling(
            @Valid @RequestBody BillingRequests.CalculateBillingRequest request) {
        BillingResponses.BillingCalculationResult result = billingFacade.calculateBilling(request);
        return ok(result);
    }
}
