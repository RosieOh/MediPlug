package com.emrsystem.domain.billing.store;

import com.emrsystem.domain.billing.entity.Billing;
import com.emrsystem.domain.billing.entity.ProcedureCode;
import com.emrsystem.domain.billing.entity.Receipt;
import com.emrsystem.domain.billing.repository.BillingRepository;
import com.emrsystem.domain.billing.repository.ProcedureCodeRepository;
import com.emrsystem.domain.billing.repository.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BillingStore {

    private final ProcedureCodeRepository procedureCodeRepository;
    private final BillingRepository billingRepository;
    private final ReceiptRepository receiptRepository;

    // Procedure Code operations
    public ProcedureCode saveProcedureCode(ProcedureCode procedureCode) {
        return procedureCodeRepository.save(procedureCode);
    }

    public Optional<ProcedureCode> findProcedureCodeById(Long id) {
        return procedureCodeRepository.findById(id);
    }

    public Optional<ProcedureCode> findProcedureCodeByCode(String code) {
        return procedureCodeRepository.findByCode(code);
    }

    public List<ProcedureCode> findAllProcedureCodes() {
        return procedureCodeRepository.findAll();
    }

    public boolean existsProcedureCodeByCode(String code) {
        return procedureCodeRepository.existsByCode(code);
    }

    public void deleteProcedureCodeById(Long id) {
        procedureCodeRepository.deleteById(id);
    }

    // Billing operations
    public Billing saveBilling(Billing billing) {
        return billingRepository.save(billing);
    }

    public Optional<Billing> findBillingById(Long id) {
        return billingRepository.findById(id);
    }

    public Page<Billing> findBillingsByPatient(Long patientId, Pageable pageable) {
        return billingRepository.findByPatient_PatientId(patientId, pageable);
    }

    // 단순화: 환자 기준 목록만 유지(추가 기준 필요 시 이후 확장)

    // Receipt operations
    public Receipt saveReceipt(Receipt receipt) {
        return receiptRepository.save(receipt);
    }

    public Optional<Receipt> findReceiptById(Long id) {
        return receiptRepository.findById(id);
    }

    public Optional<Receipt> findReceiptByBillingId(Long billingId) {
        return receiptRepository.findByBilling_BillingId(billingId);
    }

    public Page<Receipt> findReceiptsByPaymentMethod(String paymentMethod, Pageable pageable) {
        return receiptRepository.findByPaymentMethod(paymentMethod, pageable);
    }

    public Page<Receipt> findReceiptsByStatus(String status, Pageable pageable) {
        return receiptRepository.findByStatus(status, pageable);
    }

    public Page<Receipt> findReceiptsByReceiptDateBetween(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate, Pageable pageable) {
        return receiptRepository.findByReceiptDateBetween(startDate, endDate, pageable);
    }
}
