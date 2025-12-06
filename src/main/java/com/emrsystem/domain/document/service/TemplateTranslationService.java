package com.emrsystem.domain.document.service;

import com.emrsystem.domain.document.entity.DocumentTemplate;
import com.emrsystem.domain.document.entity.TemplateTranslation;
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
public class TemplateTranslationService {

    private final DocumentTemplateStore templateStore;

    public List<TemplateTranslation> getTranslationsByTemplate(Long templateId) {
        // 템플릿 존재 여부만 확인
        if (!templateStore.findTemplateById(templateId).isPresent()) {
            throw new CommonException(ErrorCode.DATA_NOT_FOUND, "템플릿을 찾을 수 없습니다.");
        }
        return templateStore.findTranslationsByTemplateId(templateId);
    }

    public TemplateTranslation getTranslation(Long templateId, String languageCode) {
        return templateStore.findTranslationByTemplateIdAndLanguage(templateId, languageCode)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND,
                        "번역을 찾을 수 없습니다: " + languageCode));
    }

    public List<TemplateTranslation> getActiveTranslationsByLanguage(String languageCode) {
        return templateStore.findActiveTranslationsByLanguage(languageCode);
    }

    @Transactional
    public TemplateTranslation createTranslation(Long templateId, String languageCode,
                                                String templateName, String content, String description) {
        DocumentTemplate template = templateStore.findTemplateById(templateId)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "템플릿을 찾을 수 없습니다."));

        // 이미 존재하는 번역인지 확인
        if (templateStore.findTranslationByTemplateIdAndLanguage(templateId, languageCode).isPresent()) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION,
                    "이미 존재하는 번역입니다: " + languageCode);
        }

        TemplateTranslation translation = TemplateTranslation.create(
                template, languageCode, templateName, content, description);

        return templateStore.saveTranslation(translation);
    }

    @Transactional
    public TemplateTranslation updateTranslation(Long templateId, String languageCode,
                                                 String templateName, String content, String description) {
        TemplateTranslation translation = getTranslation(templateId, languageCode);
        translation.update(templateName, content, description);
        return templateStore.saveTranslation(translation);
    }

    @Transactional
    public void activateTranslation(Long templateId, String languageCode) {
        TemplateTranslation translation = getTranslation(templateId, languageCode);
        translation.activate();
        templateStore.saveTranslation(translation);
    }

    @Transactional
    public void deactivateTranslation(Long templateId, String languageCode) {
        TemplateTranslation translation = getTranslation(templateId, languageCode);
        translation.deactivate();
        templateStore.saveTranslation(translation);
    }
}
