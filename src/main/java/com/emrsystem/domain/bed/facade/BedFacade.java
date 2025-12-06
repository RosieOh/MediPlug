package com.emrsystem.domain.bed.facade;

import com.emrsystem.domain.bed.entity.Admission;
import com.emrsystem.domain.bed.entity.Bed;
import com.emrsystem.domain.bed.request.BedRequests;
import com.emrsystem.domain.bed.response.BedResponses;
import com.emrsystem.domain.bed.service.BedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BedFacade {

    private final BedService bedService;

    // Bed operations
    public List<Bed> getBedsByRoom(Long roomId) {
        return bedService.getBedsByRoom(roomId);
    }

    public List<Bed> getBedsByStatus(String status) {
        return bedService.getBedsByStatus(status);
    }

    public List<Bed> getBedsByBedType(String bedType) {
        return bedService.getBedsByBedType(bedType);
    }

    public List<Bed> getAvailableBeds() {
        return bedService.getAvailableBeds();
    }

    public List<Bed> getAvailableBedsByBedType(String bedType) {
        return bedService.getAvailableBedsByBedType(bedType);
    }

    public List<Bed> getAvailableBedsByRoom(Long roomId) {
        return bedService.getAvailableBedsByRoom(roomId);
    }

    public Bed getBed(Long id) {
        return bedService.getBed(id);
    }

    public Bed createBed(BedRequests.CreateBedRequest request) {
        return bedService.createBed(request);
    }

    public Bed updateBed(Long id, BedRequests.UpdateBedRequest request) {
        return bedService.updateBed(id, request);
    }

    public Bed setBedMaintenance(Long id) {
        return bedService.setBedMaintenance(id);
    }

    public Bed setBedAvailable(Long id) {
        return bedService.setBedAvailable(id);
    }

    public void deleteBed(Long id) {
        bedService.deleteBed(id);
    }

    // Admission operations
    public Page<Admission> getAdmissionsByPatient(Long patientId, Pageable pageable) {
        return bedService.getAdmissionsByPatient(patientId, pageable);
    }

    public Page<Admission> getAdmissionsByBed(Long bedId, Pageable pageable) {
        return bedService.getAdmissionsByBed(bedId, pageable);
    }

    public Page<Admission> getAdmissionsByStatus(String status, Pageable pageable) {
        return bedService.getAdmissionsByStatus(status, pageable);
    }

    public Admission getAdmission(Long id) {
        return bedService.getAdmission(id);
    }

    public Admission createAdmission(BedRequests.CreateAdmissionRequest request) {
        return bedService.createAdmission(request);
    }

    public Admission updateAdmission(Long id, BedRequests.UpdateAdmissionRequest request) {
        return bedService.updateAdmission(id, request);
    }

    public Admission dischargePatient(Long id, BedRequests.DischargeRequest request) {
        return bedService.dischargePatient(id, request);
    }

    public Admission transferPatient(Long id, BedRequests.TransferRequest request) {
        return bedService.transferPatient(id, request);
    }

    // Bed availability operations
    public List<BedResponses.BedAvailabilitySummary> getBedAvailability(BedRequests.BedAvailabilityRequest request) {
        return bedService.getBedAvailability(request);
    }

    public List<BedResponses.BedOccupancySummary> getBedOccupancySummary() {
        return bedService.getBedOccupancySummary();
    }
}
