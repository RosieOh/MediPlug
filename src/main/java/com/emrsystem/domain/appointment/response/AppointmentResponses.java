package com.emrsystem.domain.appointment.response;

import com.emrsystem.domain.appointment.entity.Appointment;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class AppointmentResponses {

    @Getter
    @AllArgsConstructor
    public static class AppointmentSummary {
        private Long id;
        private Long patientId;
        private Long doctorId;
        private LocalDateTime startAt;
        private LocalDateTime endAt;
        private String status;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static AppointmentSummary from(Appointment appointment) {
            return new AppointmentSummary(
                    appointment.getId(),
                    appointment.getPatient().getId(),
                    appointment.getDoctor().getId(),
                    appointment.getStartAt(),
                    appointment.getEndAt(),
                    appointment.getStatus(),
                    appointment.getCreatedAt(),
                    appointment.getUpdatedAt()
            );
        }
    }
}


