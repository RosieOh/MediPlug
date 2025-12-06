package com.emrsystem.domain.billing.repository;

import com.emrsystem.domain.billing.entity.Receipt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    Optional<Receipt> findByBilling_BillingId(Long billingId);
    Page<Receipt> findByPaymentMethod(String paymentMethod, Pageable pageable);
    Page<Receipt> findByStatus(String status, Pageable pageable);
    Page<Receipt> findByReceiptDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
