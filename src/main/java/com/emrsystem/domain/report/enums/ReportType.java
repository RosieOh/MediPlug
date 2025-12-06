package com.emrsystem.domain.report.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportType {
    MEDICAL_SUMMARY("MEDICAL_SUMMARY", "진료 요약"),
    LAB_RESULTS("LAB_RESULTS", "검사 결과"),
    INPATIENT("INPATIENT", "입원 환자"),
    STATISTICS("STATISTICS", "통계");

    private final String code;
    private final String description;

    public static ReportType fromString(String code) {
        for (ReportType type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown report type: " + code);
    }
}

