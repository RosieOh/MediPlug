package com.emrsystem.domain.document.mapper;

import com.emrsystem.domain.document.entity.DocumentTemplate;
import com.emrsystem.domain.document.entity.TemplateVariable;
import com.emrsystem.domain.document.request.DocumentTemplateRequests;
import com.emrsystem.domain.document.response.DocumentTemplateResponses;
import com.emrsystem.global.common.mapper.EntityMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * DocumentTemplate Entity와 DTO 간 변환을 담당하는 매퍼
 */
@Component
public class DocumentTemplateMapper implements EntityMapper<DocumentTemplate, DocumentTemplateResponses.TemplateDetail> {

    @Override
    public DocumentTemplateResponses.TemplateDetail toDto(DocumentTemplate entity) {
        return DocumentTemplateResponses.TemplateDetail.from(entity);
    }

    @Override
    public DocumentTemplate toEntity(DocumentTemplateResponses.TemplateDetail dto) {
        // DTO에서 Entity로의 직접 변환은 일반적으로 사용되지 않음
        // 대신 CreateRequest를 사용하여 Entity를 생성
        throw new UnsupportedOperationException("DTO에서 Entity로의 직접 변환은 지원하지 않습니다. CreateRequest를 사용하세요.");
    }

    @Override
    public void updateEntity(DocumentTemplate entity, DocumentTemplateResponses.TemplateDetail dto) {
        // DTO에서 Entity 업데이트는 일반적으로 사용되지 않음
        // 대신 UpdateRequest를 사용하여 Entity를 업데이트
        throw new UnsupportedOperationException("DTO에서 Entity 업데이트는 지원하지 않습니다. UpdateRequest를 사용하세요.");
    }

    /**
     * Entity를 Summary DTO로 변환
     */
    public DocumentTemplateResponses.TemplateSummary toSummary(DocumentTemplate entity) {
        return DocumentTemplateResponses.TemplateSummary.from(entity);
    }

    /**
     * CreateRequest를 Entity로 변환
     */
    public DocumentTemplate toEntity(DocumentTemplateRequests.CreateTemplateRequest request) {
        return DocumentTemplate.create(
                request.getTemplateName(),
                request.getDocumentType(),
                request.getFormat(),
                request.getContent(),
                request.getDescription(),
                request.isDefault(),
                request.getVersion() != null ? request.getVersion() : "1.0"
        );
    }

    /**
     * UpdateRequest로 Entity 업데이트
     */
    public void updateEntity(DocumentTemplate entity, DocumentTemplateRequests.UpdateTemplateRequest request) {
        entity.update(
                request.getTemplateName(),
                request.getContent(),
                request.getDescription(),
                request.getVersion() != null ? request.getVersion() : entity.getVersion()
        );
    }

    /**
     * VariableRequest를 TemplateVariable Entity로 변환
     */
    public TemplateVariable toEntity(DocumentTemplateRequests.VariableRequest request, DocumentTemplate template) {
        return TemplateVariable.create(
                template,
                request.getVariableName(),
                request.getDisplayName(),
                request.getDataType(),
                request.getDefaultValue(),
                request.getDescription(),
                request.isRequired(),
                request.getValidationRule()
        );
    }

    /**
     * VariableRequest 리스트를 TemplateVariable Entity 리스트로 변환
     */
    public List<TemplateVariable> toVariableEntityList(
            List<DocumentTemplateRequests.VariableRequest> requests,
            DocumentTemplate template) {
        if (requests == null) {
            return List.of();
        }
        return requests.stream()
                .map(request -> toEntity(request, template))
                .collect(Collectors.toList());
    }
}

