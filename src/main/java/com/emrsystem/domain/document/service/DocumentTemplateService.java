package com.emrsystem.domain.document.service;

import com.emrsystem.domain.document.entity.DocumentTemplate;
import com.emrsystem.domain.document.entity.TemplateVariable;
import com.emrsystem.domain.document.mapper.DocumentTemplateMapper;
import com.emrsystem.domain.document.request.DocumentTemplateRequests;
import com.emrsystem.domain.document.store.DocumentTemplateStore;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DocumentTemplateService {

    private final DocumentTemplateStore templateStore;
    private final TemplateVersionService versionService;
    private final DocumentTemplateMapper templateMapper;

    public DocumentTemplate getTemplate(Long id) {
        return templateStore.findTemplateById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "템플릿을 찾을 수 없습니다."));
    }

    public List<DocumentTemplate> getTemplatesByDocumentType(String documentType) {
        return templateStore.findTemplatesByDocumentType(documentType);
    }

    public List<DocumentTemplate> getActiveTemplatesByDocumentType(String documentType) {
        return templateStore.findActiveTemplatesByDocumentType(documentType);
    }

    public DocumentTemplate getDefaultTemplate(String documentType) {
        return templateStore.findDefaultTemplateByDocumentType(documentType)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND,
                        "기본 템플릿을 찾을 수 없습니다: " + documentType));
    }

    public List<DocumentTemplate> getAllActiveTemplates() {
        return templateStore.findAllActiveTemplates();
    }

    @Transactional
    public DocumentTemplate createTemplate(DocumentTemplateRequests.CreateTemplateRequest request) {
        if (templateStore.existsByTemplateName(request.getTemplateName())) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION,
                    "이미 존재하는 템플릿 이름입니다: " + request.getTemplateName());
        }

        DocumentTemplate template = templateMapper.toEntity(request);

        // 기본 템플릿으로 설정하는 경우, 같은 타입의 다른 기본 템플릿 해제
        if (request.isDefault()) {
            templateStore.findDefaultTemplateByDocumentType(request.getDocumentType())
                    .ifPresent(existingDefault -> existingDefault.unsetAsDefault());
        }

        template = templateStore.saveTemplate(template);

        // 변수 추가
        if (request.getVariables() != null) {
            List<TemplateVariable> variables = templateMapper.toVariableEntityList(request.getVariables(), template);
            variables.forEach(template::addVariable);
        }

        template = templateStore.saveTemplate(template);

        // 초기 버전 생성
        String initialVersion = request.getVersion() != null ? request.getVersion() : "1.0";
        versionService.createVersion(
                template.getId(),
                initialVersion,
                "초기 템플릿 생성",
                1L // TODO: 실제 사용자 ID로 변경 필요
        );

        return template;
    }

    @Transactional
    public DocumentTemplate updateTemplate(Long id, DocumentTemplateRequests.UpdateTemplateRequest request, Long userId) {
        DocumentTemplate template = getTemplate(id);
        String oldContent = template.getContent();
        
        // 템플릿 정보 업데이트
        templateMapper.updateEntity(template, request);

        // 변수 업데이트 (기존 변수 삭제 후 새로 추가)
        templateStore.deleteVariablesByTemplateId(id);
        if (request.getVariables() != null) {
            List<TemplateVariable> variables = templateMapper.toVariableEntityList(request.getVariables(), template);
            variables.forEach(template::addVariable);
        }

        template = templateStore.saveTemplate(template);

        // 내용이 변경된 경우 버전 생성
        if (!oldContent.equals(request.getContent()) && userId != null) {
            String newVersion = request.getVersion() != null
                    ? request.getVersion()
                    : incrementVersion(template.getVersion());
            versionService.createVersion(
                    template.getId(),
                    newVersion,
                    "템플릿 내용 업데이트",
                    userId
            );
        }

        return template;
    }

    /**
     * 버전 문자열을 증가시킵니다.
     * 예: "1.0" -> "1.1", "1.1" -> "1.2"
     */
    private String incrementVersion(String currentVersion) {
        if (currentVersion == null || currentVersion.isEmpty()) {
            return "1.0";
        }
        try {
            String[] parts = currentVersion.split("\\.");
            if (parts.length >= 2) {
                int minor = Integer.parseInt(parts[1]);
                return parts[0] + "." + (minor + 1);
            }
            return currentVersion + ".1";
        } catch (NumberFormatException e) {
            return currentVersion + ".1";
        }
    }

    @Transactional
    public void activateTemplate(Long id) {
        DocumentTemplate template = getTemplate(id);
        template.activate();
        templateStore.saveTemplate(template);
    }

    @Transactional
    public void deactivateTemplate(Long id) {
        DocumentTemplate template = getTemplate(id);
        template.deactivate();
        templateStore.saveTemplate(template);
    }

    @Transactional
    public void setAsDefault(Long id) {
        DocumentTemplate template = getTemplate(id);
        
        // 같은 타입의 다른 기본 템플릿 해제
        templateStore.findDefaultTemplateByDocumentType(template.getDocumentType())
                .ifPresent(existingDefault -> {
                    if (!existingDefault.getId().equals(id)) {
                        existingDefault.unsetAsDefault();
                        templateStore.saveTemplate(existingDefault);
                    }
                });

        template.setAsDefault();
        templateStore.saveTemplate(template);
    }

    @Transactional
    public void deleteTemplate(Long id) {
        // 템플릿 존재 여부만 확인
        if (!templateStore.findTemplateById(id).isPresent()) {
            throw new CommonException(ErrorCode.DATA_NOT_FOUND, "템플릿을 찾을 수 없습니다.");
        }
        templateStore.deleteTemplate(id);
    }
}

