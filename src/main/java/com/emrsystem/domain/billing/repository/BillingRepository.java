package com.emrsystem.domain.billing.repository;

import com.emrsystem.domain.billing.entity.Billing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public interface BillingRepository extends JpaRepository<Billing, Long> {
    Page<Billing> findByPatient_PatientId(Long patientId, Pageable pageable);
    Page<Billing> findByAppointment_AppointmentId(Long appointmentId, Pageable pageable);
    Page<Billing> findByStatus(String status, Pageable pageable);
    Page<Billing> findByBillingType(String billingType, Pageable pageable);
    Page<Billing> findByBillingDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    // Statistics queries
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM Billing b WHERE b.createdAt BETWEEN :start AND :end")
    Optional<BigDecimal> sumTotalAmountByCreatedAtBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT COALESCE(SUM(b.insuranceAmount), 0) FROM Billing b WHERE b.createdAt BETWEEN :start AND :end")
    Optional<BigDecimal> sumInsuranceAmountByCreatedAtBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
    
    @Query("SELECT COALESCE(SUM(b.copayAmount), 0) FROM Billing b WHERE b.createdAt BETWEEN :start AND :end")
    Optional<BigDecimal> sumCopayAmountByCreatedAtBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
