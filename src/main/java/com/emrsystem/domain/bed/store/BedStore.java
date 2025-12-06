package com.emrsystem.domain.bed.store;

import com.emrsystem.domain.bed.entity.Admission;
import com.emrsystem.domain.bed.entity.Bed;
import com.emrsystem.domain.bed.repository.AdmissionRepository;
import com.emrsystem.domain.bed.repository.BedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BedStore {

    private final BedRepository bedRepository;
    private final AdmissionRepository admissionRepository;

    // Bed operations
    public Bed saveBed(Bed bed) {
        return bedRepository.save(bed);
    }

    public Optional<Bed> findBedById(Long id) {
        return bedRepository.findById(id);
    }

    public List<Bed> findBedsByRoom(Long roomId) {
        return bedRepository.findByRoom_RoomId(roomId);
    }

    public List<Bed> findBedsByStatus(String status) {
        return bedRepository.findByStatus(status);
    }

    public List<Bed> findBedsByBedType(String bedType) {
        return bedRepository.findByBedType(bedType);
    }

    public List<Bed> findAvailableBeds() {
        return bedRepository.findByStatus("AVAILABLE");
    }

    public List<Bed> findAvailableBedsByBedType(String bedType) {
        return bedRepository.findByStatusAndBedType("AVAILABLE", bedType);
    }

    public List<Bed> findAvailableBedsByRoom(Long roomId) {
        return bedRepository.findByStatusAndRoom_RoomId("AVAILABLE", roomId);
    }

    public boolean existsBedByRoomAndBedNumber(Long roomId, String bedNumber) {
        return bedRepository.existsByRoom_RoomIdAndBedNumber(roomId, bedNumber);
    }

    public void deleteBedById(Long id) {
        bedRepository.deleteById(id);
    }

    // Admission operations
    public Admission saveAdmission(Admission admission) {
        return admissionRepository.save(admission);
    }

    public Optional<Admission> findAdmissionById(Long id) {
        return admissionRepository.findById(id);
    }

    public Optional<Admission> findActiveAdmissionByBed(Long bedId) {
        return admissionRepository.findByBed_BedIdAndStatus(bedId, "ACTIVE");
    }

    public Optional<Admission> findActiveAdmissionByPatient(Long patientId) {
        return admissionRepository.findByPatient_PatientIdAndStatus(patientId, "ACTIVE");
    }

    public Page<Admission> findAdmissionsByPatient(Long patientId, Pageable pageable) {
        return admissionRepository.findByPatient_PatientId(patientId, pageable);
    }

    public Page<Admission> findAdmissionsByBed(Long bedId, Pageable pageable) {
        return admissionRepository.findByBed_BedId(bedId, pageable);
    }

    public Page<Admission> findAdmissionsByStatus(String status, Pageable pageable) {
        return admissionRepository.findByStatus(status, pageable);
    }

    public Page<Admission> findAdmissionsByAdmissionDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return admissionRepository.findByAdmissionDateBetween(startDate, endDate, pageable);
    }

    public Page<Admission> findAdmissionsByDischargeDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return admissionRepository.findByDischargeDateBetween(startDate, endDate, pageable);
    }

    public List<Admission> findActiveAdmissions() {
        return admissionRepository.findByStatus("ACTIVE");
    }

    public List<Admission> findActiveAdmissionsByRoom(Long roomId) {
        return admissionRepository.findByStatusAndBed_Room_RoomId("ACTIVE", roomId);
    }

    public void deleteAdmissionById(Long id) {
        admissionRepository.deleteById(id);
    }
}
