package com.emrsystem.domain.appointment.controller;

import com.emrsystem.domain.appointment.facade.AppointmentFacade;
import com.emrsystem.domain.appointment.request.AppointmentRequests.CreateAppointmentRequest;
import com.emrsystem.domain.appointment.request.AppointmentRequests.UpdateAppointmentRequest;
import com.emrsystem.domain.appointment.request.AppointmentRequests.UpdateAppointmentStatusRequest;
import com.emrsystem.domain.appointment.response.AppointmentResponses.AppointmentSummary;
import com.emrsystem.global.common.controller.BaseController;
import com.emrsystem.global.common.dto.ApiResponse;
import com.emrsystem.global.common.dto.PageResponse;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
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

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController extends BaseController {

    private final AppointmentFacade appointmentFacade;

    @GetMapping
    @Operation(summary = "예약 목록 조회", description = "페이지네이션 및 의사/환자/기간 필터를 지원합니다.")
    public ResponseEntity<ApiResponse<PageResponse<AppointmentSummary>>> page(@PageableDefault(size = 20) @Parameter(description = "페이지/정렬 파라미터") Pageable pageable,
                                                                              @RequestParam(required = false) @Parameter(description = "의사 ID", example = "5") Long doctorId,
                                                                              @RequestParam(required = false) @Parameter(description = "환자 ID", example = "10") Long patientId,
                                                                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Parameter(description = "시작일시", example = "2025-01-01T09:00:00") LocalDateTime start,
                                                                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Parameter(description = "종료일시", example = "2025-01-08T18:00:00") LocalDateTime end
    ) {
        PageResponse<AppointmentSummary> response = (doctorId == null && patientId == null && start == null && end == null)
                ? appointmentFacade.page(pageable)
                : appointmentFacade.search(doctorId, patientId, start, end, pageable);
        return ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AppointmentSummary>> get(@PathVariable Long id) {
        return ok(appointmentFacade.get(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AppointmentSummary>> create(@Valid @RequestBody CreateAppointmentRequest request) {
        return created(appointmentFacade.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AppointmentSummary>> update(@PathVariable Long id,
                                                                  @Valid @RequestBody UpdateAppointmentRequest request) {
        return ok(appointmentFacade.update(id, request));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<AppointmentSummary>> updateStatus(@PathVariable Long id,
                                                                        @Valid @RequestBody UpdateAppointmentStatusRequest request) {
        return ok(appointmentFacade.updateStatus(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        appointmentFacade.delete(id);
        return okMessage("deleted");
    }
}


