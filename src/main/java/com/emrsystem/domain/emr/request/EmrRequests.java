package com.emrsystem.domain.emr.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class EmrRequests {

    @Getter
    @NoArgsConstructor
    public static class CreateMedicalRecordRequest {
        @NotNull(message = "환자 ID는 필수입니다.")
        private Long patientId;

        @NotNull(message = "의사 ID는 필수입니다.")
        private Long doctorId;

        @NotNull(message = "기록일시는 필수입니다.")
        private java.time.LocalDateTime recordDate;

        @NotBlank(message = "기록 내용은 필수입니다.")
        private String content;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateMedicalRecordRequest {
        @NotNull(message = "기록일시는 필수입니다.")
        private java.time.LocalDateTime recordDate;

        @NotBlank(message = "기록 내용은 필수입니다.")
        private String content;
    }

    @Getter
    @NoArgsConstructor
    public static class CreateChartTemplateRequest {
        @NotBlank(message = "템플릿 코드는 필수입니다.")
        @Size(max = 100, message = "템플릿 코드는 100자를 초과할 수 없습니다.")
        private String code;

        @NotBlank(message = "템플릿 이름은 필수입니다.")
        @Size(max = 100, message = "템플릿 이름은 100자를 초과할 수 없습니다.")
        private String name;

        @NotBlank(message = "템플릿 내용은 필수입니다.")
        private String content;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateChartTemplateRequest {
        @NotBlank(message = "템플릿 이름은 필수입니다.")
        @Size(max = 100, message = "템플릿 이름은 100자를 초과할 수 없습니다.")
        private String name;

        @NotBlank(message = "템플릿 내용은 필수입니다.")
        private String content;
    }

    @Getter
    @NoArgsConstructor
    public static class CreatePrescriptionRequest {
        @NotNull(message = "진료기록 ID는 필수입니다.")
        private Long medicalRecordId;

        @NotBlank(message = "약물코드는 필수입니다.")
        @Size(max = 50, message = "약물코드는 50자를 초과할 수 없습니다.")
        private String drugCode;

        @NotBlank(message = "약물명은 필수입니다.")
        @Size(max = 100, message = "약물명은 100자를 초과할 수 없습니다.")
        private String drugName;

        @NotBlank(message = "용량은 필수입니다.")
        @Size(max = 200, message = "용량은 200자를 초과할 수 없습니다.")
        private String dosage;

        @NotBlank(message = "복용횟수는 필수입니다.")
        @Size(max = 100, message = "복용횟수는 100자를 초과할 수 없습니다.")
        private String frequency;

        @NotBlank(message = "복용기간은 필수입니다.")
        @Size(max = 100, message = "복용기간은 100자를 초과할 수 없습니다.")
        private String duration;

        @Size(max = 50, message = "단위는 50자를 초과할 수 없습니다.")
        private String unit = "정"; // 기본값

        private String notes;
    }

    @Getter
    @NoArgsConstructor
    public static class CreateLabOrderRequest {
        @NotNull(message = "진료기록 ID는 필수입니다.")
        private Long medicalRecordId;

        @NotBlank(message = "검사명은 필수입니다.")
        @Size(max = 100, message = "검사명은 100자를 초과할 수 없습니다.")
        private String testName;

        @NotNull(message = "오더일시는 필수입니다.")
        private java.time.LocalDateTime orderDate;
    }

    @Getter
    @NoArgsConstructor
    public static class CreateDiagnosisCodeRequest {
        @NotBlank(message = "진단 코드는 필수입니다.")
        @Size(max = 20, message = "진단 코드는 20자를 초과할 수 없습니다.")
        private String code;

        @NotBlank(message = "진단명은 필수입니다.")
        @Size(max = 500, message = "진단명은 500자를 초과할 수 없습니다.")
        private String name;

        @Size(max = 100, message = "카테고리는 100자를 초과할 수 없습니다.")
        private String category;

        private String description;

        @NotNull(message = "버전은 필수입니다.")
        private Integer version; // 10 for ICD-10, 11 for ICD-11
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateDiagnosisCodeRequest {
        @NotBlank(message = "진단명은 필수입니다.")
        @Size(max = 500, message = "진단명은 500자를 초과할 수 없습니다.")
        private String name;

        @Size(max = 100, message = "카테고리는 100자를 초과할 수 없습니다.")
        private String category;

        private String description;
    }

    @Getter
    @NoArgsConstructor
    public static class CreateLabResultRequest {
        @NotNull(message = "검사 오더 ID는 필수입니다.")
        private Long labOrderId;

        @NotBlank(message = "검사 항목명은 필수입니다.")
        @Size(max = 200, message = "검사 항목명은 200자를 초과할 수 없습니다.")
        private String testItemName;

        @Size(max = 50, message = "검사 항목 코드는 50자를 초과할 수 없습니다.")
        private String testItemCode;

        @Size(max = 200, message = "결과값은 200자를 초과할 수 없습니다.")
        private String resultValue;

        @Size(max = 50, message = "단위는 50자를 초과할 수 없습니다.")
        private String unit;

        @Size(max = 100, message = "정상 범위는 100자를 초과할 수 없습니다.")
        private String referenceRange;

        @Size(max = 10, message = "이상치 플래그는 10자를 초과할 수 없습니다.")
        private String abnormalFlag; // H, L, N, A

        private String notes;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateLabResultRequest {
        @Size(max = 200, message = "결과값은 200자를 초과할 수 없습니다.")
        private String resultValue;

        @Size(max = 50, message = "단위는 50자를 초과할 수 없습니다.")
        private String unit;

        @Size(max = 100, message = "정상 범위는 100자를 초과할 수 없습니다.")
        private String referenceRange;

        @Size(max = 10, message = "이상치 플래그는 10자를 초과할 수 없습니다.")
        private String abnormalFlag;

        private String notes;
    }

    @Getter
    @NoArgsConstructor
    public static class CreateMedicalImageRequest {
        @NotNull(message = "환자 ID는 필수입니다.")
        private Long patientId;

        private Long medicalRecordId;

        private Long labOrderId;

        @NotBlank(message = "영상 타입은 필수입니다.")
        @Size(max = 50, message = "영상 타입은 50자를 초과할 수 없습니다.")
        private String imageType; // XRAY, CT, MRI, ULTRASOUND, ETC

        @Size(max = 50, message = "모달리티는 50자를 초과할 수 없습니다.")
        private String modality;

        @NotBlank(message = "파일 경로는 필수입니다.")
        @Size(max = 500, message = "파일 경로는 500자를 초과할 수 없습니다.")
        private String filePath;

        @NotBlank(message = "파일명은 필수입니다.")
        @Size(max = 200, message = "파일명은 200자를 초과할 수 없습니다.")
        private String fileName;

        @NotNull(message = "파일 크기는 필수입니다.")
        private Long fileSize;

        @Size(max = 100, message = "MIME 타입은 100자를 초과할 수 없습니다.")
        private String mimeType;

        @NotNull(message = "촬영일시는 필수입니다.")
        private java.time.LocalDateTime studyDate;

        @Size(max = 500, message = "검사 설명은 500자를 초과할 수 없습니다.")
        private String studyDescription;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateMedicalImageInterpretationRequest {
        @NotNull(message = "판독 의사 ID는 필수입니다.")
        private Long radiologistId;

        @NotBlank(message = "판독 결과는 필수입니다.")
        private String interpretation;
    }

    @Getter
    @NoArgsConstructor
    public static class CreateMedicalCertificateRequest {
        @NotNull(message = "환자 ID는 필수입니다.")
        private Long patientId;

        private Long medicalRecordId;

        @NotNull(message = "의사 ID는 필수입니다.")
        private Long doctorId;

        @NotBlank(message = "증명서 타입은 필수입니다.")
        @Size(max = 50, message = "증명서 타입은 50자를 초과할 수 없습니다.")
        private String certificateType; // DIAGNOSIS, MEDICAL_OPINION, DISCHARGE_SUMMARY, etc.

        @NotBlank(message = "증명서 내용은 필수입니다.")
        private String content;

        private String diagnosis;

        private String purpose;

        @NotNull(message = "발급일은 필수입니다.")
        private java.time.LocalDate issueDate;

        @NotNull(message = "유효기간 시작일은 필수입니다.")
        private java.time.LocalDate effectiveDate;

        private java.time.LocalDate expiryDate;
    }
}
