package com.emrsystem.domain.report.controller;

import com.emrsystem.domain.report.facade.ReportFacade;
import com.emrsystem.domain.report.request.ReportRequests;
import com.emrsystem.domain.user.enums.RoleType;
import com.emrsystem.global.common.controller.BaseController;
import com.emrsystem.global.common.dto.ApiResponse;
import com.emrsystem.global.security.authorization.RequireRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Report Generation", description = "리포트 생성 API")
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController extends BaseController {

    private final ReportFacade reportFacade;

    @PostMapping("/generate")
    @Operation(summary = "리포트 생성", description = "다양한 유형의 리포트를 생성합니다 (PDF/Excel).")
    @RequireRole(RoleType.DOCTOR)
    public ResponseEntity<Resource> generateReport(
            @Valid @RequestBody ReportRequests.GenerateReportRequest request) {
        Resource resource = reportFacade.generateReport(request);

        String contentType = "PDF".equalsIgnoreCase(request.getFormat()) 
                ? MediaType.APPLICATION_PDF_VALUE 
                : "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
        
        String extension = "PDF".equalsIgnoreCase(request.getFormat()) ? "pdf" : "xlsx";
        String filename = request.getReportType().toLowerCase() + "_" + System.currentTimeMillis() + "." + extension;

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}

