package com.emrsystem.domain.report.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class ReportRequests {

    @Getter
    @NoArgsConstructor
    public static class GenerateReportRequest {
        @NotBlank(message = "리포트 유형은 필수입니다.")
        private String reportType; // MEDICAL_SUMMARY, LAB_RESULTS, INPATIENT, STATISTICS

        @NotBlank(message = "포맷은 필수입니다.")
        private String format; // PDF, EXCEL

        private Long patientId; // 환자 ID (진료 요약, 검사 결과 리포트용)

        private Long admissionId; // 입원 ID (입원 환자 리포트용)

        private LocalDate startDate; // 시작일 (검사 결과, 통계 리포트용)

        private LocalDate endDate; // 종료일 (검사 결과, 통계 리포트용)
    }
}

