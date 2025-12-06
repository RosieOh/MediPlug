package com.emrsystem.domain.document.service;

import com.emrsystem.domain.document.entity.DocumentTemplate;
import com.emrsystem.domain.document.store.DocumentTemplateStore;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

/**
 * 템플릿 렌더링 관련 공통 로직을 제공하는 헬퍼 클래스
 */
@Component
@RequiredArgsConstructor
public class TemplateRenderingHelper {

    private final TemplateEngine templateEngine;
    private final DocumentTemplateStore templateStore;

    /**
     * 템플릿 내용을 렌더링합니다.
     *
     * @param templateContent 템플릿 내용
     * @param data            템플릿 변수 데이터
     * @return 렌더링된 HTML 문자열
     */
    public String renderTemplateContent(String templateContent, Map<String, Object> data) {
        Context context = new Context();
        context.setVariables(data);

        // 기본 변수 추가
        context.setVariable("currentDate", java.time.LocalDate.now());
        context.setVariable("currentTime", java.time.LocalDateTime.now());
        context.setVariable("currentYear", java.time.Year.now().getValue());

        return templateEngine.process(templateContent, context);
    }

    /**
     * 템플릿 변수를 검증합니다.
     *
     * @param template 템플릿 엔티티
     * @param data     템플릿 변수 데이터
     * @throws CommonException 필수 변수가 누락된 경우
     */
    public void validateTemplateVariables(DocumentTemplate template, Map<String, Object> data) {
        if (template.getVariables() == null) {
            return;
        }

        for (var variable : template.getVariables()) {
            if (variable.isRequired() && !data.containsKey(variable.getVariableName())) {
                throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION,
                        "필수 변수가 누락되었습니다: " + variable.getVariableName());
            }
        }
    }

    /**
     * 다국어 지원을 위해 템플릿 내용을 가져옵니다.
     * 언어 코드가 제공된 경우 번역된 템플릿을 반환하고, 그렇지 않으면 원본 템플릿을 반환합니다.
     *
     * @param templateId   템플릿 ID
     * @param languageCode 언어 코드 (선택적)
     * @return 템플릿 내용
     */
    public String getTemplateContent(Long templateId, String languageCode) {
        DocumentTemplate template = templateStore.findTemplateById(templateId)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "템플릿을 찾을 수 없습니다."));

        String templateContent = template.getContent();

        // 다국어 지원: 언어 코드가 제공된 경우 번역된 템플릿 사용
        if (languageCode != null && !languageCode.isEmpty()) {
            templateStore.findTranslationByTemplateIdAndLanguage(templateId, languageCode)
                    .ifPresent(translation -> {
                        if (translation.isActive()) {
                            templateContent = translation.getContent();
                        }
                    });
        }

        return templateContent;
    }

    /**
     * 템플릿과 다국어 지원을 포함하여 렌더링합니다.
     *
     * @param templateId   템플릿 ID
     * @param data         템플릿 변수 데이터
     * @param languageCode 언어 코드 (선택적)
     * @return 렌더링된 HTML 문자열
     */
    public String renderTemplate(Long templateId, Map<String, Object> data, String languageCode) {
        DocumentTemplate template = templateStore.findTemplateById(templateId)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "템플릿을 찾을 수 없습니다."));
        return renderTemplate(template, data, languageCode);
    }

    /**
     * 템플릿과 다국어 지원을 포함하여 렌더링합니다.
     * 이미 조회된 템플릿을 사용하여 중복 조회를 방지합니다.
     *
     * @param template     템플릿 엔티티
     * @param data         템플릿 변수 데이터
     * @param languageCode 언어 코드 (선택적)
     * @return 렌더링된 HTML 문자열
     */
    public String renderTemplate(DocumentTemplate template, Map<String, Object> data, String languageCode) {
        // 템플릿 변수 검증
        validateTemplateVariables(template, data);

        // 템플릿 내용 가져오기 (다국어 지원 포함)
        String templateContent = template.getContent();
        if (languageCode != null && !languageCode.isEmpty()) {
            templateStore.findTranslationByTemplateIdAndLanguage(template.getId(), languageCode)
                    .ifPresent(translation -> {
                        if (translation.isActive()) {
                            templateContent = translation.getContent();
                        }
                    });
        }

        // 템플릿 렌더링
        return renderTemplateContent(templateContent, data);
    }
}

