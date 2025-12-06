package com.emrsystem.domain.appointment.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AppointmentRequests {

    @Getter
    @NoArgsConstructor
    public static class CreateAppointmentRequest {
        @NotNull
        private Long patientId;
        @NotNull
        private Long doctorId;
        @NotNull
        @Future
        private LocalDateTime startAt;
        @NotNull
        @Future
        private LocalDateTime endAt;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateAppointmentRequest {
        @NotNull
        @Future
        private LocalDateTime startAt;
        @NotNull
        @Future
        private LocalDateTime endAt;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateAppointmentStatusRequest {
        @NotNull
        private String status;
    }
}


