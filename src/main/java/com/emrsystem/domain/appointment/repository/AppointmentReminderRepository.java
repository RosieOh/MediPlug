package com.emrsystem.domain.appointment.repository;

import com.emrsystem.domain.appointment.entity.AppointmentReminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentReminderRepository extends JpaRepository<AppointmentReminder, Long> {
    List<AppointmentReminder> findByAppointment_AppointmentId(Long appointmentId);
    List<AppointmentReminder> findByStatus(String status);
    List<AppointmentReminder> findByStatusAndScheduledAtBefore(String status, LocalDateTime scheduledAt);
    List<AppointmentReminder> findByStatusAndScheduledAtBetween(String status, LocalDateTime start, LocalDateTime end);
}

