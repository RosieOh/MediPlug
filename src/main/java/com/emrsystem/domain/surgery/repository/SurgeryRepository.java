package com.emrsystem.domain.surgery.repository;

import com.emrsystem.domain.surgery.entity.Surgery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SurgeryRepository extends JpaRepository<Surgery, Long> {
    List<Surgery> findByPatient_PatientId(Long patientId);
    Page<Surgery> findByPatient_PatientId(Long patientId, Pageable pageable);
    List<Surgery> findBySurgeon_DoctorId(Long surgeonId);
    List<Surgery> findByScheduledDateTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Surgery> findByStatus(String status);
    List<Surgery> findBySurgeryRoomAndScheduledDateTimeBetween(String surgeryRoom, LocalDateTime start, LocalDateTime end);
}

