package com.emrsystem.domain.billing.service;

import com.emrsystem.domain.appointment.entity.Appointment;
import com.emrsystem.domain.appointment.store.AppointmentStore;
import com.emrsystem.domain.billing.entity.Billing;
import com.emrsystem.domain.billing.entity.ProcedureCode;
import com.emrsystem.domain.billing.entity.Receipt;
import com.emrsystem.domain.billing.request.BillingRequests;
import com.emrsystem.domain.billing.response.BillingResponses;
import com.emrsystem.domain.billing.store.BillingStore;
import com.emrsystem.domain.patient.entity.Patient;
import com.emrsystem.domain.patient.store.PatientStore;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BillingService {

    private final BillingStore billingStore;
    private final PatientStore patientStore;
    private final AppointmentStore appointmentStore;

    // Procedure Code operations
    public List<ProcedureCode> getProcedureCodes() {
        return billingStore.findAllProcedureCodes();
    }

    public ProcedureCode getProcedureCode(Long id) {
        return billingStore.findProcedureCodeById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.PROCEDURE_CODE_NOT_FOUND));
    }

    @Transactional
    public ProcedureCode createProcedureCode(BillingRequests.CreateProcedureCodeRequest request) {
        if (billingStore.existsProcedureCodeByCode(request.getCode())) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION, "Procedure code already exists: " + request.getCode());
        }

        ProcedureCode procedureCode = ProcedureCode.create(request.getCode(), request.getName(), "GENERAL", request.getPrice(), "EA", null);
        return billingStore.saveProcedureCode(procedureCode);
    }

    @Transactional
    public ProcedureCode updateProcedureCode(Long id, BillingRequests.UpdateProcedureCodeRequest request) {
        ProcedureCode procedureCode = getProcedureCode(id);
        procedureCode.update(request.getName(), "GENERAL", request.getPrice(), "EA", null);
        return billingStore.saveProcedureCode(procedureCode);
    }

    @Transactional
    public void deleteProcedureCode(Long id) {
        if (!billingStore.findProcedureCodeById(id).isPresent()) {
            throw new CommonException(ErrorCode.PROCEDURE_CODE_NOT_FOUND);
        }
        billingStore.deleteProcedureCodeById(id);
    }

    // Billing operations
    public Page<Billing> getBillingsByPatient(Long patientId, Pageable pageable) {
        return billingStore.findBillingsByPatient(patientId, pageable);
    }

    // 단순화: 환자 기준 목록만 유지

    public Billing getBilling(Long id) {
        return billingStore.findBillingById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.BILLING_NOT_FOUND));
    }

    @Transactional
    public Billing createBilling(BillingRequests.CreateBillingRequest request) {
        Patient patient = patientStore.findById(request.getPatientId())
                .orElseThrow(() -> new CommonException(ErrorCode.PATIENT_NOT_FOUND));

        Appointment appointment = null;
        if (request.getAppointmentId() != null) {
            appointment = appointmentStore.findById(request.getAppointmentId())
                    .orElseThrow(() -> new CommonException(ErrorCode.APPOINTMENT_NOT_FOUND));
        }

        // Minimal billing: choose a default procedure code (first) or throw if none
        ProcedureCode procedureCode = billingStore.findAllProcedureCodes().stream().findFirst()
                .orElseThrow(() -> new CommonException(ErrorCode.PROCEDURE_CODE_NOT_FOUND));
        Billing billing = Billing.create(appointment, patient, procedureCode,
                "OUTPATIENT", request.getTotalAmount(), request.getInsuranceAmount(), request.getPatientAmount(), "PENDING");

        return billingStore.saveBilling(billing);
    }

    @Transactional
    public Billing updateBilling(Long id, BillingRequests.UpdateBillingRequest request) {
        Billing billing = getBilling(id);
        // No update API in entity; recreate key amounts
        billing.cancel();
        billing.pay(request.getPatientAmount(), billing.getPaymentMethod());
        return billingStore.saveBilling(billing);
    }

    @Transactional
    public Billing payBilling(Long id) {
        Billing billing = getBilling(id);
        billing.pay(billing.getPatientAmount(), "CASH");
        return billingStore.saveBilling(billing);
    }

    @Transactional
    public Billing cancelBilling(Long id) {
        Billing billing = getBilling(id);
        billing.cancel();
        return billingStore.saveBilling(billing);
    }

    // Receipt operations
    public Receipt getReceipt(Long id) {
        return billingStore.findReceiptById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.RECEIPT_NOT_FOUND));
    }

    public Receipt getReceiptByBillingId(Long billingId) {
        return billingStore.findReceiptByBillingId(billingId)
                .orElseThrow(() -> new CommonException(ErrorCode.RECEIPT_NOT_FOUND));
    }

    @Transactional
    public Receipt createReceipt(BillingRequests.CreateReceiptRequest request) {
        Billing billing = getBilling(request.getBillingId());

        if (billingStore.findReceiptByBillingId(request.getBillingId()).isPresent()) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION, "Receipt already exists for billing: " + request.getBillingId());
        }

        Receipt receipt = Receipt.create(billing.getPatient(),
                billing.getBillingId().toString(), billing.getTotalAmount(), billing.getInsuranceAmount(), billing.getCopayAmount(), request.getAmountPaid(), request.getPaymentMethod());
        receipt.setBilling(billing);

        return billingStore.saveReceipt(receipt);
    }

    @Transactional
    public Receipt cancelReceipt(Long id) {
        Receipt receipt = getReceipt(id);
        receipt.cancel();
        return billingStore.saveReceipt(receipt);
    }

    // Billing calculation
    public BillingResponses.BillingCalculationResult calculateBilling(BillingRequests.CalculateBillingRequest request) {
        // 간단한 계산 로직 (실제로는 복잡한 보험 계산 로직이 필요)
        BigDecimal totalAmount = new BigDecimal("100000"); // 예시 금액
        BigDecimal insuranceAmount = totalAmount.multiply(new BigDecimal("0.7")); // 70% 보험 적용
        BigDecimal patientAmount = totalAmount.subtract(insuranceAmount); // 30% 본인부담

        String calculationDetails = String.format("총 금액: %s원, 보험 적용: %s원, 본인부담: %s원", 
                totalAmount, insuranceAmount, patientAmount);

        return new BillingResponses.BillingCalculationResult(totalAmount, insuranceAmount, patientAmount, calculationDetails);
    }
}
