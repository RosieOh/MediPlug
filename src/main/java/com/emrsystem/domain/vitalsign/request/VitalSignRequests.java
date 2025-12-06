package com.emrsystem.domain.vitalsign.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VitalSignRequests {

    @Getter
    @NoArgsConstructor
    public static class CreateVitalSignRequest {
        @NotNull(message = "환자 ID는 필수입니다.")
        private Long patientId;

        @NotNull(message = "측정 일시는 필수입니다.")
        private LocalDateTime measuredAt;

        private BigDecimal systolicBP; // 수축기 혈압

        private BigDecimal diastolicBP; // 이완기 혈압

        private BigDecimal heartRate; // 맥박

        private BigDecimal temperature; // 체온

        private BigDecimal respiratoryRate; // 호흡수

        private BigDecimal oxygenSaturation; // 산소포화도

        private BigDecimal bloodSugar; // 혈당

        private BigDecimal painScore; // 통증 점수

        @Size(max = 50, message = "측정한 사람은 50자를 초과할 수 없습니다.")
        private String measuredBy;

        @Size(max = 100, message = "측정 기기는 100자를 초과할 수 없습니다.")
        private String device;

        private String notes;
    }
}

