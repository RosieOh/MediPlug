package com.emrsystem.domain.appointment.store;

import com.emrsystem.domain.appointment.entity.AppointmentHistory;
import com.emrsystem.domain.appointment.entity.AppointmentReminder;
import com.emrsystem.domain.appointment.entity.AppointmentWaitlist;
import com.emrsystem.domain.appointment.repository.AppointmentHistoryRepository;
import com.emrsystem.domain.appointment.repository.AppointmentReminderRepository;
import com.emrsystem.domain.appointment.repository.AppointmentWaitlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AppointmentEnhancementStore {

    private final AppointmentWaitlistRepository appointmentWaitlistRepository;
    private final AppointmentHistoryRepository appointmentHistoryRepository;
    private final AppointmentReminderRepository appointmentReminderRepository;

    // AppointmentWaitlist operations
    public AppointmentWaitlist saveWaitlist(AppointmentWaitlist waitlist) {
        return appointmentWaitlistRepository.save(waitlist);
    }

    public Optional<AppointmentWaitlist> findWaitlistById(Long id) {
        return appointmentWaitlistRepository.findById(id);
    }

    public List<AppointmentWaitlist> findWaitlistsByPatient(Long patientId) {
        return appointmentWaitlistRepository.findByPatient_PatientId(patientId);
    }

    public List<AppointmentWaitlist> findWaitlistsByDoctor(Long doctorId) {
        return appointmentWaitlistRepository.findByDoctor_DoctorId(doctorId);
    }

    public List<AppointmentWaitlist> findWaitlistsByStatus(String status) {
        return appointmentWaitlistRepository.findByStatus(status);
    }

    public List<AppointmentWaitlist> findPendingWaitlistsOrdered() {
        return appointmentWaitlistRepository.findByStatusOrderByPriorityAscCreatedAtAsc("PENDING");
    }

    public List<AppointmentWaitlist> findPendingWaitlistsByDoctor(Long doctorId) {
        return appointmentWaitlistRepository.findByDoctor_DoctorIdAndStatusOrderByPriorityAscCreatedAtAsc(doctorId, "PENDING");
    }

    // AppointmentHistory operations
    public AppointmentHistory saveHistory(AppointmentHistory history) {
        return appointmentHistoryRepository.save(history);
    }

    public List<AppointmentHistory> findHistoriesByAppointment(Long appointmentId) {
        return appointmentHistoryRepository.findByAppointment_AppointmentIdOrderByCreatedAtDesc(appointmentId);
    }

    public List<AppointmentHistory> findHistoriesByActionType(String actionType) {
        return appointmentHistoryRepository.findByActionType(actionType);
    }

    // AppointmentReminder operations
    public AppointmentReminder saveReminder(AppointmentReminder reminder) {
        return appointmentReminderRepository.save(reminder);
    }

    public Optional<AppointmentReminder> findReminderById(Long id) {
        return appointmentReminderRepository.findById(id);
    }

    public List<AppointmentReminder> findRemindersByAppointment(Long appointmentId) {
        return appointmentReminderRepository.findByAppointment_AppointmentId(appointmentId);
    }

    public List<AppointmentReminder> findRemindersByStatus(String status) {
        return appointmentReminderRepository.findByStatus(status);
    }

    public List<AppointmentReminder> findPendingRemindersBefore(LocalDateTime scheduledAt) {
        return appointmentReminderRepository.findByStatusAndScheduledAtBefore("PENDING", scheduledAt);
    }

    public List<AppointmentReminder> findRemindersByStatusAndDateRange(String status, LocalDateTime start, LocalDateTime end) {
        return appointmentReminderRepository.findByStatusAndScheduledAtBetween(status, start, end);
    }
}

