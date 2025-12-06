package com.emrsystem.domain.emr.controller;

import com.emrsystem.domain.emr.entity.ChartTemplate;
import com.emrsystem.domain.emr.entity.DiagnosisCode;
import com.emrsystem.domain.emr.entity.LabOrder;
import com.emrsystem.domain.emr.entity.LabResult;
import com.emrsystem.domain.emr.entity.MedicalCertificate;
import com.emrsystem.domain.emr.entity.MedicalImage;
import com.emrsystem.domain.emr.entity.MedicalRecord;
import com.emrsystem.domain.emr.entity.Prescription;
import com.emrsystem.domain.emr.facade.EmrFacade;
import com.emrsystem.domain.emr.facade.LabTrendFacade;
import com.emrsystem.domain.emr.request.EmrRequests;
import com.emrsystem.domain.emr.response.EmrResponses;
import com.emrsystem.domain.emr.response.LabTrendResponses;
import com.emrsystem.global.common.controller.BaseController;
import com.emrsystem.global.common.dto.ApiResponse;
import com.emrsystem.global.common.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "EMR", description = "전자의무기록 관리 API")
@RestController
@RequestMapping("/api/emr")
@RequiredArgsConstructor
public class EmrController extends BaseController {

    private final EmrFacade emrFacade;
    private final LabTrendFacade labTrendFacade;

    // Medical Record endpoints
    // 전체 목록 엔드포인트는 스펙 축소로 제거: 환자/의사 기준만 제공

    @GetMapping("/medical-records/patient/{patientId}")
    @Operation(summary = "환자별 진료기록 조회", description = "특정 환자의 진료기록을 조회합니다.")
    public ResponseEntity<ApiResponse<PageResponse<EmrResponses.MedicalRecordSummary>>> getMedicalRecordsByPatient(
            @Parameter(description = "환자 ID") @PathVariable Long patientId,
            @Parameter(description = "페이지 정보") @PageableDefault(size = 20) Pageable pageable) {
        Page<MedicalRecord> medicalRecords = emrFacade.getMedicalRecordsByPatient(patientId, pageable);
        Page<EmrResponses.MedicalRecordSummary> summaries = medicalRecords.map(EmrResponses.MedicalRecordSummary::of);
        return ok(PageResponse.of(summaries));
    }

    @GetMapping("/medical-records/doctor/{doctorId}")
    @Operation(summary = "의사별 진료기록 조회", description = "특정 의사의 진료기록을 조회합니다.")
    public ResponseEntity<ApiResponse<PageResponse<EmrResponses.MedicalRecordSummary>>> getMedicalRecordsByDoctor(
            @Parameter(description = "의사 ID") @PathVariable Long doctorId,
            @Parameter(description = "페이지 정보") @PageableDefault(size = 20) Pageable pageable) {
        Page<MedicalRecord> medicalRecords = emrFacade.getMedicalRecordsByDoctor(doctorId, pageable);
        Page<EmrResponses.MedicalRecordSummary> summaries = medicalRecords.map(EmrResponses.MedicalRecordSummary::of);
        return ok(PageResponse.of(summaries));
    }

    @GetMapping("/medical-records/{id}")
    @Operation(summary = "진료기록 상세 조회", description = "특정 진료기록의 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<EmrResponses.MedicalRecordSummary>> getMedicalRecord(
            @Parameter(description = "진료기록 ID") @PathVariable Long id) {
        MedicalRecord medicalRecord = emrFacade.getMedicalRecord(id);
        return ok(EmrResponses.MedicalRecordSummary.of(medicalRecord));
    }

    @PostMapping("/medical-records")
    @Operation(summary = "진료기록 생성", description = "새로운 진료기록을 생성합니다.")
    public ResponseEntity<ApiResponse<EmrResponses.MedicalRecordSummary>> createMedicalRecord(
            @Valid @RequestBody EmrRequests.CreateMedicalRecordRequest request) {
        MedicalRecord medicalRecord = emrFacade.createMedicalRecord(request);
        return created(EmrResponses.MedicalRecordSummary.of(medicalRecord));
    }

    @PutMapping("/medical-records/{id}")
    @Operation(summary = "진료기록 수정", description = "기존 진료기록을 수정합니다.")
    public ResponseEntity<ApiResponse<EmrResponses.MedicalRecordSummary>> updateMedicalRecord(
            @Parameter(description = "진료기록 ID") @PathVariable Long id,
            @Valid @RequestBody EmrRequests.UpdateMedicalRecordRequest request) {
        MedicalRecord medicalRecord = emrFacade.updateMedicalRecord(id, request);
        return ok(EmrResponses.MedicalRecordSummary.of(medicalRecord));
    }

    // 상태 전환(완료) 제거

    // Chart Template endpoints
    // 템플릿 목록은 단순 findAll로 제공(필요 시 페이징 추가)
    @GetMapping("/chart-templates")
    @Operation(summary = "차트 템플릿 목록 조회", description = "차트 템플릿 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<EmrResponses.ChartTemplateSummary>>> getChartTemplates() {
        List<ChartTemplate> templates = emrFacade.getChartTemplates();
        List<EmrResponses.ChartTemplateSummary> summaries = templates.stream()
                .map(EmrResponses.ChartTemplateSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @PostMapping("/chart-templates")
    @Operation(summary = "차트 템플릿 생성", description = "새로운 차트 템플릿을 생성합니다.")
    public ResponseEntity<ApiResponse<EmrResponses.ChartTemplateSummary>> createChartTemplate(
            @Valid @RequestBody EmrRequests.CreateChartTemplateRequest request) {
        ChartTemplate template = emrFacade.createChartTemplate(request);
        return created(EmrResponses.ChartTemplateSummary.of(template));
    }

    @PutMapping("/chart-templates/{id}")
    @Operation(summary = "차트 템플릿 수정", description = "기존 차트 템플릿을 수정합니다.")
    public ResponseEntity<ApiResponse<EmrResponses.ChartTemplateSummary>> updateChartTemplate(
            @Parameter(description = "템플릿 ID") @PathVariable Long id,
            @Valid @RequestBody EmrRequests.UpdateChartTemplateRequest request) {
        ChartTemplate template = emrFacade.updateChartTemplate(id, request);
        return ok(EmrResponses.ChartTemplateSummary.of(template));
    }

    @DeleteMapping("/chart-templates/{id}")
    @Operation(summary = "차트 템플릿 삭제", description = "차트 템플릿을 삭제합니다.")
    public ResponseEntity<ApiResponse<String>> deleteChartTemplate(
            @Parameter(description = "템플릿 ID") @PathVariable Long id) {
        emrFacade.deleteChartTemplate(id);
        return okMessage("deleted");
    }

    // Prescription endpoints
    // 처방전 조회는 진료기록 기준으로 단순화 → /prescriptions/medical-record/{id}
    @GetMapping("/prescriptions/medical-record/{medicalRecordId}")
    @Operation(summary = "진료기록별 처방전 조회", description = "특정 진료기록의 처방전을 조회합니다.")
    public ResponseEntity<ApiResponse<PageResponse<EmrResponses.PrescriptionSummary>>> getPrescriptionsByMedicalRecord(
            @Parameter(description = "진료기록 ID") @PathVariable Long medicalRecordId,
            @Parameter(description = "페이지 정보") @PageableDefault(size = 20) Pageable pageable) {
        Page<Prescription> prescriptions = emrFacade.getPrescriptionsByMedicalRecord(medicalRecordId, pageable);
        Page<EmrResponses.PrescriptionSummary> summaries = prescriptions.map(EmrResponses.PrescriptionSummary::of);
        return ok(PageResponse.of(summaries));
    }

    @PostMapping("/prescriptions")
    @Operation(summary = "처방전 생성", description = "새로운 처방전을 생성합니다.")
    public ResponseEntity<ApiResponse<EmrResponses.PrescriptionSummary>> createPrescription(
            @Valid @RequestBody EmrRequests.CreatePrescriptionRequest request) {
        Prescription prescription = emrFacade.createPrescription(request);
        return created(EmrResponses.PrescriptionSummary.of(prescription));
    }

    // 처방 상태 전환 제거

    // Lab Order endpoints
    // 검사오더 조회는 진료기록/상태 기준으로 단순화
    @GetMapping("/lab-orders/medical-record/{medicalRecordId}")
    @Operation(summary = "진료기록별 검사 오더 조회", description = "특정 진료기록의 검사 오더를 조회합니다.")
    public ResponseEntity<ApiResponse<PageResponse<EmrResponses.LabOrderSummary>>> getLabOrdersByMedicalRecord(
            @Parameter(description = "진료기록 ID") @PathVariable Long medicalRecordId,
            @Parameter(description = "페이지 정보") @PageableDefault(size = 20) Pageable pageable) {
        Page<LabOrder> labOrders = emrFacade.getLabOrdersByMedicalRecord(medicalRecordId, pageable);
        Page<EmrResponses.LabOrderSummary> summaries = labOrders.map(EmrResponses.LabOrderSummary::of);
        return ok(PageResponse.of(summaries));
    }

    @PostMapping("/lab-orders")
    @Operation(summary = "검사 오더 생성", description = "새로운 검사 오더를 생성합니다.")
    public ResponseEntity<ApiResponse<EmrResponses.LabOrderSummary>> createLabOrder(
            @Valid @RequestBody EmrRequests.CreateLabOrderRequest request) {
        LabOrder labOrder = emrFacade.createLabOrder(request);
        return created(EmrResponses.LabOrderSummary.of(labOrder));
    }

    // Diagnosis Code endpoints
    @GetMapping("/diagnosis-codes")
    @Operation(summary = "진단 코드 목록 조회", description = "진단 코드 목록을 조회합니다. 코드 또는 이름으로 검색 가능합니다.")
    public ResponseEntity<ApiResponse<PageResponse<EmrResponses.DiagnosisCodeSummary>>> getDiagnosisCodes(
            @Parameter(description = "코드 검색") @RequestParam(required = false) String code,
            @Parameter(description = "이름 검색") @RequestParam(required = false) String name,
            @Parameter(description = "페이지 정보") @PageableDefault(size = 20) Pageable pageable) {
        Page<DiagnosisCode> diagnosisCodes = (code != null || name != null)
                ? emrFacade.searchDiagnosisCodes(code, name, pageable)
                : emrFacade.getDiagnosisCodes(pageable);
        Page<EmrResponses.DiagnosisCodeSummary> summaries = diagnosisCodes.map(EmrResponses.DiagnosisCodeSummary::of);
        return ok(PageResponse.of(summaries));
    }

    @GetMapping("/diagnosis-codes/category/{category}")
    @Operation(summary = "카테고리별 진단 코드 조회", description = "특정 카테고리의 진단 코드를 조회합니다.")
    public ResponseEntity<ApiResponse<List<EmrResponses.DiagnosisCodeSummary>>> getDiagnosisCodesByCategory(
            @Parameter(description = "카테고리") @PathVariable String category) {
        List<DiagnosisCode> diagnosisCodes = emrFacade.getDiagnosisCodesByCategory(category);
        List<EmrResponses.DiagnosisCodeSummary> summaries = diagnosisCodes.stream()
                .map(EmrResponses.DiagnosisCodeSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/diagnosis-codes/{id}")
    @Operation(summary = "진단 코드 상세 조회", description = "특정 진단 코드의 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<EmrResponses.DiagnosisCodeSummary>> getDiagnosisCode(
            @Parameter(description = "진단 코드 ID") @PathVariable Long id) {
        DiagnosisCode diagnosisCode = emrFacade.getDiagnosisCode(id);
        return ok(EmrResponses.DiagnosisCodeSummary.of(diagnosisCode));
    }

    @GetMapping("/diagnosis-codes/code/{code}")
    @Operation(summary = "코드로 진단 코드 조회", description = "코드로 진단 코드를 조회합니다.")
    public ResponseEntity<ApiResponse<EmrResponses.DiagnosisCodeSummary>> getDiagnosisCodeByCode(
            @Parameter(description = "진단 코드") @PathVariable String code) {
        DiagnosisCode diagnosisCode = emrFacade.getDiagnosisCodeByCode(code);
        return ok(EmrResponses.DiagnosisCodeSummary.of(diagnosisCode));
    }

    @PostMapping("/diagnosis-codes")
    @Operation(summary = "진단 코드 생성", description = "새로운 진단 코드를 생성합니다.")
    public ResponseEntity<ApiResponse<EmrResponses.DiagnosisCodeSummary>> createDiagnosisCode(
            @Valid @RequestBody EmrRequests.CreateDiagnosisCodeRequest request) {
        DiagnosisCode diagnosisCode = emrFacade.createDiagnosisCode(request);
        return created(EmrResponses.DiagnosisCodeSummary.of(diagnosisCode));
    }

    @PutMapping("/diagnosis-codes/{id}")
    @Operation(summary = "진단 코드 수정", description = "기존 진단 코드를 수정합니다.")
    public ResponseEntity<ApiResponse<EmrResponses.DiagnosisCodeSummary>> updateDiagnosisCode(
            @Parameter(description = "진단 코드 ID") @PathVariable Long id,
            @Valid @RequestBody EmrRequests.UpdateDiagnosisCodeRequest request) {
        DiagnosisCode diagnosisCode = emrFacade.updateDiagnosisCode(id, request);
        return ok(EmrResponses.DiagnosisCodeSummary.of(diagnosisCode));
    }

    @PostMapping("/diagnosis-codes/{id}/deactivate")
    @Operation(summary = "진단 코드 비활성화", description = "진단 코드를 비활성화합니다.")
    public ResponseEntity<ApiResponse<String>> deactivateDiagnosisCode(
            @Parameter(description = "진단 코드 ID") @PathVariable Long id) {
        emrFacade.deactivateDiagnosisCode(id);
        return okMessage("deactivated");
    }

    @PostMapping("/diagnosis-codes/{id}/activate")
    @Operation(summary = "진단 코드 활성화", description = "진단 코드를 활성화합니다.")
    public ResponseEntity<ApiResponse<String>> activateDiagnosisCode(
            @Parameter(description = "진단 코드 ID") @PathVariable Long id) {
        emrFacade.activateDiagnosisCode(id);
        return okMessage("activated");
    }

    // Lab Result endpoints
    @GetMapping("/lab-results/lab-order/{labOrderId}")
    @Operation(summary = "검사 오더별 결과 조회", description = "특정 검사 오더의 결과 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<EmrResponses.LabResultSummary>>> getLabResultsByLabOrder(
            @Parameter(description = "검사 오더 ID") @PathVariable Long labOrderId) {
        List<LabResult> labResults = emrFacade.getLabResultsByLabOrder(labOrderId);
        List<EmrResponses.LabResultSummary> summaries = labResults.stream()
                .map(EmrResponses.LabResultSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/lab-results/abnormal")
    @Operation(summary = "이상 검사 결과 조회", description = "이상치가 있는 검사 결과를 조회합니다.")
    public ResponseEntity<ApiResponse<List<EmrResponses.LabResultSummary>>> getAbnormalLabResults() {
        List<LabResult> labResults = emrFacade.getAbnormalLabResults();
        List<EmrResponses.LabResultSummary> summaries = labResults.stream()
                .map(EmrResponses.LabResultSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/lab-results/{id}")
    @Operation(summary = "검사 결과 상세 조회", description = "특정 검사 결과의 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<EmrResponses.LabResultSummary>> getLabResult(
            @Parameter(description = "검사 결과 ID") @PathVariable Long id) {
        LabResult labResult = emrFacade.getLabResult(id);
        return ok(EmrResponses.LabResultSummary.of(labResult));
    }

    @PostMapping("/lab-results")
    @Operation(summary = "검사 결과 생성", description = "새로운 검사 결과를 생성합니다.")
    public ResponseEntity<ApiResponse<EmrResponses.LabResultSummary>> createLabResult(
            @Valid @RequestBody EmrRequests.CreateLabResultRequest request) {
        LabResult labResult = emrFacade.createLabResult(request);
        return created(EmrResponses.LabResultSummary.of(labResult));
    }

    @PutMapping("/lab-results/{id}")
    @Operation(summary = "검사 결과 수정", description = "기존 검사 결과를 수정합니다.")
    public ResponseEntity<ApiResponse<EmrResponses.LabResultSummary>> updateLabResult(
            @Parameter(description = "검사 결과 ID") @PathVariable Long id,
            @Valid @RequestBody EmrRequests.UpdateLabResultRequest request) {
        LabResult labResult = emrFacade.updateLabResult(id, request);
        return ok(EmrResponses.LabResultSummary.of(labResult));
    }

    @PostMapping("/lab-results/{id}/correct")
    @Operation(summary = "검사 결과 정정", description = "검사 결과를 정정 처리합니다.")
    public ResponseEntity<ApiResponse<String>> correctLabResult(
            @Parameter(description = "검사 결과 ID") @PathVariable Long id) {
        emrFacade.correctLabResult(id);
        return okMessage("corrected");
    }

    @PostMapping("/lab-results/{id}/cancel")
    @Operation(summary = "검사 결과 취소", description = "검사 결과를 취소 처리합니다.")
    public ResponseEntity<ApiResponse<String>> cancelLabResult(
            @Parameter(description = "검사 결과 ID") @PathVariable Long id) {
        emrFacade.cancelLabResult(id);
        return okMessage("cancelled");
    }

    // Lab Trend Analysis endpoints
    @GetMapping("/lab-results/trend/{patientId}")
    @Operation(summary = "검사 결과 추이 분석", description = "환자의 특정 검사 항목에 대한 추이를 분석합니다.")
    public ResponseEntity<ApiResponse<LabTrendResponses.LabTrendAnalysis>> getLabTrendAnalysis(
            @Parameter(description = "환자 ID") @PathVariable Long patientId,
            @Parameter(description = "검사 항목명") @RequestParam String testItemName,
            @Parameter(description = "시작일") @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "종료일") @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) LocalDate endDate) {
        LabTrendResponses.LabTrendAnalysis analysis = labTrendFacade.getTrendAnalysis(
                patientId, testItemName, startDate.atStartOfDay(), endDate.atTime(23, 59, 59));
        return ok(analysis);
    }

    @GetMapping("/lab-results/test-items/{patientId}")
    @Operation(summary = "검사 항목 목록 조회", description = "환자의 검사 항목 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<String>>> getTestItemNames(
            @Parameter(description = "환자 ID") @PathVariable Long patientId) {
        List<String> testItemNames = labTrendFacade.getTestItemNames(patientId);
        return ok(testItemNames);
    }

    @GetMapping("/lab-results/comparison/{patientId}")
    @Operation(summary = "검사 결과 비교", description = "환자의 최신 검사 결과와 이전 결과를 비교합니다.")
    public ResponseEntity<ApiResponse<LabTrendResponses.LabResultComparison>> compareLabResults(
            @Parameter(description = "환자 ID") @PathVariable Long patientId,
            @Parameter(description = "검사 항목명") @RequestParam String testItemName,
            @Parameter(description = "비교할 결과 수 (기본값: 5)") @RequestParam(defaultValue = "5") int compareCount) {
        LabTrendResponses.LabResultComparison comparison = labTrendFacade.compareResults(
                patientId, testItemName, compareCount);
        return ok(comparison);
    }

    // Medical Image endpoints
    @GetMapping("/medical-images/patient/{patientId}")
    @Operation(summary = "환자별 의료 영상 조회", description = "특정 환자의 의료 영상 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<PageResponse<EmrResponses.MedicalImageSummary>>> getMedicalImagesByPatient(
            @Parameter(description = "환자 ID") @PathVariable Long patientId,
            @Parameter(description = "페이지 정보") @PageableDefault(size = 20) Pageable pageable) {
        Page<MedicalImage> medicalImages = emrFacade.getMedicalImagesByPatient(patientId, pageable);
        Page<EmrResponses.MedicalImageSummary> summaries = medicalImages.map(EmrResponses.MedicalImageSummary::of);
        return ok(PageResponse.of(summaries));
    }

    @GetMapping("/medical-images/medical-record/{medicalRecordId}")
    @Operation(summary = "진료기록별 의료 영상 조회", description = "특정 진료기록의 의료 영상 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<EmrResponses.MedicalImageSummary>>> getMedicalImagesByMedicalRecord(
            @Parameter(description = "진료기록 ID") @PathVariable Long medicalRecordId) {
        List<MedicalImage> medicalImages = emrFacade.getMedicalImagesByMedicalRecord(medicalRecordId);
        List<EmrResponses.MedicalImageSummary> summaries = medicalImages.stream()
                .map(EmrResponses.MedicalImageSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/medical-images/lab-order/{labOrderId}")
    @Operation(summary = "검사 오더별 의료 영상 조회", description = "특정 검사 오더의 의료 영상 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<EmrResponses.MedicalImageSummary>>> getMedicalImagesByLabOrder(
            @Parameter(description = "검사 오더 ID") @PathVariable Long labOrderId) {
        List<MedicalImage> medicalImages = emrFacade.getMedicalImagesByLabOrder(labOrderId);
        List<EmrResponses.MedicalImageSummary> summaries = medicalImages.stream()
                .map(EmrResponses.MedicalImageSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/medical-images/{id}")
    @Operation(summary = "의료 영상 상세 조회", description = "특정 의료 영상의 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<EmrResponses.MedicalImageSummary>> getMedicalImage(
            @Parameter(description = "영상 ID") @PathVariable Long id) {
        MedicalImage medicalImage = emrFacade.getMedicalImage(id);
        return ok(EmrResponses.MedicalImageSummary.of(medicalImage));
    }

    @PostMapping("/medical-images")
    @Operation(summary = "의료 영상 등록", description = "새로운 의료 영상을 등록합니다.")
    public ResponseEntity<ApiResponse<EmrResponses.MedicalImageSummary>> createMedicalImage(
            @Valid @RequestBody EmrRequests.CreateMedicalImageRequest request) {
        MedicalImage medicalImage = emrFacade.createMedicalImage(request);
        return created(EmrResponses.MedicalImageSummary.of(medicalImage));
    }

    @PostMapping("/medical-images/{id}/interpretation")
    @Operation(summary = "의료 영상 판독 추가", description = "의료 영상에 판독 결과를 추가합니다.")
    public ResponseEntity<ApiResponse<EmrResponses.MedicalImageSummary>> addInterpretation(
            @Parameter(description = "영상 ID") @PathVariable Long id,
            @Valid @RequestBody EmrRequests.UpdateMedicalImageInterpretationRequest request) {
        MedicalImage medicalImage = emrFacade.addInterpretation(id, request);
        return ok(EmrResponses.MedicalImageSummary.of(medicalImage));
    }

    @PostMapping("/medical-images/{id}/finalize")
    @Operation(summary = "의료 영상 확정", description = "의료 영상을 확정 처리합니다.")
    public ResponseEntity<ApiResponse<EmrResponses.MedicalImageSummary>> finalizeMedicalImage(
            @Parameter(description = "영상 ID") @PathVariable Long id) {
        MedicalImage medicalImage = emrFacade.finalizeMedicalImage(id);
        return ok(EmrResponses.MedicalImageSummary.of(medicalImage));
    }

    @PostMapping("/medical-images/{id}/cancel")
    @Operation(summary = "의료 영상 취소", description = "의료 영상을 취소 처리합니다.")
    public ResponseEntity<ApiResponse<String>> cancelMedicalImage(
            @Parameter(description = "영상 ID") @PathVariable Long id) {
        emrFacade.cancelMedicalImage(id);
        return okMessage("cancelled");
    }

    // Medical Certificate endpoints
    @GetMapping("/medical-certificates/patient/{patientId}")
    @Operation(summary = "환자별 진단서/소견서 조회", description = "특정 환자의 진단서/소견서 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<PageResponse<EmrResponses.MedicalCertificateSummary>>> getMedicalCertificatesByPatient(
            @Parameter(description = "환자 ID") @PathVariable Long patientId,
            @Parameter(description = "페이지 정보") @PageableDefault(size = 20) Pageable pageable) {
        Page<MedicalCertificate> certificates = emrFacade.getMedicalCertificatesByPatient(patientId, pageable);
        Page<EmrResponses.MedicalCertificateSummary> summaries = certificates.map(EmrResponses.MedicalCertificateSummary::of);
        return ok(PageResponse.of(summaries));
    }

    @GetMapping("/medical-certificates/{id}")
    @Operation(summary = "진단서/소견서 상세 조회", description = "특정 진단서/소견서의 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<EmrResponses.MedicalCertificateSummary>> getMedicalCertificate(
            @Parameter(description = "증명서 ID") @PathVariable Long id) {
        MedicalCertificate certificate = emrFacade.getMedicalCertificate(id);
        return ok(EmrResponses.MedicalCertificateSummary.of(certificate));
    }

    @GetMapping("/medical-certificates/number/{certificateNumber}")
    @Operation(summary = "증명서 번호로 조회", description = "증명서 번호로 진단서/소견서를 조회합니다.")
    public ResponseEntity<ApiResponse<EmrResponses.MedicalCertificateSummary>> getMedicalCertificateByNumber(
            @Parameter(description = "증명서 번호") @PathVariable String certificateNumber) {
        MedicalCertificate certificate = emrFacade.getMedicalCertificateByNumber(certificateNumber);
        return ok(EmrResponses.MedicalCertificateSummary.of(certificate));
    }

    @PostMapping("/medical-certificates")
    @Operation(summary = "진단서/소견서 발급", description = "새로운 진단서/소견서를 발급합니다.")
    public ResponseEntity<ApiResponse<EmrResponses.MedicalCertificateSummary>> createMedicalCertificate(
            @Valid @RequestBody EmrRequests.CreateMedicalCertificateRequest request) {
        MedicalCertificate certificate = emrFacade.createMedicalCertificate(request);
        return created(EmrResponses.MedicalCertificateSummary.of(certificate));
    }

    @PostMapping("/medical-certificates/{id}/cancel")
    @Operation(summary = "진단서/소견서 취소", description = "진단서/소견서를 취소 처리합니다.")
    public ResponseEntity<ApiResponse<EmrResponses.MedicalCertificateSummary>> cancelMedicalCertificate(
            @Parameter(description = "증명서 ID") @PathVariable Long id) {
        MedicalCertificate certificate = emrFacade.cancelMedicalCertificate(id);
        return ok(EmrResponses.MedicalCertificateSummary.of(certificate));
    }
}