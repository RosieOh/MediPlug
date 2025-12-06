package com.emrsystem.domain.document.service;

import com.emrsystem.domain.document.entity.DocumentTemplate;
import com.emrsystem.domain.document.request.DocumentTemplateRequests;
import com.emrsystem.domain.document.store.DocumentTemplateStore;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DocumentGenerationService {

    private final DocumentTemplateStore templateStore;
    private final DocumentConverterService converterService;
    private final TemplateRenderingHelper renderingHelper;

    /**
     * 템플릿 기반 문서 생성
     */
    public Resource generateDocument(DocumentTemplateRequests.GenerateDocumentRequest request) {
        DocumentTemplate template = templateStore.findTemplateById(request.getTemplateId())
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "템플릿을 찾을 수 없습니다."));

        if (!template.isActive()) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION, "비활성화된 템플릿입니다.");
        }

        // 템플릿 렌더링 (다국어 지원 포함, 변수 검증 포함)
        String renderedContent = renderingHelper.renderTemplate(
                template, request.getData(), request.getLanguageCode());

        // 출력 포맷 결정
        String outputFormat = request.getOutputFormat() != null
                ? request.getOutputFormat().toUpperCase()
                : template.getFormat().toUpperCase();

        // 문서 변환
        try {
            return convertToFormat(renderedContent, outputFormat, template.getTemplateName());
        } catch (IOException e) {
            log.error("문서 생성 중 오류 발생", e);
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION,
                    "문서 생성에 실패했습니다: " + e.getMessage());
        }
    }

    /**
     * 기본 템플릿으로 문서 생성
     */
    public Resource generateDocumentByType(String documentType, Map<String, Object> data, String outputFormat) {
        DocumentTemplate template = templateStore.findDefaultTemplateByDocumentType(documentType)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND,
                        "기본 템플릿을 찾을 수 없습니다: " + documentType));

        DocumentTemplateRequests.GenerateDocumentRequest request = createGenerateRequest(
                template.getId(), data, outputFormat);

        return generateDocument(request);
    }

    private DocumentTemplateRequests.GenerateDocumentRequest createGenerateRequest(
            Long templateId, Map<String, Object> data, String outputFormat) {
        DocumentTemplateRequests.GenerateDocumentRequest request =
                new DocumentTemplateRequests.GenerateDocumentRequest();
        request.setTemplateId(templateId);
        request.setData(data);
        request.setOutputFormat(outputFormat);
        return request;
    }

    /**
     * 템플릿 미리보기 (HTML 반환)
     */
    public String previewTemplate(Long templateId, Map<String, Object> data, String languageCode) {
        // renderTemplate 메서드가 이미 템플릿 조회 및 변수 검증을 포함하고 있음
        return renderingHelper.renderTemplate(templateId, data, languageCode);
    }

    /**
     * 템플릿 미리보기 (HTML 반환) - 이미 조회된 템플릿 사용
     */
    public String previewTemplate(DocumentTemplate template, Map<String, Object> data, String languageCode) {
        // 이미 조회된 템플릿을 사용하여 중복 조회 방지
        return renderingHelper.renderTemplate(template, data, languageCode);
    }

    /**
     * 렌더링된 내용을 지정된 포맷으로 변환
     */
    private Resource convertToFormat(String content, String format, String templateName) throws IOException {
        return switch (format.toUpperCase()) {
            case "PDF" -> converterService.convertHtmlToPdf(content, templateName);
            case "WORD" -> converterService.convertHtmlToWord(content, templateName);
            case "EXCEL" -> converterService.convertHtmlToExcel(content, templateName);
            case "HTML" -> new ByteArrayResource(content.getBytes(StandardCharsets.UTF_8));
            default -> throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION,
                    "지원하지 않는 포맷입니다: " + format);
        };
    }
}

