package com.emrsystem.domain.pharmacy.facade;

import com.emrsystem.domain.pharmacy.entity.DrugInteraction;
import com.emrsystem.domain.pharmacy.entity.DrugInventory;
import com.emrsystem.domain.pharmacy.entity.DrugMaster;
import com.emrsystem.domain.pharmacy.request.PharmacyRequests;
import com.emrsystem.domain.pharmacy.response.PharmacyResponses;
import com.emrsystem.domain.pharmacy.service.PharmacyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PharmacyFacade {

    private final PharmacyService pharmacyService;

    // Drug Master operations
    public List<DrugMaster> getDrugMasters() {
        return pharmacyService.getDrugMasters();
    }

    public Page<DrugMaster> searchDrugMastersByName(String name, Pageable pageable) {
        return pharmacyService.searchDrugMastersByName(name, pageable);
    }

    public DrugMaster getDrugMaster(Long id) {
        return pharmacyService.getDrugMaster(id);
    }

    public DrugMaster createDrugMaster(PharmacyRequests.CreateDrugMasterRequest request) {
        return pharmacyService.createDrugMaster(request);
    }

    public DrugMaster updateDrugMaster(Long id, PharmacyRequests.UpdateDrugMasterRequest request) {
        return pharmacyService.updateDrugMaster(id, request);
    }

    public void deleteDrugMaster(Long id) {
        pharmacyService.deleteDrugMaster(id);
    }

    // Drug Inventory operations
    public List<DrugInventory> getDrugInventoriesByDrugMaster(Long drugMasterId) {
        return pharmacyService.getDrugInventoriesByDrugMaster(drugMasterId);
    }

    public List<DrugInventory> getDrugInventoriesByLocation(String location) {
        return pharmacyService.getDrugInventoriesByLocation(location);
    }

    public DrugInventory getDrugInventory(Long id) {
        return pharmacyService.getDrugInventory(id);
    }

    public DrugInventory createDrugInventory(PharmacyRequests.CreateDrugInventoryRequest request) {
        return pharmacyService.createDrugInventory(request);
    }

    public DrugInventory updateDrugInventory(Long id, PharmacyRequests.UpdateDrugInventoryRequest request) {
        return pharmacyService.updateDrugInventory(id, request);
    }

    public DrugInventory adjustDrugStock(PharmacyRequests.DrugStockAdjustmentRequest request) {
        return pharmacyService.adjustDrugStock(request);
    }

    public void deleteDrugInventory(Long id) {
        pharmacyService.deleteDrugInventory(id);
    }

    // Stock management operations
    public List<PharmacyResponses.DrugStockSummary> getDrugStockSummary() {
        return pharmacyService.getDrugStockSummary();
    }

    public List<PharmacyResponses.DrugExpirationAlert> getDrugExpirationAlerts(PharmacyRequests.DrugExpirationAlertRequest request) {
        return pharmacyService.getDrugExpirationAlerts(request);
    }

    // Drug Interaction operations
    public DrugInteraction getDrugInteraction(Long id) {
        return pharmacyService.getDrugInteraction(id);
    }

    public List<DrugInteraction> getInteractionsByDrugId(Long drugId) {
        return pharmacyService.getInteractionsByDrugId(drugId);
    }

    public List<DrugInteraction> checkInteractions(List<Long> drugIds) {
        return pharmacyService.checkInteractions(drugIds);
    }

    public DrugInteraction createDrugInteraction(PharmacyRequests.CreateDrugInteractionRequest request) {
        return pharmacyService.createDrugInteraction(request);
    }

    public DrugInteraction updateDrugInteraction(Long id, PharmacyRequests.UpdateDrugInteractionRequest request) {
        return pharmacyService.updateDrugInteraction(id, request);
    }

    public void deactivateDrugInteraction(Long id) {
        pharmacyService.deactivateDrugInteraction(id);
    }

    public void activateDrugInteraction(Long id) {
        pharmacyService.activateDrugInteraction(id);
    }

    public void deleteDrugInteraction(Long id) {
        pharmacyService.deleteDrugInteraction(id);
    }
}
