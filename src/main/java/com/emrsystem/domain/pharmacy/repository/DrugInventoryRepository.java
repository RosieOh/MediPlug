package com.emrsystem.domain.pharmacy.repository;

import com.emrsystem.domain.pharmacy.entity.DrugInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DrugInventoryRepository extends JpaRepository<DrugInventory, Long> {
    List<DrugInventory> findByDrugMaster_DrugMasterId(Long drugMasterId);
    List<DrugInventory> findByLocation(String location);
    List<DrugInventory> findByExpirationDateBefore(LocalDate date);
    List<DrugInventory> findByExpirationDateBetween(LocalDate startDate, LocalDate endDate);
    List<DrugInventory> findByQuantityLessThan(Integer quantity);
    Optional<DrugInventory> findByDrugMaster_DrugMasterIdAndBatchNumber(Long drugMasterId, String batchNumber);
    boolean existsByDrugMaster_DrugMasterIdAndBatchNumber(Long drugMasterId, String batchNumber);
    
    // Statistics queries
    long countByExpirationDateBefore(LocalDate date);
    long countByExpirationDateBetween(LocalDate startDate, LocalDate endDate);
    long countByQuantityLessThan(Integer quantity);
}
