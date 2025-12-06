package com.emrsystem.domain.bed.service;

import com.emrsystem.domain.bed.entity.Admission;
import com.emrsystem.domain.bed.entity.Bed;
import com.emrsystem.domain.bed.request.BedRequests;
import com.emrsystem.domain.bed.response.BedResponses;
import com.emrsystem.domain.bed.store.BedStore;
import com.emrsystem.domain.patient.entity.Patient;
import com.emrsystem.domain.patient.store.PatientStore;
import com.emrsystem.domain.resource.entity.Room;
import com.emrsystem.domain.resource.store.ResourceStore;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BedService {

    private final BedStore bedStore;
    private final PatientStore patientStore;
    private final ResourceStore resourceStore;

    // Bed operations
    public List<Bed> getBedsByRoom(Long roomId) {
        return bedStore.findBedsByRoom(roomId);
    }

    public List<Bed> getBedsByStatus(String status) {
        return bedStore.findBedsByStatus(status);
    }

    public List<Bed> getBedsByBedType(String bedType) {
        return bedStore.findBedsByBedType(bedType);
    }

    public List<Bed> getAvailableBeds() {
        return bedStore.findAvailableBeds();
    }

    public List<Bed> getAvailableBedsByBedType(String bedType) {
        return bedStore.findAvailableBedsByBedType(bedType);
    }

    public List<Bed> getAvailableBedsByRoom(Long roomId) {
        return bedStore.findAvailableBedsByRoom(roomId);
    }

    public Bed getBed(Long id) {
        return bedStore.findBedById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.BED_NOT_FOUND));
    }

    @Transactional
    public Bed createBed(BedRequests.CreateBedRequest request) {
        Room room = resourceStore.findRoomById(request.getRoomId())
                .orElseThrow(() -> new CommonException(ErrorCode.ROOM_NOT_FOUND));

        if (bedStore.existsBedByRoomAndBedNumber(request.getRoomId(), request.getBedNumber())) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION, "Bed number already exists in room: " + request.getBedNumber());
        }

        Bed bed = Bed.create(room, request.getBedNumber(), request.getBedType(), null);
        return bedStore.saveBed(bed);
    }

    @Transactional
    public Bed updateBed(Long id, BedRequests.UpdateBedRequest request) {
        Bed bed = getBed(id);
        bed.update(request.getBedType(), bed.getDailyRate(), request.getDescription());
        return bedStore.saveBed(bed);
    }

    @Transactional
    public Bed setBedMaintenance(Long id) {
        Bed bed = getBed(id);
        bed.setMaintenance();
        return bedStore.saveBed(bed);
    }

    @Transactional
    public Bed setBedAvailable(Long id) {
        Bed bed = getBed(id);
        bed.setAvailable();
        return bedStore.saveBed(bed);
    }

    @Transactional
    public void deleteBed(Long id) {
        if (!bedStore.findBedById(id).isPresent()) {
            throw new CommonException(ErrorCode.BED_NOT_FOUND);
        }
        bedStore.deleteBedById(id);
    }

    // Admission operations
    public Page<Admission> getAdmissionsByPatient(Long patientId, Pageable pageable) {
        return bedStore.findAdmissionsByPatient(patientId, pageable);
    }

    public Page<Admission> getAdmissionsByBed(Long bedId, Pageable pageable) {
        return bedStore.findAdmissionsByBed(bedId, pageable);
    }

    public Page<Admission> getAdmissionsByStatus(String status, Pageable pageable) {
        return bedStore.findAdmissionsByStatus(status, pageable);
    }

    public Admission getAdmission(Long id) {
        return bedStore.findAdmissionById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.ADMISSION_NOT_FOUND));
    }

    @Transactional
    public Admission createAdmission(BedRequests.CreateAdmissionRequest request) {
        Patient patient = patientStore.findById(request.getPatientId())
                .orElseThrow(() -> new CommonException(ErrorCode.PATIENT_NOT_FOUND));

        Bed bed = getBed(request.getBedId());

        if (bedStore.findActiveAdmissionByBed(request.getBedId()).isPresent()) {
            throw new CommonException(ErrorCode.BED_OCCUPIED, "Bed is already occupied");
        }

        if (bedStore.findActiveAdmissionByPatient(request.getPatientId()).isPresent()) {
            throw new CommonException(ErrorCode.PATIENT_ALREADY_ADMITTED, "Patient is already admitted");
        }

        Admission admission = Admission.create(patient, bed, request.getAdmissionDate(), 
                request.getReason(), request.getNotes());
        
        bed.setOccupied();
        bedStore.saveBed(bed);

        return bedStore.saveAdmission(admission);
    }

    @Transactional
    public Admission updateAdmission(Long id, BedRequests.UpdateAdmissionRequest request) {
        Admission admission = getAdmission(id);
        admission.update(request.getReason(), request.getNotes());
        return bedStore.saveAdmission(admission);
    }

    @Transactional
    public Admission dischargePatient(Long id, BedRequests.DischargeRequest request) {
        Admission admission = getAdmission(id);
        admission.discharge(request.getDischargeDate(), request.getReason());

        Bed bed = admission.getBed();
        bed.setAvailable();
        bedStore.saveBed(bed);

        return bedStore.saveAdmission(admission);
    }

    @Transactional
    public Admission transferPatient(Long id, BedRequests.TransferRequest request) {
        Admission admission = getAdmission(id);
        Bed currentBed = admission.getBed();
        Bed newBed = getBed(request.getNewBedId());

        if (bedStore.findActiveAdmissionByBed(request.getNewBedId()).isPresent()) {
            throw new CommonException(ErrorCode.BED_OCCUPIED, "Target bed is already occupied");
        }

        admission.transfer(newBed, request.getTransferDate(), request.getReason(), request.getNotes());

        currentBed.setAvailable();
        newBed.setOccupied();
        bedStore.saveBed(currentBed);
        bedStore.saveBed(newBed);

        return bedStore.saveAdmission(admission);
    }

    // Bed availability operations
    public List<BedResponses.BedAvailabilitySummary> getBedAvailability(BedRequests.BedAvailabilityRequest request) {
        List<Bed> beds;
        
        if (request.getBedType() != null && request.getRoomCode() != null) {
            Room room = resourceStore.findRoomByCode(request.getRoomCode())
                    .orElseThrow(() -> new CommonException(ErrorCode.ROOM_NOT_FOUND));
            beds = bedStore.findAvailableBedsByRoom(room.getRoomId()).stream()
                    .filter(bed -> request.getBedType().equals(bed.getBedType()))
                    .collect(java.util.stream.Collectors.toList());
        } else if (request.getBedType() != null) {
            beds = bedStore.findAvailableBedsByBedType(request.getBedType());
        } else if (request.getRoomCode() != null) {
            Room room = resourceStore.findRoomByCode(request.getRoomCode())
                    .orElseThrow(() -> new CommonException(ErrorCode.ROOM_NOT_FOUND));
            beds = bedStore.findAvailableBedsByRoom(room.getRoomId());
        } else {
            beds = bedStore.findAvailableBeds();
        }

        List<BedResponses.BedAvailabilitySummary> summaries = new ArrayList<>();
        for (Bed bed : beds) {
            LocalDateTime lastOccupiedAt = null;
            LocalDateTime estimatedAvailableAt = null;

            // Get last admission for this bed
            Page<Admission> lastAdmissions = bedStore.findAdmissionsByBed(bed.getBedId(), Pageable.ofSize(1));
            if (!lastAdmissions.isEmpty()) {
                Admission lastAdmission = lastAdmissions.getContent().get(0);
                lastOccupiedAt = lastAdmission.getDischargeDate() != null ? 
                        lastAdmission.getDischargeDate() : lastAdmission.getAdmissionDate();
                estimatedAvailableAt = lastOccupiedAt.plusHours(2); // 2시간 후 사용 가능 예상
            }

            summaries.add(new BedResponses.BedAvailabilitySummary(
                    bed.getBedId(), bed.getBedNumber(), bed.getBedType(),
                    bed.getRoom().getCode(), bed.getRoom().getName(), bed.getStatus(),
                    lastOccupiedAt, estimatedAvailableAt));
        }

        return summaries;
    }

    public List<BedResponses.BedOccupancySummary> getBedOccupancySummary() {
        List<Room> rooms = resourceStore.findAllRooms();
        List<BedResponses.BedOccupancySummary> summaries = new ArrayList<>();

        for (Room room : rooms) {
            List<Bed> beds = bedStore.findBedsByRoom(room.getRoomId());
            int totalBeds = beds.size();
            int occupiedBeds = 0;
            int maintenanceBeds = 0;

            for (Bed bed : beds) {
                if ("OCCUPIED".equals(bed.getStatus())) {
                    occupiedBeds++;
                } else if ("MAINTENANCE".equals(bed.getStatus())) {
                    maintenanceBeds++;
                }
            }

            int availableBeds = totalBeds - occupiedBeds - maintenanceBeds;

            summaries.add(new BedResponses.BedOccupancySummary(
                    room.getRoomId(), room.getCode(), room.getName(),
                    totalBeds, occupiedBeds, availableBeds, maintenanceBeds));
        }

        return summaries;
    }
}
