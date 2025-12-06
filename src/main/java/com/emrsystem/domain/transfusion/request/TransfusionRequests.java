package com.emrsystem.domain.transfusion.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TransfusionRequests {

    @Getter
    @NoArgsConstructor
    public static class CreateTransfusionRequest {
        @NotNull(message = "환자 ID는 필수입니다.")
        private Long patientId;

        @NotNull(message = "요청 의사 ID는 필수입니다.")
        private Long requestingDoctorId;

        @NotBlank(message = "혈액 성분은 필수입니다.")
        @Size(max = 50, message = "혈액 성분은 50자를 초과할 수 없습니다.")
        private String bloodComponent; // 전혈, 적혈구, 혈소판, 혈장 등

        @NotNull(message = "수혈 단위는 필수입니다.")
        private Integer units;

        @NotBlank(message = "수혈 사유는 필수입니다.")
        private String reason;
    }

    @Getter
    @NoArgsConstructor
    public static class CreateTransfusionRecordRequest {
        @NotNull(message = "수혈 요청 ID는 필수입니다.")
        private Long transfusionRequestId;

        @NotNull(message = "시행 의사 ID는 필수입니다.")
        private Long administeringDoctorId;

        @NotBlank(message = "혈액 성분은 필수입니다.")
        @Size(max = 50, message = "혈액 성분은 50자를 초과할 수 없습니다.")
        private String bloodComponent;

        @NotBlank(message = "혈액형은 필수입니다.")
        @Size(max = 50, message = "혈액형은 50자를 초과할 수 없습니다.")
        private String bloodType;

        @NotBlank(message = "혈액팩 번호는 필수입니다.")
        @Size(max = 100, message = "혈액팩 번호는 100자를 초과할 수 없습니다.")
        private String bloodBagNumber;

        @NotNull(message = "수혈 단위는 필수입니다.")
        private Integer units;
    }

    @Getter
    @NoArgsConstructor
    public static class CreateTransfusionReactionRequest {
        @NotNull(message = "수혈 기록 ID는 필수입니다.")
        private Long transfusionRecordId;

        @NotBlank(message = "반응 유형은 필수입니다.")
        @Size(max = 50, message = "반응 유형은 50자를 초과할 수 없습니다.")
        private String reactionType;

        @NotBlank(message = "심각도는 필수입니다.")
        @Size(max = 50, message = "심각도는 50자를 초과할 수 없습니다.")
        private String severity;

        @NotBlank(message = "증상은 필수입니다.")
        private String symptoms;

        private String treatment;
    }
}

