package com.emrsystem.domain.appointment.service;

import com.emrsystem.domain.appointment.entity.Appointment;
import com.emrsystem.domain.appointment.entity.AppointmentHistory;
import com.emrsystem.domain.appointment.entity.AppointmentReminder;
import com.emrsystem.domain.appointment.entity.AppointmentWaitlist;
import com.emrsystem.domain.appointment.repository.AppointmentRepository;
import com.emrsystem.domain.appointment.request.AppointmentEnhancementRequests;
import com.emrsystem.domain.appointment.response.AppointmentEnhancementResponses;
import com.emrsystem.domain.appointment.store.AppointmentEnhancementStore;
import com.emrsystem.domain.doctor.entity.Doctor;
import com.emrsystem.domain.doctor.store.DoctorStore;
import com.emrsystem.domain.patient.entity.Patient;
import com.emrsystem.domain.patient.store.PatientStore;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentEnhancementService {

    private final AppointmentEnhancementStore appointmentEnhancementStore;
    private final AppointmentRepository appointmentRepository;
    private final PatientStore patientStore;
    private final DoctorStore doctorStore;

    // AppointmentWaitlist operations
    public List<AppointmentWaitlist> getWaitlistsByPatient(Long patientId) {
        return appointmentEnhancementStore.findWaitlistsByPatient(patientId);
    }

    public List<AppointmentWaitlist> getWaitlistsByDoctor(Long doctorId) {
        return appointmentEnhancementStore.findWaitlistsByDoctor(doctorId);
    }

    public List<AppointmentWaitlist> getPendingWaitlists() {
        return appointmentEnhancementStore.findPendingWaitlistsOrdered();
    }

    public List<AppointmentWaitlist> getPendingWaitlistsByDoctor(Long doctorId) {
        return appointmentEnhancementStore.findPendingWaitlistsByDoctor(doctorId);
    }

    @Transactional
    public AppointmentWaitlist addToWaitlist(AppointmentEnhancementRequests.CreateWaitlistRequest request) {
        Patient patient = patientStore.findById(request.getPatientId())
                .orElseThrow(() -> new CommonException(ErrorCode.PATIENT_NOT_FOUND));

        Doctor doctor = doctorStore.findById(request.getDoctorId())
                .orElseThrow(() -> new CommonException(ErrorCode.DOCTOR_NOT_FOUND));

        AppointmentWaitlist waitlist = AppointmentWaitlist.create(
                patient,
                doctor,
                request.getPreferredStartAt(),
                request.getPreferredEndAt(),
                request.getPriority() != null ? request.getPriority() : 10,
                request.getReason()
        );

        return appointmentEnhancementStore.saveWaitlist(waitlist);
    }

    @Transactional
    public AppointmentWaitlist notifyWaitlist(Long waitlistId) {
        AppointmentWaitlist waitlist = appointmentEnhancementStore.findWaitlistById(waitlistId)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "대기 목록을 찾을 수 없습니다."));
        waitlist.markAsNotified();
        return appointmentEnhancementStore.saveWaitlist(waitlist);
    }

    @Transactional
    public AppointmentWaitlist fulfillWaitlist(Long waitlistId, Long appointmentId) {
        AppointmentWaitlist waitlist = appointmentEnhancementStore.findWaitlistById(waitlistId)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "대기 목록을 찾을 수 없습니다."));

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "예약을 찾을 수 없습니다."));

        waitlist.fulfill(appointment);
        return appointmentEnhancementStore.saveWaitlist(waitlist);
    }

    @Transactional
    public AppointmentWaitlist cancelWaitlist(Long waitlistId) {
        AppointmentWaitlist waitlist = appointmentEnhancementStore.findWaitlistById(waitlistId)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "대기 목록을 찾을 수 없습니다."));
        waitlist.cancel();
        return appointmentEnhancementStore.saveWaitlist(waitlist);
    }

    // AppointmentHistory operations
    public List<AppointmentHistory> getAppointmentHistory(Long appointmentId) {
        return appointmentEnhancementStore.findHistoriesByAppointment(appointmentId);
    }

    @Transactional
    public AppointmentHistory recordAppointmentHistory(Appointment appointment, String actionType, String changedBy) {
        AppointmentHistory history = AppointmentHistory.create(appointment, actionType, changedBy);
        return appointmentEnhancementStore.saveHistory(history);
    }

    @Transactional
    public AppointmentHistory recordAppointmentChange(Appointment appointment, String changedField,
                                                      String oldValue, String newValue, String changedBy, String reason) {
        AppointmentHistory history = AppointmentHistory.createChange(
                appointment, changedField, oldValue, newValue, changedBy, reason);
        return appointmentEnhancementStore.saveHistory(history);
    }

    // AppointmentReminder operations
    public List<AppointmentReminder> getRemindersByAppointment(Long appointmentId) {
        return appointmentEnhancementStore.findRemindersByAppointment(appointmentId);
    }

    public List<AppointmentReminder> getPendingReminders() {
        return appointmentEnhancementStore.findPendingRemindersBefore(LocalDateTime.now());
    }

    @Transactional
    public AppointmentReminder createReminder(AppointmentEnhancementRequests.CreateReminderRequest request) {
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "예약을 찾을 수 없습니다."));

        // 예약 시간 24시간 전에 리마인더 발송 (기본값)
        LocalDateTime scheduledAt = request.getScheduledAt() != null
                ? request.getScheduledAt()
                : appointment.getStartAt().minusHours(24);

        String recipient = request.getRecipient() != null
                ? request.getRecipient()
                : appointment.getPatient().getPhone();

        String message = request.getMessage() != null
                ? request.getMessage()
                : String.format("%s님, %s 예약이 %s에 있습니다.",
                        appointment.getPatient().getName(),
                        appointment.getDoctor().getName(),
                        appointment.getStartAt().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        AppointmentReminder reminder = AppointmentReminder.create(
                appointment,
                request.getChannel(),
                scheduledAt,
                recipient,
                message
        );

        return appointmentEnhancementStore.saveReminder(reminder);
    }

    @Transactional
    public AppointmentReminder markReminderAsSent(Long reminderId) {
        AppointmentReminder reminder = appointmentEnhancementStore.findReminderById(reminderId)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "리마인더를 찾을 수 없습니다."));
        reminder.markAsSent(LocalDateTime.now());
        return appointmentEnhancementStore.saveReminder(reminder);
    }

    @Transactional
    public AppointmentReminder markReminderAsFailed(Long reminderId, String errorMessage) {
        AppointmentReminder reminder = appointmentEnhancementStore.findReminderById(reminderId)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "리마인더를 찾을 수 없습니다."));
        reminder.markAsFailed(errorMessage);
        return appointmentEnhancementStore.saveReminder(reminder);
    }

    // Statistics
    public AppointmentEnhancementResponses.AppointmentStatistics getAppointmentStatistics(
            LocalDateTime startDate, LocalDateTime endDate) {
        List<Appointment> appointments = appointmentRepository.findByStartAtBetween(
                startDate, endDate, org.springframework.data.domain.Pageable.unpaged()).getContent();

        long totalAppointments = appointments.size();
        long completedAppointments = appointments.stream()
                .filter(a -> "COMPLETED".equals(a.getStatus()))
                .count();
        long cancelledAppointments = appointments.stream()
                .filter(a -> "CANCELLED".equals(a.getStatus()))
                .count();
        long noShowAppointments = appointmentEnhancementStore.findHistoriesByActionType("NO_SHOW").stream()
                .filter(h -> h.getCreatedAt().isAfter(startDate) && h.getCreatedAt().isBefore(endDate))
                .count();

        double completionRate = totalAppointments > 0
                ? (double) completedAppointments / totalAppointments * 100
                : 0.0;
        double noShowRate = totalAppointments > 0
                ? (double) noShowAppointments / totalAppointments * 100
                : 0.0;

        return AppointmentEnhancementResponses.AppointmentStatistics.builder()
                .startDate(startDate.toLocalDate())
                .endDate(endDate.toLocalDate())
                .totalAppointments(totalAppointments)
                .completedAppointments(completedAppointments)
                .cancelledAppointments(cancelledAppointments)
                .noShowAppointments(noShowAppointments)
                .completionRate(java.math.BigDecimal.valueOf(completionRate).setScale(2, java.math.RoundingMode.HALF_UP))
                .noShowRate(java.math.BigDecimal.valueOf(noShowRate).setScale(2, java.math.RoundingMode.HALF_UP))
                .build();
    }
}

