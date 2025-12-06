package com.emrsystem.domain.statistics.controller;

import com.emrsystem.domain.statistics.facade.StatisticsFacade;
import com.emrsystem.domain.statistics.response.StatisticsResponses;
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

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Statistics & Dashboard", description = "통계 및 대시보드 API")
@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController extends BaseController {

    private final StatisticsFacade statisticsFacade;

    @GetMapping("/dashboard")
    @Operation(summary = "대시보드 통계 조회", description = "실시간 대시보드 통계를 조회합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<StatisticsResponses.DashboardSummary>> getDashboardSummary() {
        return ok(statisticsFacade.getDashboardSummary());
    }

    @GetMapping("/department")
    @Operation(summary = "과별 통계 조회", description = "과별 진료 통계를 조회합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<List<StatisticsResponses.DepartmentStatistics>>> getDepartmentStatistics(
            @Parameter(description = "시작일") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "종료일") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ok(statisticsFacade.getDepartmentStatistics(startDate, endDate));
    }

    @GetMapping("/revenue")
    @Operation(summary = "수익 통계 조회", description = "기간별 수익 통계를 조회합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<StatisticsResponses.RevenueStatistics>> getRevenueStatistics(
            @Parameter(description = "시작일") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "종료일") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ok(statisticsFacade.getRevenueStatistics(startDate, endDate));
    }

    @GetMapping("/inventory")
    @Operation(summary = "재고 통계 조회", description = "약품 재고 통계를 조회합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<StatisticsResponses.InventoryStatistics>> getInventoryStatistics() {
        return ok(statisticsFacade.getInventoryStatistics());
    }

    @GetMapping("/daily")
    @Operation(summary = "일별 통계 조회", description = "최근 N일간의 일별 통계를 조회합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<List<StatisticsResponses.DailyStatistics>>> getDailyStatistics(
            @Parameter(description = "조회 일수 (기본값: 30)") @RequestParam(defaultValue = "30") int days) {
        return ok(statisticsFacade.getDailyStatistics(days));
    }
}

