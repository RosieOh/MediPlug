package com.emrsystem.domain.pharmacy.repository;

import com.emrsystem.domain.pharmacy.entity.DrugMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DrugMasterRepository extends JpaRepository<DrugMaster, Long> {
    Optional<DrugMaster> findByDrugCode(String drugCode);
    Page<DrugMaster> findByDrugNameContainingIgnoreCase(String drugName, Pageable pageable);
    boolean existsByDrugCode(String drugCode);
}
