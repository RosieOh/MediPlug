package com.emrsystem.domain.document.response;

import com.emrsystem.domain.document.entity.DocumentTemplate;
import com.emrsystem.domain.document.entity.TemplateVariable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class DocumentTemplateResponses {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TemplateSummary {
        private Long templateId;
        private String templateName;
        private String documentType;
        private String format;
        private String description;
        private boolean active;
        private boolean isDefault;
        private String version;
        private int variableCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static TemplateSummary from(DocumentTemplate template) {
            return TemplateSummary.builder()
                    .templateId(template.getId())
                    .templateName(template.getTemplateName())
                    .documentType(template.getDocumentType())
                    .format(template.getFormat())
                    .description(template.getDescription())
                    .active(template.isActive())
                    .isDefault(template.isDefault())
                    .version(template.getVersion())
                    .variableCount(template.getVariables() != null ? template.getVariables().size() : 0)
                    .createdAt(template.getCreatedAt())
                    .updatedAt(template.getUpdatedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TemplateDetail {
        private Long templateId;
        private String templateName;
        private String documentType;
        private String format;
        private String content;
        private String description;
        private boolean active;
        private boolean isDefault;
        private String version;
        private List<VariableSummary> variables;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static TemplateDetail from(DocumentTemplate template) {
            return TemplateDetail.builder()
                    .templateId(template.getId())
                    .templateName(template.getTemplateName())
                    .documentType(template.getDocumentType())
                    .format(template.getFormat())
                    .content(template.getContent())
                    .description(template.getDescription())
                    .active(template.isActive())
                    .isDefault(template.isDefault())
                    .version(template.getVersion())
                    .variables(template.getVariables() != null
                            ? template.getVariables().stream()
                                    .map(VariableSummary::from)
                                    .collect(Collectors.toList())
                            : List.of())
                    .createdAt(template.getCreatedAt())
                    .updatedAt(template.getUpdatedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VariableSummary {
        private Long variableId;
        private String variableName;
        private String displayName;
        private String dataType;
        private String defaultValue;
        private String description;
        private boolean required;
        private String validationRule;

        public static VariableSummary from(TemplateVariable variable) {
            return VariableSummary.builder()
                    .variableId(variable.getVariableId())
                    .variableName(variable.getVariableName())
                    .displayName(variable.getDisplayName())
                    .dataType(variable.getDataType())
                    .defaultValue(variable.getDefaultValue())
                    .description(variable.getDescription())
                    .required(variable.isRequired())
                    .validationRule(variable.getValidationRule())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VersionSummary {
        private Long versionId;
        private String version;
        private String changeDescription;
        private String createdBy;
        private LocalDateTime createdAt;

        public static VersionSummary from(com.emrsystem.domain.document.entity.TemplateVersion version) {
            return VersionSummary.builder()
                    .versionId(version.getId())
                    .version(version.getVersion())
                    .changeDescription(version.getChangeDescription())
                    .createdBy(version.getCreatedBy() != null ? version.getCreatedBy().getUsername() : null)
                    .createdAt(version.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TranslationSummary {
        private Long translationId;
        private String languageCode;
        private String templateName;
        private String description;
        private boolean active;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static TranslationSummary from(com.emrsystem.domain.document.entity.TemplateTranslation translation) {
            return TranslationSummary.builder()
                    .translationId(translation.getId())
                    .languageCode(translation.getLanguageCode())
                    .templateName(translation.getTemplateName())
                    .description(translation.getDescription())
                    .active(translation.isActive())
                    .createdAt(translation.getCreatedAt())
                    .updatedAt(translation.getUpdatedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HospitalTemplateSummary {
        private Long hospitalTemplateId;
        private Long templateId;
        private String templateName;
        private Long hospitalId;
        private String hospitalName;
        private boolean shared;
        private boolean active;
        private LocalDateTime createdAt;

        public static HospitalTemplateSummary from(com.emrsystem.domain.document.entity.HospitalTemplate hospitalTemplate) {
            return HospitalTemplateSummary.builder()
                    .hospitalTemplateId(hospitalTemplate.getId())
                    .templateId(hospitalTemplate.getTemplate().getId())
                    .templateName(hospitalTemplate.getTemplate().getTemplateName())
                    .hospitalId(hospitalTemplate.getHospital().getId())
                    .hospitalName(hospitalTemplate.getHospital().getName())
                    .shared(hospitalTemplate.isShared())
                    .active(hospitalTemplate.isActive())
                    .createdAt(hospitalTemplate.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssetSummary {
        private Long assetId;
        private String assetName;
        private String assetType;
        private String filePath;
        private String mimeType;
        private Long fileSize;
        private String description;
        private LocalDateTime createdAt;

        public static AssetSummary from(com.emrsystem.domain.document.entity.TemplateAsset asset) {
            return AssetSummary.builder()
                    .assetId(asset.getId())
                    .assetName(asset.getAssetName())
                    .assetType(asset.getAssetType())
                    .filePath(asset.getFilePath())
                    .mimeType(asset.getMimeType())
                    .fileSize(asset.getFileSize())
                    .description(asset.getDescription())
                    .createdAt(asset.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PreviewResponse {
        private String htmlContent;
        private String templateName;
        private String documentType;
    }
}

