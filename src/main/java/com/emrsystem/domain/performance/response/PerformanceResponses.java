package com.emrsystem.domain.performance.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PerformanceResponses {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DoctorPerformance {
        private Long doctorId;
        private String doctorName;
        private LocalDate startDate;
        private LocalDate endDate;
        private long medicalRecordCount;
        private long appointmentCount;
        private long completedAppointmentCount;
        private BigDecimal completionRate; // 완료율 (%)
    }
}

