package com.emrsystem.domain.patientportal.controller;

import com.emrsystem.domain.appointment.entity.Appointment;
import com.emrsystem.domain.emr.entity.MedicalRecord;
import com.emrsystem.domain.emr.entity.Prescription;
import com.emrsystem.domain.patientportal.facade.PatientPortalFacade;
import com.emrsystem.domain.patientportal.response.PatientPortalResponses;
import com.emrsystem.global.common.controller.BaseController;
import com.emrsystem.global.common.dto.ApiResponse;
import com.emrsystem.global.common.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Patient Portal", description = "환자 포털 API")
@RestController
@RequestMapping("/api/patient-portal")
@RequiredArgsConstructor
public class PatientPortalController extends BaseController {

    private final PatientPortalFacade patientPortalFacade;

    @GetMapping("/patients/{patientId}/summary")
    @Operation(summary = "환자 요약 정보 조회", description = "환자의 요약 정보를 조회합니다.")
    public ResponseEntity<ApiResponse<PatientPortalResponses.PatientSummary>> getPatientSummary(
            @Parameter(description = "환자 ID") @PathVariable Long patientId) {
        PatientPortalResponses.PatientSummary summary = patientPortalFacade.getPatientSummary(patientId);
        return ok(summary);
    }

    @GetMapping("/patients/{patientId}/medical-records")
    @Operation(summary = "진료 기록 조회", description = "환자의 진료 기록을 조회합니다.")
    public ResponseEntity<ApiResponse<PageResponse<MedicalRecord>>> getMedicalRecords(
            @Parameter(description = "환자 ID") @PathVariable Long patientId,
            @Parameter(description = "페이지 정보") @PageableDefault(size = 20) Pageable pageable) {
        Page<MedicalRecord> records = patientPortalFacade.getMedicalRecords(patientId, pageable);
        return ok(PageResponse.of(records));
    }

    @GetMapping("/patients/{patientId}/appointments")
    @Operation(summary = "예약 조회", description = "환자의 예약 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<Appointment>>> getAppointments(
            @Parameter(description = "환자 ID") @PathVariable Long patientId) {
        List<Appointment> appointments = patientPortalFacade.getAppointments(patientId);
        return ok(appointments);
    }

    @GetMapping("/patients/{patientId}/prescriptions")
    @Operation(summary = "처방전 조회", description = "환자의 처방전 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<List<Prescription>>> getPrescriptions(
            @Parameter(description = "환자 ID") @PathVariable Long patientId) {
        List<Prescription> prescriptions = patientPortalFacade.getPrescriptions(patientId);
        return ok(prescriptions);
    }
}

