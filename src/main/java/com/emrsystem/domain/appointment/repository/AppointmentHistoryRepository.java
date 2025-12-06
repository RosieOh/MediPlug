package com.emrsystem.domain.appointment.repository;

import com.emrsystem.domain.appointment.entity.AppointmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentHistoryRepository extends JpaRepository<AppointmentHistory, Long> {
    List<AppointmentHistory> findByAppointment_AppointmentId(Long appointmentId);
    List<AppointmentHistory> findByAppointment_AppointmentIdOrderByCreatedAtDesc(Long appointmentId);
    List<AppointmentHistory> findByActionType(String actionType);
}

