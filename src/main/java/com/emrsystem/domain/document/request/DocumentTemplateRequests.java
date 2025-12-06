package com.emrsystem.domain.document.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

public class DocumentTemplateRequests {

    @Getter
    @NoArgsConstructor
    public static class CreateTemplateRequest {
        @NotBlank(message = "템플릿 이름은 필수입니다.")
        @Size(max = 100, message = "템플릿 이름은 100자를 초과할 수 없습니다.")
        private String templateName;

        @NotBlank(message = "문서 타입은 필수입니다.")
        @Size(max = 50, message = "문서 타입은 50자를 초과할 수 없습니다.")
        private String documentType;

        @NotBlank(message = "포맷은 필수입니다.")
        @Size(max = 50, message = "포맷은 50자를 초과할 수 없습니다.")
        private String format; // PDF, WORD, EXCEL, HTML

        @NotBlank(message = "템플릿 내용은 필수입니다.")
        private String content;

        @Size(max = 500, message = "설명은 500자를 초과할 수 없습니다.")
        private String description;

        private boolean isDefault = false;

        @Size(max = 100, message = "버전은 100자를 초과할 수 없습니다.")
        private String version;

        @Valid
        private List<VariableRequest> variables;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateTemplateRequest {
        @NotBlank(message = "템플릿 이름은 필수입니다.")
        @Size(max = 100, message = "템플릿 이름은 100자를 초과할 수 없습니다.")
        private String templateName;

        @NotBlank(message = "템플릿 내용은 필수입니다.")
        private String content;

        @Size(max = 500, message = "설명은 500자를 초과할 수 없습니다.")
        private String description;

        @Size(max = 100, message = "버전은 100자를 초과할 수 없습니다.")
        private String version;

        @Valid
        private List<VariableRequest> variables;

        private Long userId; // 수정자 ID (선택)
    }

    @Getter
    @NoArgsConstructor
    public static class VariableRequest {
        @NotBlank(message = "변수명은 필수입니다.")
        @Size(max = 100, message = "변수명은 100자를 초과할 수 없습니다.")
        private String variableName;

        @Size(max = 200, message = "표시명은 200자를 초과할 수 없습니다.")
        private String displayName;

        @Size(max = 50, message = "데이터 타입은 50자를 초과할 수 없습니다.")
        private String dataType;

        private String defaultValue;

        private String description;

        private boolean required = false;

        private String validationRule;
    }

    @Getter
    @NoArgsConstructor
    public static class GenerateDocumentRequest {
        @NotNull(message = "템플릿 ID는 필수입니다.")
        private Long templateId;

        @NotNull(message = "데이터는 필수입니다.")
        private Map<String, Object> data; // 템플릿 변수에 매핑될 데이터

        private String outputFormat; // PDF, WORD, EXCEL, HTML (템플릿 기본값 사용 시 null)

        private String languageCode; // 언어 코드 (ko, en, zh, ja 등)

        public void setTemplateId(Long templateId) {
            this.templateId = templateId;
        }

        public void setData(Map<String, Object> data) {
            this.data = data;
        }

        public void setOutputFormat(String outputFormat) {
            this.outputFormat = outputFormat;
        }

        public void setLanguageCode(String languageCode) {
            this.languageCode = languageCode;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class PreviewTemplateRequest {
        @NotNull(message = "템플릿 ID는 필수입니다.")
        private Long templateId;

        @NotNull(message = "데이터는 필수입니다.")
        private Map<String, Object> data;

        private String languageCode; // 언어 코드 (ko, en, zh, ja 등)

        public void setTemplateId(Long templateId) {
            this.templateId = templateId;
        }

        public void setData(Map<String, Object> data) {
            this.data = data;
        }

        public void setLanguageCode(String languageCode) {
            this.languageCode = languageCode;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class CreateVersionRequest {
        @NotBlank(message = "버전은 필수입니다.")
        @Size(max = 100, message = "버전은 100자를 초과할 수 없습니다.")
        private String version;

        @Size(max = 1000, message = "변경 사항 설명은 1000자를 초과할 수 없습니다.")
        private String changeDescription;

        @NotNull(message = "생성자 ID는 필수입니다.")
        private Long createdByUserId;
    }

    @Getter
    @NoArgsConstructor
    public static class CreateTranslationRequest {
        @NotBlank(message = "언어 코드는 필수입니다.")
        @Size(max = 10, message = "언어 코드는 10자를 초과할 수 없습니다.")
        private String languageCode;

        @NotBlank(message = "템플릿 이름은 필수입니다.")
        @Size(max = 100, message = "템플릿 이름은 100자를 초과할 수 없습니다.")
        private String templateName;

        @NotBlank(message = "템플릿 내용은 필수입니다.")
        private String content;

        @Size(max = 500, message = "설명은 500자를 초과할 수 없습니다.")
        private String description;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateTranslationRequest {
        @NotNull(message = "템플릿 ID는 필수입니다.")
        private Long templateId;

        @NotBlank(message = "언어 코드는 필수입니다.")
        private String languageCode;

        @NotBlank(message = "템플릿 이름은 필수입니다.")
        @Size(max = 100, message = "템플릿 이름은 100자를 초과할 수 없습니다.")
        private String templateName;

        @NotBlank(message = "템플릿 내용은 필수입니다.")
        private String content;

        @Size(max = 500, message = "설명은 500자를 초과할 수 없습니다.")
        private String description;
    }

    @Getter
    @NoArgsConstructor
    public static class ShareTemplateRequest {
        @NotNull(message = "병원 ID는 필수입니다.")
        private Long hospitalId;

        private boolean shared = true;
    }

    @Getter
    @NoArgsConstructor
    public static class PreviewRequest {
        @NotNull(message = "데이터는 필수입니다.")
        private Map<String, Object> data;

        private String languageCode; // 언어 코드 (선택)
    }
}

