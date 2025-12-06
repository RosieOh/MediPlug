package com.emrsystem.domain.patient.controller;

import com.emrsystem.domain.patient.facade.PatientFacade;
import com.emrsystem.domain.patient.request.PatientRequests.CreatePatientRequest;
import com.emrsystem.domain.patient.request.PatientRequests.UpdatePatientRequest;
import com.emrsystem.domain.patient.request.PatientRequests.CreateAllergyRequest;
import com.emrsystem.domain.patient.request.PatientRequests.UpdateAllergyRequest;
import com.emrsystem.domain.patient.request.PatientRequests.CreateBloodTypeRequest;
import com.emrsystem.domain.patient.response.PatientResponses.PatientSummary;
import com.emrsystem.domain.patient.response.PatientResponses.AllergySummary;
import com.emrsystem.domain.patient.response.PatientResponses.BloodTypeSummary;
import com.emrsystem.domain.patient.service.BloodTypeService;
import com.emrsystem.domain.patient.service.PatientAllergyService;
import com.emrsystem.global.common.controller.BaseController;
import com.emrsystem.global.common.dto.ApiResponse;
import com.emrsystem.global.common.dto.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import com.emrsystem.core.annotation.LogSensitive;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController extends BaseController {

    private final PatientFacade patientFacade;
    private final PatientAllergyService allergyService;
    private final BloodTypeService bloodTypeService;

    @GetMapping
    @Operation(summary = "환자 목록 조회", description = "페이지네이션 및 이름/식별자 필터를 지원합니다.")
    @LogSensitive(resource = "PATIENT", action = "READ")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_DOCTOR','ROLE_NURSE')")
    public ResponseEntity<ApiResponse<PageResponse<PatientSummary>>> page(@PageableDefault(size = 20) @Parameter(description = "페이지/정렬 파라미터") Pageable pageable,
                                                                          @RequestParam(required = false) @Parameter(description = "환자명 부분검색", example = "Park") String name,
                                                                          @RequestParam(required = false) @Parameter(description = "환자 식별자 부분검색", example = "MRN-") String identifier
    ) {
        PageResponse<PatientSummary> response = (name == null && identifier == null)
                ? patientFacade.page(pageable)
                : patientFacade.search(name, identifier, pageable);
        return ok(response);
    }

    @GetMapping("/{id}")
    @LogSensitive(resource = "PATIENT", action = "READ")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_DOCTOR','ROLE_NURSE')")
    public ResponseEntity<ApiResponse<PatientSummary>> get(@PathVariable Long id) {
        return ok(patientFacade.get(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PatientSummary>> create(@Valid @RequestBody CreatePatientRequest request) {
        return created(patientFacade.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientSummary>> update(@PathVariable Long id,
                                                              @Valid @RequestBody UpdatePatientRequest request) {
        return ok(patientFacade.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        patientFacade.delete(id);
        return okMessage("deleted");
    }

    // 알레르기 관련 엔드포인트
    @GetMapping("/{patientId}/allergies")
    @Operation(summary = "환자 알레르기 목록 조회", description = "특정 환자의 활성 알레르기 정보를 조회합니다.")
    @LogSensitive(resource = "PATIENT_ALLERGY", action = "READ")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_DOCTOR','ROLE_NURSE')")
    public ResponseEntity<ApiResponse<List<AllergySummary>>> getAllergies(@PathVariable Long patientId) {
        List<AllergySummary> allergies = allergyService.getByPatientId(patientId).stream()
                .map(AllergySummary::from)
                .collect(Collectors.toList());
        return ok(allergies);
    }

    @GetMapping("/{patientId}/allergies/{allergyId}")
    @Operation(summary = "알레르기 상세 조회")
    @LogSensitive(resource = "PATIENT_ALLERGY", action = "READ")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_DOCTOR','ROLE_NURSE')")
    public ResponseEntity<ApiResponse<AllergySummary>> getAllergy(@PathVariable Long patientId,
                                                                    @PathVariable Long allergyId) {
        return ok(AllergySummary.from(allergyService.get(allergyId)));
    }

    @PostMapping("/{patientId}/allergies")
    @Operation(summary = "알레르기 등록", description = "환자의 알레르기 정보를 등록합니다.")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_DOCTOR')")
    public ResponseEntity<ApiResponse<AllergySummary>> createAllergy(@PathVariable Long patientId,
                                                                    @Valid @RequestBody CreateAllergyRequest request) {
        return created(AllergySummary.from(allergyService.create(patientId, request)));
    }

    @PutMapping("/{patientId}/allergies/{allergyId}")
    @Operation(summary = "알레르기 수정")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_DOCTOR')")
    public ResponseEntity<ApiResponse<AllergySummary>> updateAllergy(@PathVariable Long patientId,
                                                                   @PathVariable Long allergyId,
                                                                   @Valid @RequestBody UpdateAllergyRequest request) {
        return ok(AllergySummary.from(allergyService.update(allergyId, request)));
    }

    @PostMapping("/{patientId}/allergies/{allergyId}/deactivate")
    @Operation(summary = "알레르기 비활성화")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_DOCTOR')")
    public ResponseEntity<ApiResponse<String>> deactivateAllergy(@PathVariable Long patientId,
                                                                 @PathVariable Long allergyId) {
        allergyService.deactivate(allergyId);
        return okMessage("deactivated");
    }

    @PostMapping("/{patientId}/allergies/{allergyId}/activate")
    @Operation(summary = "알레르기 활성화")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_DOCTOR')")
    public ResponseEntity<ApiResponse<String>> activateAllergy(@PathVariable Long patientId,
                                                               @PathVariable Long allergyId) {
        allergyService.activate(allergyId);
        return okMessage("activated");
    }

    @DeleteMapping("/{patientId}/allergies/{allergyId}")
    @Operation(summary = "알레르기 삭제")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteAllergy(@PathVariable Long patientId,
                                                              @PathVariable Long allergyId) {
        allergyService.delete(allergyId);
        return okMessage("deleted");
    }

    // Blood Type endpoints
    @GetMapping("/{patientId}/blood-type")
    @Operation(summary = "환자 혈액형 조회", description = "환자의 혈액형 정보를 조회합니다.")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_DOCTOR','ROLE_NURSE')")
    public ResponseEntity<ApiResponse<BloodTypeSummary>> getBloodType(@PathVariable Long patientId) {
        return ok(BloodTypeSummary.from(bloodTypeService.getByPatient(patientId)));
    }

    @PostMapping("/{patientId}/blood-type")
    @Operation(summary = "혈액형 등록/수정", description = "환자의 혈액형 정보를 등록하거나 수정합니다.")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_DOCTOR')")
    public ResponseEntity<ApiResponse<BloodTypeSummary>> createOrUpdateBloodType(
            @PathVariable Long patientId,
            @Valid @RequestBody CreateBloodTypeRequest request) {
        return ok(BloodTypeSummary.from(bloodTypeService.createOrUpdate(patientId, request)));
    }
}


