package com.emrsystem.domain.report.generator;

import com.emrsystem.domain.bed.entity.Admission;
import com.emrsystem.domain.emr.entity.LabResult;
import com.emrsystem.domain.emr.entity.MedicalRecord;
import com.emrsystem.domain.patient.entity.Patient;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
public class ExcelReportGenerator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 진료 요약 리포트 생성
     */
    public Resource generateMedicalSummaryReport(Patient patient, List<MedicalRecord> medicalRecords) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("진료 요약");

            int rowNum = 0;

            // 헤더
            Row headerRow = sheet.createRow(rowNum++);
            Cell headerCell = headerRow.createCell(0);
            headerCell.setCellValue("진료 요약 리포트");
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 16);
            headerStyle.setFont(headerFont);
            headerCell.setCellStyle(headerStyle);

            // 환자 정보
            rowNum++;
            createLabelValueRow(sheet, rowNum++, "환자명", patient.getName());
            createLabelValueRow(sheet, rowNum++, "생년월일", patient.getBirthDate().format(DATE_FORMATTER));
            createLabelValueRow(sheet, rowNum++, "성별", patient.getGender());
            createLabelValueRow(sheet, rowNum++, "전화번호", patient.getPhone());

            // 진료 기록
            rowNum++;
            Row sectionRow = sheet.createRow(rowNum++);
            Cell sectionCell = sectionRow.createCell(0);
            sectionCell.setCellValue("진료 기록");
            CellStyle sectionStyle = workbook.createCellStyle();
            Font sectionFont = workbook.createFont();
            sectionFont.setBold(true);
            sectionStyle.setFont(sectionFont);
            sectionCell.setCellStyle(sectionStyle);

            if (medicalRecords.isEmpty()) {
                createLabelValueRow(sheet, rowNum++, "내용", "진료 기록이 없습니다.");
            } else {
                for (MedicalRecord record : medicalRecords) {
                    createLabelValueRow(sheet, rowNum++, "진료일", 
                            record.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                    createLabelValueRow(sheet, rowNum++, "의사", record.getDoctor().getName());
                    if (record.getChiefComplaint() != null && !record.getChiefComplaint().isEmpty()) {
                        createLabelValueRow(sheet, rowNum++, "주소", record.getChiefComplaint());
                    }
                    if (record.getDiagnosis() != null && !record.getDiagnosis().isEmpty()) {
                        createLabelValueRow(sheet, rowNum++, "진단", record.getDiagnosis());
                    }
                    if (record.getTreatment() != null && !record.getTreatment().isEmpty()) {
                        createLabelValueRow(sheet, rowNum++, "치료", record.getTreatment());
                    }
                    rowNum++;
                }
            }

            // 컬럼 너비 자동 조정
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            return new ByteArrayResource(baos.toByteArray());
        }
    }

    /**
     * 검사 결과 리포트 생성
     */
    public Resource generateLabResultsReport(Patient patient, List<LabResult> labResults,
                                            LocalDate startDate, LocalDate endDate) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("검사 결과");

            int rowNum = 0;

            // 헤더
            Row headerRow = sheet.createRow(rowNum++);
            Cell headerCell = headerRow.createCell(0);
            headerCell.setCellValue("검사 결과 리포트");

            // 환자 정보
            rowNum++;
            createLabelValueRow(sheet, rowNum++, "환자명", patient.getName());
            createLabelValueRow(sheet, rowNum++, "기간", 
                    startDate.format(DATE_FORMATTER) + " ~ " + endDate.format(DATE_FORMATTER));

            // 검사 결과 테이블
            rowNum++;
            Row tableHeaderRow = sheet.createRow(rowNum++);
            String[] headers = {"검사 항목", "결과값", "단위", "이상 여부"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = tableHeaderRow.createCell(i);
                cell.setCellValue(headers[i]);
                CellStyle style = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                style.setFont(font);
                cell.setCellStyle(style);
            }

            if (labResults.isEmpty()) {
                Row dataRow = sheet.createRow(rowNum++);
                dataRow.createCell(0).setCellValue("검사 결과가 없습니다.");
            } else {
                for (LabResult result : labResults) {
                    Row dataRow = sheet.createRow(rowNum++);
                    dataRow.createCell(0).setCellValue(result.getTestItemName());
                    dataRow.createCell(1).setCellValue(result.getResultValue() != null ? result.getResultValue() : "");
                    dataRow.createCell(2).setCellValue(result.getUnit() != null ? result.getUnit() : "");
                    dataRow.createCell(3).setCellValue(result.getAbnormalFlag() != null ? result.getAbnormalFlag() : "");
                }
            }

            // 컬럼 너비 자동 조정
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            return new ByteArrayResource(baos.toByteArray());
        }
    }

    /**
     * 입원 환자 리포트 생성
     */
    public Resource generateInpatientReport(Admission admission) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("입원 환자");

            int rowNum = 0;

            Row headerRow = sheet.createRow(rowNum++);
            headerRow.createCell(0).setCellValue("입원 환자 리포트");

            rowNum++;
            createLabelValueRow(sheet, rowNum++, "환자명", admission.getPatient().getName());
            createLabelValueRow(sheet, rowNum++, "입원일", 
                    admission.getAdmissionDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            createLabelValueRow(sheet, rowNum++, "병상", admission.getBed().getBedNumber());
            createLabelValueRow(sheet, rowNum++, "상태", admission.getStatus());

            if (admission.getDischargeDate() != null) {
                createLabelValueRow(sheet, rowNum++, "퇴원일", 
                        admission.getDischargeDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            }

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            return new ByteArrayResource(baos.toByteArray());
        }
    }

    /**
     * 통계 리포트 생성
     */
    public Resource generateStatisticsReport(LocalDate startDate, LocalDate endDate) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("통계");

            int rowNum = 0;

            Row headerRow = sheet.createRow(rowNum++);
            headerRow.createCell(0).setCellValue("통계 리포트");

            rowNum++;
            createLabelValueRow(sheet, rowNum++, "기간", 
                    startDate.format(DATE_FORMATTER) + " ~ " + endDate.format(DATE_FORMATTER));
            createLabelValueRow(sheet, rowNum++, "내용", "통계 데이터는 추후 구현 예정입니다.");

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            return new ByteArrayResource(baos.toByteArray());
        }
    }

    private void createLabelValueRow(Sheet sheet, int rowNum, String label, String value) {
        Row row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue(label);
        row.createCell(1).setCellValue(value);
    }
}

