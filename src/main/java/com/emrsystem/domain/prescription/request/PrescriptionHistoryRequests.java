package com.emrsystem.domain.prescription.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PrescriptionHistoryRequests {

    @Getter
    @NoArgsConstructor
    public static class CreateMedicationLogRequest {
        @NotNull(message = "처방 ID는 필수입니다.")
        private Long prescriptionId;

        private LocalDate medicationDate;

        private LocalDateTime medicationTime;

        @NotNull(message = "복용 여부는 필수입니다.")
        private boolean taken;

        @NotBlank(message = "기록한 사람은 필수입니다.")
        @Size(max = 200, message = "기록한 사람은 200자를 초과할 수 없습니다.")
        private String recordedBy;

        private String notes;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateMedicationLogRequest {
        private Boolean taken;

        private String notes;
    }
}

