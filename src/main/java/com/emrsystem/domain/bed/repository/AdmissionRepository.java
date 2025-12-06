package com.emrsystem.domain.bed.repository;

import com.emrsystem.domain.bed.entity.Admission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AdmissionRepository extends JpaRepository<Admission, Long> {
    Optional<Admission> findByBed_BedIdAndStatus(Long bedId, String status);
    Optional<Admission> findByPatient_PatientIdAndStatus(Long patientId, String status);
    Page<Admission> findByPatient_PatientId(Long patientId, Pageable pageable);
    Page<Admission> findByBed_BedId(Long bedId, Pageable pageable);
    Page<Admission> findByStatus(String status, Pageable pageable);
    Page<Admission> findByAdmissionDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    Page<Admission> findByDischargeDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    List<Admission> findByStatus(String status);
    List<Admission> findByStatusAndBed_Room_RoomId(String status, Long roomId);
    
    // Statistics queries
    long countByStatus(String status);
}
