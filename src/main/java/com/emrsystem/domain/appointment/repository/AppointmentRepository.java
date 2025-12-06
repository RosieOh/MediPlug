package com.emrsystem.domain.appointment.repository;

import com.emrsystem.domain.appointment.entity.Appointment;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {
    Page<Appointment> findByDoctor_Id(Long doctorId, Pageable pageable);
    Page<Appointment> findByPatient_Id(Long patientId, Pageable pageable);
    Page<Appointment> findByStartAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("select count(a) > 0 from Appointment a where a.doctor.id = :doctorId and a.deleted = false and ((a.startAt < :endAt and a.endAt > :startAt))")
    boolean existsOverlapping(@Param("doctorId") Long doctorId,
                              @Param("startAt") LocalDateTime startAt,
                              @Param("endAt") LocalDateTime endAt);
    
    // Statistics queries
    long countByStartAtBetween(LocalDateTime start, LocalDateTime end);
}


