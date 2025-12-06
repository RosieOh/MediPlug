package com.emrsystem.domain.integration.controller;

import com.emrsystem.domain.integration.facade.ExternalIntegrationFacade;
import com.emrsystem.domain.integration.response.IntegrationResponses;
import com.emrsystem.domain.user.enums.RoleType;
import com.emrsystem.global.common.controller.BaseController;
import com.emrsystem.global.common.dto.ApiResponse;
import com.emrsystem.global.security.authorization.RequireRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "External Integration", description = "외부 시스템 연동 API")
@RestController
@RequestMapping("/api/integration")
@RequiredArgsConstructor
public class ExternalIntegrationController extends BaseController {

    private final ExternalIntegrationFacade externalIntegrationFacade;

    @GetMapping("/status/{systemType}")
    @Operation(summary = "외부 시스템 연동 상태 조회", description = "외부 시스템의 연동 상태를 조회합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<IntegrationResponses.IntegrationStatus>> getIntegrationStatus(
            @Parameter(description = "시스템 유형 (HL7_FHIR, LIS, PACS, PHARMACY, INSURANCE)") @PathVariable String systemType) {
        IntegrationResponses.IntegrationStatus status = externalIntegrationFacade.getIntegrationStatus(systemType);
        return ok(status);
    }

    @PostMapping("/sync/{systemType}")
    @Operation(summary = "외부 시스템 동기화", description = "외부 시스템과 데이터를 동기화합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<Void>> syncWithExternalSystem(
            @Parameter(description = "시스템 유형") @PathVariable String systemType) {
        externalIntegrationFacade.syncWithExternalSystem(systemType);
        return ok(null);
    }
}

