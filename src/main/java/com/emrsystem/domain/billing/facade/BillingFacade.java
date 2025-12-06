package com.emrsystem.domain.billing.facade;

import com.emrsystem.domain.billing.entity.Billing;
import com.emrsystem.domain.billing.entity.ProcedureCode;
import com.emrsystem.domain.billing.entity.Receipt;
import com.emrsystem.domain.billing.request.BillingRequests;
import com.emrsystem.domain.billing.response.BillingResponses;
import com.emrsystem.domain.billing.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BillingFacade {

    private final BillingService billingService;

    // Procedure Code operations
    public List<ProcedureCode> getProcedureCodes() {
        return billingService.getProcedureCodes();
    }

    public ProcedureCode getProcedureCode(Long id) {
        return billingService.getProcedureCode(id);
    }

    public ProcedureCode createProcedureCode(BillingRequests.CreateProcedureCodeRequest request) {
        return billingService.createProcedureCode(request);
    }

    public ProcedureCode updateProcedureCode(Long id, BillingRequests.UpdateProcedureCodeRequest request) {
        return billingService.updateProcedureCode(id, request);
    }

    public void deleteProcedureCode(Long id) {
        billingService.deleteProcedureCode(id);
    }

    // Billing operations
    public Page<Billing> getBillingsByPatient(Long patientId, Pageable pageable) {
        return billingService.getBillingsByPatient(patientId, pageable);
    }

    // 단순화: 환자 기준 목록만 유지

    public Billing getBilling(Long id) {
        return billingService.getBilling(id);
    }

    public Billing createBilling(BillingRequests.CreateBillingRequest request) {
        return billingService.createBilling(request);
    }

    public Billing updateBilling(Long id, BillingRequests.UpdateBillingRequest request) {
        return billingService.updateBilling(id, request);
    }

    public Billing payBilling(Long id) {
        return billingService.payBilling(id);
    }

    public Billing cancelBilling(Long id) {
        return billingService.cancelBilling(id);
    }

    // Receipt operations
    public Receipt getReceipt(Long id) {
        return billingService.getReceipt(id);
    }

    public Receipt getReceiptByBillingId(Long billingId) {
        return billingService.getReceiptByBillingId(billingId);
    }

    public Receipt createReceipt(BillingRequests.CreateReceiptRequest request) {
        return billingService.createReceipt(request);
    }

    public Receipt cancelReceipt(Long id) {
        return billingService.cancelReceipt(id);
    }

    // Billing calculation
    public BillingResponses.BillingCalculationResult calculateBilling(BillingRequests.CalculateBillingRequest request) {
        return billingService.calculateBilling(request);
    }
}
