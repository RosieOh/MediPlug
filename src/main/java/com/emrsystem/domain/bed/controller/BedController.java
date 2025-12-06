package com.emrsystem.domain.bed.controller;

import com.emrsystem.domain.bed.entity.Admission;
import com.emrsystem.domain.bed.entity.Bed;
import com.emrsystem.domain.bed.facade.BedFacade;
import com.emrsystem.domain.bed.request.BedRequests;
import com.emrsystem.domain.bed.response.BedResponses;
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

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Bed Management", description = "병상/입퇴원 관리 API")
@RestController
@RequestMapping("/api/beds")
@RequiredArgsConstructor
public class BedController extends BaseController {

    private final BedFacade bedFacade;

    // Bed endpoints
    @GetMapping("/room/{roomId}")
    @Operation(summary = "병실별 병상 조회", description = "특정 병실의 병상을 조회합니다.")
    public ResponseEntity<ApiResponse<List<BedResponses.BedSummary>>> getBedsByRoom(
            @Parameter(description = "병실 ID") @PathVariable Long roomId) {
        List<Bed> beds = bedFacade.getBedsByRoom(roomId);
        List<BedResponses.BedSummary> summaries = beds.stream()
                .map(BedResponses.BedSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "상태별 병상 조회", description = "특정 상태의 병상을 조회합니다.")
    public ResponseEntity<ApiResponse<List<BedResponses.BedSummary>>> getBedsByStatus(
            @Parameter(description = "상태") @PathVariable String status) {
        List<Bed> beds = bedFacade.getBedsByStatus(status);
        List<BedResponses.BedSummary> summaries = beds.stream()
                .map(BedResponses.BedSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/type/{bedType}")
    @Operation(summary = "유형별 병상 조회", description = "특정 유형의 병상을 조회합니다.")
    public ResponseEntity<ApiResponse<List<BedResponses.BedSummary>>> getBedsByBedType(
            @Parameter(description = "병상 유형") @PathVariable String bedType) {
        List<Bed> beds = bedFacade.getBedsByBedType(bedType);
        List<BedResponses.BedSummary> summaries = beds.stream()
                .map(BedResponses.BedSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/available")
    @Operation(summary = "사용 가능한 병상 조회", description = "사용 가능한 병상을 조회합니다.")
    public ResponseEntity<ApiResponse<List<BedResponses.BedSummary>>> getAvailableBeds() {
        List<Bed> beds = bedFacade.getAvailableBeds();
        List<BedResponses.BedSummary> summaries = beds.stream()
                .map(BedResponses.BedSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/available/type/{bedType}")
    @Operation(summary = "유형별 사용 가능한 병상 조회", description = "특정 유형의 사용 가능한 병상을 조회합니다.")
    public ResponseEntity<ApiResponse<List<BedResponses.BedSummary>>> getAvailableBedsByBedType(
            @Parameter(description = "병상 유형") @PathVariable String bedType) {
        List<Bed> beds = bedFacade.getAvailableBedsByBedType(bedType);
        List<BedResponses.BedSummary> summaries = beds.stream()
                .map(BedResponses.BedSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @GetMapping("/{id}")
    @Operation(summary = "병상 상세 조회", description = "특정 병상의 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<BedResponses.BedSummary>> getBed(
            @Parameter(description = "병상 ID") @PathVariable Long id) {
        Bed bed = bedFacade.getBed(id);
        return ok(BedResponses.BedSummary.of(bed));
    }

    @PostMapping
    @Operation(summary = "병상 생성", description = "새로운 병상을 생성합니다.")
    public ResponseEntity<ApiResponse<BedResponses.BedSummary>> createBed(
            @Valid @RequestBody BedRequests.CreateBedRequest request) {
        Bed bed = bedFacade.createBed(request);
        return created(BedResponses.BedSummary.of(bed));
    }

    @PutMapping("/{id}")
    @Operation(summary = "병상 수정", description = "기존 병상을 수정합니다.")
    public ResponseEntity<ApiResponse<BedResponses.BedSummary>> updateBed(
            @Parameter(description = "병상 ID") @PathVariable Long id,
            @Valid @RequestBody BedRequests.UpdateBedRequest request) {
        Bed bed = bedFacade.updateBed(id, request);
        return ok(BedResponses.BedSummary.of(bed));
    }

    @PostMapping("/{id}/maintenance")
    @Operation(summary = "병상 점검 설정", description = "병상을 점검 상태로 설정합니다.")
    public ResponseEntity<ApiResponse<BedResponses.BedSummary>> setBedMaintenance(
            @Parameter(description = "병상 ID") @PathVariable Long id) {
        Bed bed = bedFacade.setBedMaintenance(id);
        return ok(BedResponses.BedSummary.of(bed));
    }

    @PostMapping("/{id}/available")
    @Operation(summary = "병상 사용 가능 설정", description = "병상을 사용 가능 상태로 설정합니다.")
    public ResponseEntity<ApiResponse<BedResponses.BedSummary>> setBedAvailable(
            @Parameter(description = "병상 ID") @PathVariable Long id) {
        Bed bed = bedFacade.setBedAvailable(id);
        return ok(BedResponses.BedSummary.of(bed));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "병상 삭제", description = "병상을 삭제합니다.")
    public ResponseEntity<ApiResponse<String>> deleteBed(
            @Parameter(description = "병상 ID") @PathVariable Long id) {
        bedFacade.deleteBed(id);
        return okMessage("deleted");
    }

    // Admission endpoints
    @GetMapping("/admissions/patient/{patientId}")
    @Operation(summary = "환자별 입원 이력 조회", description = "특정 환자의 입원 이력을 조회합니다.")
    public ResponseEntity<ApiResponse<PageResponse<BedResponses.AdmissionSummary>>> getAdmissionsByPatient(
            @Parameter(description = "환자 ID") @PathVariable Long patientId,
            @Parameter(description = "페이지 정보") @PageableDefault(size = 20) Pageable pageable) {
        Page<Admission> admissions = bedFacade.getAdmissionsByPatient(patientId, pageable);
        Page<BedResponses.AdmissionSummary> summaries = admissions.map(BedResponses.AdmissionSummary::of);
        return ok(PageResponse.of(summaries));
    }

    @GetMapping("/admissions/bed/{bedId}")
    @Operation(summary = "병상별 입원 이력 조회", description = "특정 병상의 입원 이력을 조회합니다.")
    public ResponseEntity<ApiResponse<PageResponse<BedResponses.AdmissionSummary>>> getAdmissionsByBed(
            @Parameter(description = "병상 ID") @PathVariable Long bedId,
            @Parameter(description = "페이지 정보") @PageableDefault(size = 20) Pageable pageable) {
        Page<Admission> admissions = bedFacade.getAdmissionsByBed(bedId, pageable);
        Page<BedResponses.AdmissionSummary> summaries = admissions.map(BedResponses.AdmissionSummary::of);
        return ok(PageResponse.of(summaries));
    }

    @GetMapping("/admissions/status/{status}")
    @Operation(summary = "상태별 입원 조회", description = "특정 상태의 입원을 조회합니다.")
    public ResponseEntity<ApiResponse<PageResponse<BedResponses.AdmissionSummary>>> getAdmissionsByStatus(
            @Parameter(description = "상태") @PathVariable String status,
            @Parameter(description = "페이지 정보") @PageableDefault(size = 20) Pageable pageable) {
        Page<Admission> admissions = bedFacade.getAdmissionsByStatus(status, pageable);
        Page<BedResponses.AdmissionSummary> summaries = admissions.map(BedResponses.AdmissionSummary::of);
        return ok(PageResponse.of(summaries));
    }

    @GetMapping("/admissions/{id}")
    @Operation(summary = "입원 상세 조회", description = "특정 입원의 상세 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<BedResponses.AdmissionSummary>> getAdmission(
            @Parameter(description = "입원 ID") @PathVariable Long id) {
        Admission admission = bedFacade.getAdmission(id);
        return ok(BedResponses.AdmissionSummary.of(admission));
    }

    @PostMapping("/admissions")
    @Operation(summary = "입원 등록", description = "새로운 입원을 등록합니다.")
    public ResponseEntity<ApiResponse<BedResponses.AdmissionSummary>> createAdmission(
            @Valid @RequestBody BedRequests.CreateAdmissionRequest request) {
        Admission admission = bedFacade.createAdmission(request);
        return created(BedResponses.AdmissionSummary.of(admission));
    }

    @PutMapping("/admissions/{id}")
    @Operation(summary = "입원 정보 수정", description = "기존 입원 정보를 수정합니다.")
    public ResponseEntity<ApiResponse<BedResponses.AdmissionSummary>> updateAdmission(
            @Parameter(description = "입원 ID") @PathVariable Long id,
            @Valid @RequestBody BedRequests.UpdateAdmissionRequest request) {
        Admission admission = bedFacade.updateAdmission(id, request);
        return ok(BedResponses.AdmissionSummary.of(admission));
    }

    @PostMapping("/admissions/{id}/discharge")
    @Operation(summary = "퇴원 처리", description = "입원을 퇴원으로 처리합니다.")
    public ResponseEntity<ApiResponse<BedResponses.AdmissionSummary>> dischargePatient(
            @Parameter(description = "입원 ID") @PathVariable Long id,
            @Valid @RequestBody BedRequests.DischargeRequest request) {
        Admission admission = bedFacade.dischargePatient(id, request);
        return ok(BedResponses.AdmissionSummary.of(admission));
    }

    @PostMapping("/admissions/{id}/transfer")
    @Operation(summary = "전실 처리", description = "입원을 다른 병상으로 전실 처리합니다.")
    public ResponseEntity<ApiResponse<BedResponses.AdmissionSummary>> transferPatient(
            @Parameter(description = "입원 ID") @PathVariable Long id,
            @Valid @RequestBody BedRequests.TransferRequest request) {
        Admission admission = bedFacade.transferPatient(id, request);
        return ok(BedResponses.AdmissionSummary.of(admission));
    }

    // Bed availability endpoints
    @PostMapping("/availability")
    @Operation(summary = "병상 가용성 조회", description = "병상의 가용성을 조회합니다.")
    public ResponseEntity<ApiResponse<List<BedResponses.BedAvailabilitySummary>>> getBedAvailability(
            @Valid @RequestBody BedRequests.BedAvailabilityRequest request) {
        List<BedResponses.BedAvailabilitySummary> summaries = bedFacade.getBedAvailability(request);
        return ok(summaries);
    }

    @GetMapping("/occupancy-summary")
    @Operation(summary = "병상 점유 현황 요약", description = "병상 점유 현황을 요약하여 조회합니다.")
    public ResponseEntity<ApiResponse<List<BedResponses.BedOccupancySummary>>> getBedOccupancySummary() {
        List<BedResponses.BedOccupancySummary> summaries = bedFacade.getBedOccupancySummary();
        return ok(summaries);
    }
}
