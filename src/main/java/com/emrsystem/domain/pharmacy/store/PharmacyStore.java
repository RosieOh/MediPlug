package com.emrsystem.domain.pharmacy.store;

import com.emrsystem.domain.pharmacy.entity.DrugInteraction;
import com.emrsystem.domain.pharmacy.entity.DrugInventory;
import com.emrsystem.domain.pharmacy.entity.DrugMaster;
import com.emrsystem.domain.pharmacy.repository.DrugInteractionRepository;
import com.emrsystem.domain.pharmacy.repository.DrugInventoryRepository;
import com.emrsystem.domain.pharmacy.repository.DrugMasterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PharmacyStore {

    private final DrugMasterRepository drugMasterRepository;
    private final DrugInventoryRepository drugInventoryRepository;
    private final DrugInteractionRepository drugInteractionRepository;

    // Drug Master operations
    public DrugMaster saveDrugMaster(DrugMaster drugMaster) {
        return drugMasterRepository.save(drugMaster);
    }

    public Optional<DrugMaster> findDrugMasterById(Long id) {
        return drugMasterRepository.findById(id);
    }

    public Optional<DrugMaster> findDrugMasterByCode(String code) {
        return drugMasterRepository.findByDrugCode(code);
    }

    public List<DrugMaster> findAllDrugMasters() {
        return drugMasterRepository.findAll();
    }

    public Page<DrugMaster> findDrugMastersByNameContaining(String name, Pageable pageable) {
        return drugMasterRepository.findByDrugNameContainingIgnoreCase(name, pageable);
    }

    public boolean existsDrugMasterByCode(String code) {
        return drugMasterRepository.existsByDrugCode(code);
    }

    public void deleteDrugMasterById(Long id) {
        drugMasterRepository.deleteById(id);
    }

    // Drug Inventory operations
    public DrugInventory saveDrugInventory(DrugInventory drugInventory) {
        return drugInventoryRepository.save(drugInventory);
    }

    public Optional<DrugInventory> findDrugInventoryById(Long id) {
        return drugInventoryRepository.findById(id);
    }

    public List<DrugInventory> findDrugInventoriesByDrugMaster(Long drugMasterId) {
        return drugInventoryRepository.findByDrugMaster_DrugMasterId(drugMasterId);
    }

    public List<DrugInventory> findDrugInventoriesByLocation(String location) {
        return drugInventoryRepository.findByLocation(location);
    }

    // status 기반 조회 제거(스키마 미지원)

    public List<DrugInventory> findDrugInventoriesByExpirationDateBefore(LocalDate date) {
        return drugInventoryRepository.findByExpirationDateBefore(date);
    }

    public List<DrugInventory> findDrugInventoriesByExpirationDateBetween(LocalDate startDate, LocalDate endDate) {
        return drugInventoryRepository.findByExpirationDateBetween(startDate, endDate);
    }

    public List<DrugInventory> findDrugInventoriesByQuantityLessThan(Integer quantity) {
        return drugInventoryRepository.findByQuantityLessThan(quantity);
    }

    public Optional<DrugInventory> findDrugInventoryByDrugMasterAndBatchNumber(Long drugMasterId, String batchNumber) {
        return drugInventoryRepository.findByDrugMaster_DrugMasterIdAndBatchNumber(drugMasterId, batchNumber);
    }

    public boolean existsDrugInventoryByDrugMasterAndBatchNumber(Long drugMasterId, String batchNumber) {
        return drugInventoryRepository.existsByDrugMaster_DrugMasterIdAndBatchNumber(drugMasterId, batchNumber);
    }

    public void deleteDrugInventoryById(Long id) {
        drugInventoryRepository.deleteById(id);
    }

    // Drug Interaction operations
    public DrugInteraction saveDrugInteraction(DrugInteraction interaction) {
        return drugInteractionRepository.save(interaction);
    }

    public Optional<DrugInteraction> findDrugInteractionById(Long id) {
        return drugInteractionRepository.findById(id);
    }

    public Optional<DrugInteraction> findInteractionBetweenDrugs(Long drug1Id, Long drug2Id) {
        return drugInteractionRepository.findInteractionBetweenDrugs(drug1Id, drug2Id);
    }

    public List<DrugInteraction> findInteractionsByDrugId(Long drugId) {
        return drugInteractionRepository.findByDrugId(drugId);
    }

    public List<DrugInteraction> findInteractionsBetweenDrugs(Long drug1Id, Long drug2Id) {
        return drugInteractionRepository.findInteractionsBetweenDrugs(drug1Id, drug2Id);
    }

    public List<DrugInteraction> findInteractionsBySeverity(String severity) {
        return drugInteractionRepository.findBySeverityAndActiveTrue(severity);
    }

    public void deleteDrugInteractionById(Long id) {
        drugInteractionRepository.deleteById(id);
    }
}
