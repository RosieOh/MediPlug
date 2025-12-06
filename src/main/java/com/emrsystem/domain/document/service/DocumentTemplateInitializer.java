package com.emrsystem.domain.document.service;

import com.emrsystem.domain.document.entity.DocumentTemplate;
import com.emrsystem.domain.document.entity.TemplateVariable;
import com.emrsystem.domain.document.store.DocumentTemplateStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 기본 문서 템플릿 초기화
 * 애플리케이션 시작 시 기본 템플릿을 생성합니다.
 */
@Slf4j
@Component
@Order(100)
@RequiredArgsConstructor
public class DocumentTemplateInitializer implements CommandLineRunner {

    private final DocumentTemplateStore templateStore;

    @Override
    @Transactional
    public void run(String... args) {
        // 이미 템플릿이 있으면 스킵
        if (!templateStore.findAllActiveTemplates().isEmpty()) {
            log.debug("기본 템플릿이 이미 존재합니다. 스킵합니다.");
            return;
        }

        log.info("기본 문서 템플릿을 생성합니다...");

        // 진단서 템플릿
        createDiagnosisCertificateTemplate();

        // 소견서 템플릿
        createMedicalOpinionTemplate();

        // 퇴원 요약서 템플릿
        createDischargeSummaryTemplate();

        log.info("기본 문서 템플릿 생성 완료");
    }

    private void createDiagnosisCertificateTemplate() {
        String content = """
            <!DOCTYPE html>
            <html xmlns:th="http://www.thymeleaf.org">
            <head>
                <meta charset="UTF-8">
                <title>진단서</title>
                <style>
                    body { font-family: 'Malgun Gothic', sans-serif; padding: 40px; }
                    .header { text-align: center; margin-bottom: 40px; }
                    .title { font-size: 24px; font-weight: bold; margin-bottom: 10px; }
                    .content { line-height: 1.8; margin: 20px 0; }
                    .signature { margin-top: 60px; text-align: right; }
                    .date { margin-top: 20px; }
                </style>
            </head>
            <body>
                <div class="header">
                    <div class="title">진 단 서</div>
                </div>
                <div class="content">
                    <p>성명: <span th:text="${patientName}">홍길동</span></p>
                    <p>생년월일: <span th:text="${patientBirthDate}">1990-01-01</span></p>
                    <p>주소: <span th:text="${patientAddress}">서울시 강남구</span></p>
                    <p>진단명: <span th:text="${diagnosis}">감기</span></p>
                    <p>진단일: <span th:text="${diagnosisDate}">2025-01-01</span></p>
                    <p>치료 기간: <span th:text="${treatmentPeriod}">3일</span></p>
                    <p>비고: <span th:text="${notes}">-</span></p>
                </div>
                <div class="signature">
                    <p>발급일: <span th:text="${#temporals.format(currentDate, 'yyyy년 MM월 dd일')}">2025년 01월 01일</span></p>
                    <p>의사: <span th:text="${doctorName}">김의사</span></p>
                    <p>병원명: <span th:text="${hospitalName}">메디플러그 병원</span></p>
                </div>
            </body>
            </html>
            """;

        DocumentTemplate template = DocumentTemplate.create(
                "기본 진단서",
                "DIAGNOSIS_CERTIFICATE",
                "PDF",
                content,
                "기본 진단서 템플릿",
                true,
                "1.0"
        );

        template = templateStore.saveTemplate(template);

        // 변수 추가
        addVariable(template, "patientName", "환자명", "STRING", null, "환자 이름", true, null);
        addVariable(template, "patientBirthDate", "생년월일", "DATE", null, "환자 생년월일", true, null);
        addVariable(template, "patientAddress", "주소", "STRING", null, "환자 주소", false, null);
        addVariable(template, "diagnosis", "진단명", "STRING", null, "진단명", true, null);
        addVariable(template, "diagnosisDate", "진단일", "DATE", null, "진단 날짜", true, null);
        addVariable(template, "treatmentPeriod", "치료 기간", "STRING", null, "치료 기간", false, null);
        addVariable(template, "notes", "비고", "STRING", "-", "추가 사항", false, null);
        addVariable(template, "doctorName", "의사명", "STRING", null, "담당 의사 이름", true, null);
        addVariable(template, "hospitalName", "병원명", "STRING", null, "병원 이름", true, null);

        templateStore.saveTemplate(template);
    }

    private void createMedicalOpinionTemplate() {
        String content = """
            <!DOCTYPE html>
            <html xmlns:th="http://www.thymeleaf.org">
            <head>
                <meta charset="UTF-8">
                <title>소견서</title>
                <style>
                    body { font-family: 'Malgun Gothic', sans-serif; padding: 40px; }
                    .header { text-align: center; margin-bottom: 40px; }
                    .title { font-size: 24px; font-weight: bold; margin-bottom: 10px; }
                    .content { line-height: 1.8; margin: 20px 0; }
                    .signature { margin-top: 60px; text-align: right; }
                </style>
            </head>
            <body>
                <div class="header">
                    <div class="title">소 견 서</div>
                </div>
                <div class="content">
                    <p>성명: <span th:text="${patientName}">홍길동</span></p>
                    <p>생년월일: <span th:text="${patientBirthDate}">1990-01-01</span></p>
                    <p>소견 내용:</p>
                    <p th:text="${opinion}">환자는 현재 건강한 상태입니다.</p>
                    <p>발급일: <span th:text="${#temporals.format(currentDate, 'yyyy년 MM월 dd일')}">2025년 01월 01일</span></p>
                </div>
                <div class="signature">
                    <p>의사: <span th:text="${doctorName}">김의사</span></p>
                    <p>병원명: <span th:text="${hospitalName}">메디플러그 병원</span></p>
                </div>
            </body>
            </html>
            """;

        DocumentTemplate template = DocumentTemplate.create(
                "기본 소견서",
                "MEDICAL_OPINION",
                "PDF",
                content,
                "기본 소견서 템플릿",
                true,
                "1.0"
        );

        template = templateStore.saveTemplate(template);

        addVariable(template, "patientName", "환자명", "STRING", null, "환자 이름", true, null);
        addVariable(template, "patientBirthDate", "생년월일", "DATE", null, "환자 생년월일", true, null);
        addVariable(template, "opinion", "소견 내용", "STRING", null, "의료 소견 내용", true, null);
        addVariable(template, "doctorName", "의사명", "STRING", null, "담당 의사 이름", true, null);
        addVariable(template, "hospitalName", "병원명", "STRING", null, "병원 이름", true, null);

        templateStore.saveTemplate(template);
    }

    private void createDischargeSummaryTemplate() {
        String content = """
            <!DOCTYPE html>
            <html xmlns:th="http://www.thymeleaf.org">
            <head>
                <meta charset="UTF-8">
                <title>퇴원 요약서</title>
                <style>
                    body { font-family: 'Malgun Gothic', sans-serif; padding: 40px; }
                    .header { text-align: center; margin-bottom: 40px; }
                    .title { font-size: 24px; font-weight: bold; margin-bottom: 10px; }
                    .content { line-height: 1.8; margin: 20px 0; }
                    table { width: 100%; border-collapse: collapse; margin: 20px 0; }
                    th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
                    th { background-color: #f2f2f2; }
                </style>
            </head>
            <body>
                <div class="header">
                    <div class="title">퇴 원 요 약 서</div>
                </div>
                <div class="content">
                    <p><strong>환자 정보</strong></p>
                    <p>성명: <span th:text="${patientName}">홍길동</span></p>
                    <p>생년월일: <span th:text="${patientBirthDate}">1990-01-01</span></p>
                    <p>입원일: <span th:text="${admissionDate}">2025-01-01</span></p>
                    <p>퇴원일: <span th:text="${dischargeDate}">2025-01-05</span></p>
                    <p><strong>진단명</strong></p>
                    <p th:text="${diagnosis}">감기</p>
                    <p><strong>치료 내용</strong></p>
                    <p th:text="${treatment}">항생제 투여 및 휴식</p>
                    <p><strong>퇴원 후 지시사항</strong></p>
                    <p th:text="${dischargeInstructions}">충분한 휴식 및 약물 복용</p>
                </div>
                <div style="margin-top: 60px; text-align: right;">
                    <p>의사: <span th:text="${doctorName}">김의사</span></p>
                    <p>병원명: <span th:text="${hospitalName}">메디플러그 병원</span></p>
                    <p>발급일: <span th:text="${#temporals.format(currentDate, 'yyyy년 MM월 dd일')}">2025년 01월 05일</span></p>
                </div>
            </body>
            </html>
            """;

        DocumentTemplate template = DocumentTemplate.create(
                "기본 퇴원 요약서",
                "DISCHARGE_SUMMARY",
                "PDF",
                content,
                "기본 퇴원 요약서 템플릿",
                true,
                "1.0"
        );

        template = templateStore.saveTemplate(template);

        addVariable(template, "patientName", "환자명", "STRING", null, "환자 이름", true, null);
        addVariable(template, "patientBirthDate", "생년월일", "DATE", null, "환자 생년월일", true, null);
        addVariable(template, "admissionDate", "입원일", "DATE", null, "입원 날짜", true, null);
        addVariable(template, "dischargeDate", "퇴원일", "DATE", null, "퇴원 날짜", true, null);
        addVariable(template, "diagnosis", "진단명", "STRING", null, "진단명", true, null);
        addVariable(template, "treatment", "치료 내용", "STRING", null, "치료 내용", true, null);
        addVariable(template, "dischargeInstructions", "퇴원 후 지시사항", "STRING", null, "퇴원 후 지시사항", false, null);
        addVariable(template, "doctorName", "의사명", "STRING", null, "담당 의사 이름", true, null);
        addVariable(template, "hospitalName", "병원명", "STRING", null, "병원 이름", true, null);

        templateStore.saveTemplate(template);
    }

    private void addVariable(DocumentTemplate template, String variableName, String displayName,
                            String dataType, String defaultValue, String description,
                            boolean required, String validationRule) {
        TemplateVariable variable = TemplateVariable.create(
                template, variableName, displayName, dataType, defaultValue,
                description, required, validationRule);
        template.addVariable(variable);
    }
}

