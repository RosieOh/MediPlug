package com.emrsystem.domain.appointment.repository;

import com.emrsystem.domain.appointment.entity.AppointmentWaitlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentWaitlistRepository extends JpaRepository<AppointmentWaitlist, Long> {
    List<AppointmentWaitlist> findByPatient_PatientId(Long patientId);
    List<AppointmentWaitlist> findByDoctor_DoctorId(Long doctorId);
    List<AppointmentWaitlist> findByStatus(String status);
    List<AppointmentWaitlist> findByStatusOrderByPriorityAscCreatedAtAsc(String status);
    List<AppointmentWaitlist> findByDoctor_DoctorIdAndStatusOrderByPriorityAscCreatedAtAsc(Long doctorId, String status);
    List<AppointmentWaitlist> findByPreferredStartAtBetween(LocalDateTime start, LocalDateTime end);
}

