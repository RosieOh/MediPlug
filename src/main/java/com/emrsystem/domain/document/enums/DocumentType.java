package com.emrsystem.domain.document.enums;

public enum DocumentType {
    DIAGNOSIS_CERTIFICATE("진단서"),
    MEDICAL_OPINION("소견서"),
    DISCHARGE_SUMMARY("퇴원 요약서"),
    PRESCRIPTION("처방전"),
    LAB_REPORT("검사 결과 리포트"),
    SURGERY_REPORT("수술 기록"),
    ADMISSION_RECORD("입원 기록"),
    STATISTICS_REPORT("통계 리포트"),
    MEDICAL_RECORD("진료 기록"),
    REFERRAL_LETTER("의뢰서");

    private final String description;

    DocumentType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static DocumentType fromString(String value) {
        for (DocumentType type : DocumentType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown document type: " + value);
    }
}

