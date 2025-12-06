package com.emrsystem.domain.inventory.repository;

import com.emrsystem.domain.inventory.entity.PurchaseOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    Optional<PurchaseOrder> findByOrderNumber(String orderNumber);
    List<PurchaseOrder> findByStatus(String status);
    List<PurchaseOrder> findByDrugMaster_DrugMasterId(Long drugMasterId);
    List<PurchaseOrder> findBySupplier_SupplierId(Long supplierId);
    List<PurchaseOrder> findByExpectedDeliveryDateBetween(LocalDate startDate, LocalDate endDate);
    Page<PurchaseOrder> findByStatus(String status, Pageable pageable);
    boolean existsByOrderNumber(String orderNumber);
}

