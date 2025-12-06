package com.emrsystem.domain.document.service;

import com.emrsystem.domain.document.entity.DocumentTemplate;
import com.emrsystem.domain.document.entity.TemplateVersion;
import com.emrsystem.domain.document.store.DocumentTemplateStore;
import com.emrsystem.domain.user.entity.UserAccount;
import com.emrsystem.domain.user.repository.UserAccountRepository;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TemplateVersionService {

    private final DocumentTemplateStore templateStore;
    private final UserAccountRepository userAccountRepository;

    public List<TemplateVersion> getVersionsByTemplate(Long templateId) {
        // 템플릿 존재 여부만 확인
        if (!templateStore.findTemplateById(templateId).isPresent()) {
            throw new CommonException(ErrorCode.DATA_NOT_FOUND, "템플릿을 찾을 수 없습니다.");
        }
        return templateStore.findVersionsByTemplateId(templateId);
    }

    public TemplateVersion getVersion(Long templateId, String version) {
        return templateStore.findVersionByTemplateIdAndVersion(templateId, version)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND,
                        "버전을 찾을 수 없습니다: " + version));
    }

    @Transactional
    public TemplateVersion createVersion(Long templateId, String version, String changeDescription, Long createdByUserId) {
        DocumentTemplate template = templateStore.findTemplateById(templateId)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "템플릿을 찾을 수 없습니다."));

        // 이미 존재하는 버전인지 확인
        if (templateStore.findVersionByTemplateIdAndVersion(templateId, version).isPresent()) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION,
                    "이미 존재하는 버전입니다: " + version);
        }

        UserAccount createdBy = userAccountRepository.findById(createdByUserId)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "사용자를 찾을 수 없습니다."));

        TemplateVersion templateVersion = TemplateVersion.create(
                template,
                version,
                template.getContent(), // 현재 템플릿 내용 저장
                changeDescription,
                createdBy,
                null // 메타데이터는 나중에 추가 가능
        );

        return templateStore.saveTemplateVersion(templateVersion);
    }

    @Transactional
    public TemplateVersion restoreVersion(Long templateId, String version) {
        TemplateVersion templateVersion = getVersion(templateId, version);
        DocumentTemplate template = templateStore.findTemplateById(templateId)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "템플릿을 찾을 수 없습니다."));

        // 템플릿 내용을 해당 버전으로 복원
        template.update(template.getTemplateName(), templateVersion.getContent(),
                template.getDescription(), version);

        return templateStore.saveTemplate(template) != null ? templateVersion : null;
    }
}
