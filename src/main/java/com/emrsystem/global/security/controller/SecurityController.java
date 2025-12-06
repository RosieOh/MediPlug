package com.emrsystem.global.security.controller;

import com.emrsystem.domain.user.entity.TwoFactorAuth;
import com.emrsystem.domain.user.entity.UserAccount;
import com.emrsystem.domain.user.repository.UserAccountRepository;
import com.emrsystem.global.common.controller.BaseController;
import com.emrsystem.global.common.dto.ApiResponse;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import com.emrsystem.global.security.entity.UserSession;
import com.emrsystem.global.security.request.SecurityRequests;
import com.emrsystem.global.security.response.SecurityResponses;
import com.emrsystem.global.security.service.SessionManagementServiceInterface;
import com.emrsystem.global.security.service.TwoFactorAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Security", description = "보안 관리 API")
@RestController
@RequestMapping("/api/security")
@RequiredArgsConstructor
public class SecurityController extends BaseController {

    private final TwoFactorAuthService twoFactorAuthService;
    private final SessionManagementServiceInterface sessionManagementService;
    private final UserAccountRepository userAccountRepository;

    // 2FA endpoints
    @PostMapping("/2fa/setup")
    @Operation(summary = "2FA 설정 시작", description = "2FA 설정을 시작하고 QR 코드를 생성합니다.")
    public ResponseEntity<ApiResponse<SecurityResponses.TwoFactorSetup>> setupTwoFactorAuth(Authentication authentication) {
        UserAccount userAccount = userAccountRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "사용자를 찾을 수 없습니다."));

        TwoFactorAuth twoFactorAuth = twoFactorAuthService.setupTwoFactorAuth(userAccount.getId());
        String qrCodeUri = twoFactorAuthService.generateQrCodeUri(userAccount.getUsername(), twoFactorAuth.getSecretKey());
        List<String> backupCodes = twoFactorAuthService.generateBackupCodes();

        SecurityResponses.TwoFactorSetup setup = SecurityResponses.TwoFactorSetup.builder()
                .secretKey(twoFactorAuth.getSecretKey())
                .qrCodeUri(qrCodeUri)
                .backupCodes(backupCodes)
                .build();

        return ok(setup);
    }

    @PostMapping("/2fa/enable")
    @Operation(summary = "2FA 활성화", description = "인증 코드를 검증하여 2FA를 활성화합니다.")
    public ResponseEntity<ApiResponse<String>> enableTwoFactorAuth(
            Authentication authentication,
            @Valid @RequestBody SecurityRequests.EnableTwoFactorRequest request) {
        UserAccount userAccount = userAccountRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "사용자를 찾을 수 없습니다."));

        twoFactorAuthService.enableTwoFactorAuth(userAccount.getId(), request.getCode());
        return ok("2FA가 활성화되었습니다.");
    }

    @PostMapping("/2fa/disable")
    @Operation(summary = "2FA 비활성화", description = "2FA를 비활성화합니다.")
    public ResponseEntity<ApiResponse<String>> disableTwoFactorAuth(Authentication authentication) {
        UserAccount userAccount = userAccountRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "사용자를 찾을 수 없습니다."));

        twoFactorAuthService.disableTwoFactorAuth(userAccount.getId());
        return ok("2FA가 비활성화되었습니다.");
    }

    @GetMapping("/2fa/status")
    @Operation(summary = "2FA 상태 조회", description = "현재 사용자의 2FA 상태를 조회합니다.")
    public ResponseEntity<ApiResponse<Boolean>> getTwoFactorAuthStatus(Authentication authentication) {
        UserAccount userAccount = userAccountRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "사용자를 찾을 수 없습니다."));

        TwoFactorAuth twoFactorAuth = twoFactorAuthService.getTwoFactorAuth(userAccount.getId());
        return ok(twoFactorAuth != null && twoFactorAuth.isEnabled());
    }

    // Session management endpoints
    @GetMapping("/sessions")
    @Operation(summary = "활성 세션 조회", description = "현재 사용자의 모든 활성 세션을 조회합니다.")
    public ResponseEntity<ApiResponse<List<SecurityResponses.SessionSummary>>> getActiveSessions(Authentication authentication) {
        List<UserSession> sessions = sessionManagementService.getUserSessions(authentication.getName());
        List<SecurityResponses.SessionSummary> summaries = sessions.stream()
                .map(SecurityResponses.SessionSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @DeleteMapping("/sessions/{sessionId}")
    @Operation(summary = "세션 종료", description = "특정 세션을 종료합니다.")
    public ResponseEntity<ApiResponse<String>> invalidateSession(
            @Parameter(description = "세션 ID") @PathVariable String sessionId) {
        sessionManagementService.invalidateSession(sessionId);
        return ok("세션이 종료되었습니다.");
    }

    @DeleteMapping("/sessions/all")
    @Operation(summary = "모든 세션 종료", description = "현재 사용자의 모든 세션을 종료합니다.")
    public ResponseEntity<ApiResponse<String>> invalidateAllSessions(Authentication authentication) {
        sessionManagementService.invalidateAllUserSessions(authentication.getName());
        return ok("모든 세션이 종료되었습니다.");
    }
}

