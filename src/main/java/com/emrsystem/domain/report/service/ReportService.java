package com.emrsystem.domain.report.service;

import com.emrsystem.domain.bed.entity.Admission;
import com.emrsystem.domain.bed.store.BedStore;
import com.emrsystem.domain.emr.entity.LabResult;
import com.emrsystem.domain.emr.entity.MedicalRecord;
import com.emrsystem.domain.emr.store.EmrStore;
import com.emrsystem.domain.patient.entity.Patient;
import com.emrsystem.domain.patient.store.PatientStore;
import com.emrsystem.domain.report.enums.ReportType;
import com.emrsystem.domain.report.generator.ExcelReportGenerator;
import com.emrsystem.domain.report.generator.PdfReportGenerator;
import com.emrsystem.domain.report.request.ReportRequests;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final PatientStore patientStore;
    private final EmrStore emrStore;
    private final BedStore bedStore;
    private final PdfReportGenerator pdfReportGenerator;
    private final ExcelReportGenerator excelReportGenerator;

    /**
     * 리포트 생성
     */
    public Resource generateReport(ReportRequests.GenerateReportRequest request) {
        ReportType reportType = ReportType.fromString(request.getReportType());
        String format = request.getFormat().toUpperCase();

        try {
            switch (reportType) {
                case MEDICAL_SUMMARY:
                    return generateMedicalSummaryReport(request.getPatientId(), format);
                case LAB_RESULTS:
                    return generateLabResultsReport(request.getPatientId(), request.getStartDate(), request.getEndDate(), format);
                case INPATIENT:
                    return generateInpatientReport(request.getAdmissionId(), format);
                case STATISTICS:
                    return generateStatisticsReport(request.getStartDate(), request.getEndDate(), format);
                default:
                    throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION, "지원하지 않는 리포트 유형입니다.");
            }
        } catch (IOException e) {
            log.error("리포트 생성 중 오류 발생", e);
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION, "리포트 생성에 실패했습니다: " + e.getMessage());
        }
    }

    /**
     * 진료 요약 리포트 생성
     */
    private Resource generateMedicalSummaryReport(Long patientId, String format) throws IOException {
        Patient patient = patientStore.findById(patientId)
                .orElseThrow(() -> new CommonException(ErrorCode.PATIENT_NOT_FOUND));

        List<MedicalRecord> medicalRecords = emrStore.findMedicalRecordsByPatient(patientId, 
                org.springframework.data.domain.Pageable.unpaged()).getContent();

        if ("PDF".equals(format)) {
            return pdfReportGenerator.generateMedicalSummaryReport(patient, medicalRecords);
        } else if ("EXCEL".equals(format)) {
            return excelReportGenerator.generateMedicalSummaryReport(patient, medicalRecords);
        } else {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION, "지원하지 않는 포맷입니다: " + format);
        }
    }

    /**
     * 검사 결과 리포트 생성
     */
    private Resource generateLabResultsReport(Long patientId, LocalDate startDate, LocalDate endDate, String format) throws IOException {
        Patient patient = patientStore.findById(patientId)
                .orElseThrow(() -> new CommonException(ErrorCode.PATIENT_NOT_FOUND));

        // 검사 결과 조회 로직 (간단한 예시)
        List<LabResult> labResults = emrStore.findLabResultsByLabOrder(null); // TODO: 실제 구현 필요

        if ("PDF".equals(format)) {
            return pdfReportGenerator.generateLabResultsReport(patient, labResults, startDate, endDate);
        } else if ("EXCEL".equals(format)) {
            return excelReportGenerator.generateLabResultsReport(patient, labResults, startDate, endDate);
        } else {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION, "지원하지 않는 포맷입니다: " + format);
        }
    }

    /**
     * 입원 환자 리포트 생성
     */
    private Resource generateInpatientReport(Long admissionId, String format) throws IOException {
        Admission admission = bedStore.findAdmissionById(admissionId)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "입원 정보를 찾을 수 없습니다."));

        if ("PDF".equals(format)) {
            return pdfReportGenerator.generateInpatientReport(admission);
        } else if ("EXCEL".equals(format)) {
            return excelReportGenerator.generateInpatientReport(admission);
        } else {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION, "지원하지 않는 포맷입니다: " + format);
        }
    }

    /**
     * 통계 리포트 생성
     */
    private Resource generateStatisticsReport(LocalDate startDate, LocalDate endDate, String format) throws IOException {
        if ("PDF".equals(format)) {
            return pdfReportGenerator.generateStatisticsReport(startDate, endDate);
        } else if ("EXCEL".equals(format)) {
            return excelReportGenerator.generateStatisticsReport(startDate, endDate);
        } else {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION, "지원하지 않는 포맷입니다: " + format);
        }
    }
}

