package com.emrsystem.domain.performance.controller;

import com.emrsystem.domain.performance.facade.PerformanceFacade;
import com.emrsystem.domain.performance.response.PerformanceResponses;
import com.emrsystem.domain.user.enums.RoleType;
import com.emrsystem.global.common.controller.BaseController;
import com.emrsystem.global.common.dto.ApiResponse;
import com.emrsystem.global.security.authorization.RequireRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Performance Management", description = "의료진 성과 관리 API")
@RestController
@RequestMapping("/api/performance")
@RequiredArgsConstructor
public class PerformanceController extends BaseController {

    private final PerformanceFacade performanceFacade;

    @GetMapping("/doctors/{doctorId}")
    @Operation(summary = "의사별 성과 조회", description = "특정 의사의 성과를 조회합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<PerformanceResponses.DoctorPerformance>> getDoctorPerformance(
            @Parameter(description = "의사 ID") @PathVariable Long doctorId,
            @Parameter(description = "시작 일시") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "종료 일시") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        PerformanceResponses.DoctorPerformance performance = 
                performanceFacade.getDoctorPerformance(doctorId, startDate, endDate);
        return ok(performance);
    }

    @GetMapping("/doctors")
    @Operation(summary = "전체 의사 성과 조회", description = "모든 의사의 성과를 조회합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<List<PerformanceResponses.DoctorPerformance>>> getAllDoctorsPerformance(
            @Parameter(description = "시작 일시") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "종료 일시") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<PerformanceResponses.DoctorPerformance> performances = 
                performanceFacade.getAllDoctorsPerformance(startDate, endDate);
        return ok(performances);
    }
}

