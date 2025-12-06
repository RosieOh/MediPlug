package com.emrsystem.domain.appointment.response;

import com.emrsystem.domain.appointment.entity.AppointmentHistory;
import com.emrsystem.domain.appointment.entity.AppointmentReminder;
import com.emrsystem.domain.appointment.entity.AppointmentWaitlist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentEnhancementResponses {

    @Getter
    @NoArgsConstructor
    public static class WaitlistSummary {
        private Long waitlistId;
        private Long patientId;
        private String patientName;
        private Long doctorId;
        private String doctorName;
        private LocalDateTime preferredStartAt;
        private LocalDateTime preferredEndAt;
        private String status;
        private int priority;
        private String reason;
        private Long appointmentId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static WaitlistSummary of(AppointmentWaitlist waitlist) {
            WaitlistSummary summary = new WaitlistSummary();
            summary.waitlistId = waitlist.getId();
            summary.patientId = waitlist.getPatient().getId();
            summary.patientName = waitlist.getPatient().getName();
            summary.doctorId = waitlist.getDoctor().getId();
            summary.doctorName = waitlist.getDoctor().getName();
            summary.preferredStartAt = waitlist.getPreferredStartAt();
            summary.preferredEndAt = waitlist.getPreferredEndAt();
            summary.status = waitlist.getStatus();
            summary.priority = waitlist.getPriority();
            summary.reason = waitlist.getReason();
            summary.appointmentId = waitlist.getAppointment() != null ? waitlist.getAppointment().getId() : null;
            summary.createdAt = waitlist.getCreatedAt();
            summary.updatedAt = waitlist.getUpdatedAt();
            return summary;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class AppointmentHistorySummary {
        private Long historyId;
        private Long appointmentId;
        private String actionType;
        private String changedField;
        private String oldValue;
        private String newValue;
        private String changedBy;
        private String reason;
        private LocalDateTime createdAt;

        public static AppointmentHistorySummary of(AppointmentHistory history) {
            return new AppointmentHistorySummary(
                    history.getId(),
                    history.getAppointment().getId(),
                    history.getActionType(),
                    history.getChangedField(),
                    history.getOldValue(),
                    history.getNewValue(),
                    history.getChangedBy(),
                    history.getReason(),
                    history.getCreatedAt()
            );
        }
    }

    @Getter
    @AllArgsConstructor
    public static class ReminderSummary {
        private Long reminderId;
        private Long appointmentId;
        private String channel;
        private LocalDateTime scheduledAt;
        private LocalDateTime sentAt;
        private String status;
        private String recipient;
        private String message;
        private String errorMessage;
        private int retryCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static ReminderSummary of(AppointmentReminder reminder) {
            return new ReminderSummary(
                    reminder.getId(),
                    reminder.getAppointment().getId(),
                    reminder.getChannel(),
                    reminder.getScheduledAt(),
                    reminder.getSentAt(),
                    reminder.getStatus(),
                    reminder.getRecipient(),
                    reminder.getMessage(),
                    reminder.getErrorMessage(),
                    reminder.getRetryCount(),
                    reminder.getCreatedAt(),
                    reminder.getUpdatedAt()
            );
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AppointmentStatistics {
        private LocalDate startDate;
        private LocalDate endDate;
        private long totalAppointments;
        private long completedAppointments;
        private long cancelledAppointments;
        private long noShowAppointments;
        private BigDecimal completionRate; // 완료율 (%)
        private BigDecimal noShowRate; // 노쇼율 (%)
    }
}

