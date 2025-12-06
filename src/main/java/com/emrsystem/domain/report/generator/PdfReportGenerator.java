package com.emrsystem.domain.report.generator;

import com.emrsystem.domain.bed.entity.Admission;
import com.emrsystem.domain.emr.entity.LabResult;
import com.emrsystem.domain.emr.entity.MedicalRecord;
import com.emrsystem.domain.patient.entity.Patient;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Component
public class PdfReportGenerator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 진료 요약 리포트 생성
     */
    public Resource generateMedicalSummaryReport(Patient patient, List<MedicalRecord> medicalRecords) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, baos);
        document.open();

        try {
            // 헤더
            addHeader(document, "진료 요약 리포트");

            // 환자 정보
            addPatientInfo(document, patient);

            // 진료 기록
            addSectionTitle(document, "진료 기록");
            if (medicalRecords.isEmpty()) {
                addParagraph(document, "진료 기록이 없습니다.");
            } else {
                for (MedicalRecord record : medicalRecords) {
                    addMedicalRecord(document, record);
                }
            }

            document.close();
            return new ByteArrayResource(baos.toByteArray());
        } catch (DocumentException e) {
            document.close();
            throw new IOException("PDF 생성 중 오류 발생", e);
        }
    }

    /**
     * 검사 결과 리포트 생성
     */
    public Resource generateLabResultsReport(Patient patient, List<LabResult> labResults, 
                                            LocalDate startDate, LocalDate endDate) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, baos);
        document.open();

        try {
            addHeader(document, "검사 결과 리포트");
            addPatientInfo(document, patient);
            addParagraph(document, "기간: " + startDate.format(DATE_FORMATTER) + " ~ " + endDate.format(DATE_FORMATTER));

            addSectionTitle(document, "검사 결과");
            if (labResults.isEmpty()) {
                addParagraph(document, "검사 결과가 없습니다.");
            } else {
                for (LabResult result : labResults) {
                    addLabResult(document, result);
                }
            }

            document.close();
            return new ByteArrayResource(baos.toByteArray());
        } catch (DocumentException e) {
            document.close();
            throw new IOException("PDF 생성 중 오류 발생", e);
        }
    }

    /**
     * 입원 환자 리포트 생성
     */
    public Resource generateInpatientReport(Admission admission) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, baos);
        document.open();

        try {
            addHeader(document, "입원 환자 리포트");
            addParagraph(document, "환자명: " + admission.getPatient().getName());
            addParagraph(document, "입원일: " + admission.getAdmissionDate().format(DATE_FORMATTER));
            addParagraph(document, "병상: " + admission.getBed().getBedNumber());
            addParagraph(document, "상태: " + admission.getStatus());

            if (admission.getDischargeDate() != null) {
                addParagraph(document, "퇴원일: " + admission.getDischargeDate().format(DATE_FORMATTER));
            }

            document.close();
            return new ByteArrayResource(baos.toByteArray());
        } catch (DocumentException e) {
            document.close();
            throw new IOException("PDF 생성 중 오류 발생", e);
        }
    }

    /**
     * 통계 리포트 생성
     */
    public Resource generateStatisticsReport(LocalDate startDate, LocalDate endDate) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, baos);
        document.open();

        try {
            addHeader(document, "통계 리포트");
            addParagraph(document, "기간: " + startDate.format(DATE_FORMATTER) + " ~ " + endDate.format(DATE_FORMATTER));
            addParagraph(document, "통계 데이터는 추후 구현 예정입니다.");

            document.close();
            return new ByteArrayResource(baos.toByteArray());
        } catch (DocumentException e) {
            document.close();
            throw new IOException("PDF 생성 중 오류 발생", e);
        }
    }

    // Helper methods
    private void addHeader(Document document, String title) throws DocumentException {
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph titlePara = new Paragraph(title, titleFont);
        titlePara.setAlignment(Element.ALIGN_CENTER);
        titlePara.setSpacingAfter(20);
        document.add(titlePara);
    }

    private void addPatientInfo(Document document, Patient patient) throws DocumentException {
        addSectionTitle(document, "환자 정보");
        addParagraph(document, "이름: " + patient.getName());
        addParagraph(document, "생년월일: " + patient.getBirthDate().format(DATE_FORMATTER));
        addParagraph(document, "성별: " + patient.getGender());
        addParagraph(document, "전화번호: " + patient.getPhone());
    }

    private void addSectionTitle(Document document, String title) throws DocumentException {
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Paragraph sectionPara = new Paragraph(title, sectionFont);
        sectionPara.setSpacingBefore(10);
        sectionPara.setSpacingAfter(5);
        document.add(sectionPara);
    }

    private void addParagraph(Document document, String text) throws DocumentException {
        Paragraph para = new Paragraph(text);
        para.setSpacingAfter(5);
        document.add(para);
    }

    private void addMedicalRecord(Document document, MedicalRecord record) throws DocumentException {
        addParagraph(document, "진료일: " + record.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        addParagraph(document, "의사: " + record.getDoctor().getName());
        if (record.getChiefComplaint() != null && !record.getChiefComplaint().isEmpty()) {
            addParagraph(document, "주소: " + record.getChiefComplaint());
        }
        if (record.getDiagnosis() != null && !record.getDiagnosis().isEmpty()) {
            addParagraph(document, "진단: " + record.getDiagnosis());
        }
        if (record.getTreatment() != null && !record.getTreatment().isEmpty()) {
            addParagraph(document, "치료: " + record.getTreatment());
        }
        document.add(new Paragraph("---"));
    }

    private void addLabResult(Document document, LabResult result) throws DocumentException {
        addParagraph(document, "검사 항목: " + result.getTestItemName());
        addParagraph(document, "결과값: " + result.getResultValue());
        if (result.getUnit() != null) {
            addParagraph(document, "단위: " + result.getUnit());
        }
        if (result.getAbnormalFlag() != null) {
            addParagraph(document, "이상 여부: " + result.getAbnormalFlag());
        }
        document.add(new Paragraph("---"));
    }
}

