package com.emrsystem.domain.appointment.facade;

import com.emrsystem.domain.appointment.entity.Appointment;
import com.emrsystem.domain.appointment.entity.AppointmentHistory;
import com.emrsystem.domain.appointment.entity.AppointmentReminder;
import com.emrsystem.domain.appointment.entity.AppointmentWaitlist;
import com.emrsystem.domain.appointment.request.AppointmentEnhancementRequests;
import com.emrsystem.domain.appointment.response.AppointmentEnhancementResponses;
import com.emrsystem.domain.appointment.service.AppointmentEnhancementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AppointmentEnhancementFacade {

    private final AppointmentEnhancementService appointmentEnhancementService;

    public List<AppointmentWaitlist> getWaitlistsByPatient(Long patientId) {
        return appointmentEnhancementService.getWaitlistsByPatient(patientId);
    }

    public List<AppointmentWaitlist> getWaitlistsByDoctor(Long doctorId) {
        return appointmentEnhancementService.getWaitlistsByDoctor(doctorId);
    }

    public List<AppointmentWaitlist> getPendingWaitlists() {
        return appointmentEnhancementService.getPendingWaitlists();
    }

    public List<AppointmentWaitlist> getPendingWaitlistsByDoctor(Long doctorId) {
        return appointmentEnhancementService.getPendingWaitlistsByDoctor(doctorId);
    }

    public AppointmentWaitlist addToWaitlist(AppointmentEnhancementRequests.CreateWaitlistRequest request) {
        return appointmentEnhancementService.addToWaitlist(request);
    }

    public AppointmentWaitlist notifyWaitlist(Long waitlistId) {
        return appointmentEnhancementService.notifyWaitlist(waitlistId);
    }

    public AppointmentWaitlist fulfillWaitlist(Long waitlistId, Long appointmentId) {
        return appointmentEnhancementService.fulfillWaitlist(waitlistId, appointmentId);
    }

    public AppointmentWaitlist cancelWaitlist(Long waitlistId) {
        return appointmentEnhancementService.cancelWaitlist(waitlistId);
    }

    public List<AppointmentHistory> getAppointmentHistory(Long appointmentId) {
        return appointmentEnhancementService.getAppointmentHistory(appointmentId);
    }

    public AppointmentHistory recordAppointmentHistory(Appointment appointment, String actionType, String changedBy) {
        return appointmentEnhancementService.recordAppointmentHistory(appointment, actionType, changedBy);
    }

    public AppointmentHistory recordAppointmentChange(Appointment appointment, String changedField,
                                                      String oldValue, String newValue, String changedBy, String reason) {
        return appointmentEnhancementService.recordAppointmentChange(appointment, changedField, oldValue, newValue, changedBy, reason);
    }

    public List<AppointmentReminder> getRemindersByAppointment(Long appointmentId) {
        return appointmentEnhancementService.getRemindersByAppointment(appointmentId);
    }

    public List<AppointmentReminder> getPendingReminders() {
        return appointmentEnhancementService.getPendingReminders();
    }

    public AppointmentReminder createReminder(AppointmentEnhancementRequests.CreateReminderRequest request) {
        return appointmentEnhancementService.createReminder(request);
    }

    public AppointmentReminder markReminderAsSent(Long reminderId) {
        return appointmentEnhancementService.markReminderAsSent(reminderId);
    }

    public AppointmentReminder markReminderAsFailed(Long reminderId, String errorMessage) {
        return appointmentEnhancementService.markReminderAsFailed(reminderId, errorMessage);
    }

    public AppointmentEnhancementResponses.AppointmentStatistics getAppointmentStatistics(
            LocalDateTime startDate, LocalDateTime endDate) {
        return appointmentEnhancementService.getAppointmentStatistics(startDate, endDate);
    }
}

