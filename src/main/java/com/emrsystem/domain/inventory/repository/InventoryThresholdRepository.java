package com.emrsystem.domain.inventory.repository;

import com.emrsystem.domain.inventory.entity.InventoryThreshold;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryThresholdRepository extends JpaRepository<InventoryThreshold, Long> {
    Optional<InventoryThreshold> findByDrugMaster_DrugMasterId(Long drugMasterId);
    List<InventoryThreshold> findByAutoReorderEnabled(boolean enabled);
    boolean existsByDrugMaster_DrugMasterId(Long drugMasterId);
}

