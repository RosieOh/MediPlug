package com.emrsystem.global.security;

import com.emrsystem.domain.user.entity.TwoFactorAuth;
import com.emrsystem.domain.user.entity.UserAccount;
import com.emrsystem.domain.user.service.UserAccountService;
import com.emrsystem.global.common.controller.BaseController;
import com.emrsystem.global.common.dto.ApiResponse;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import com.emrsystem.global.security.service.SessionManagementServiceInterface;
import com.emrsystem.global.security.service.TwoFactorAuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserAccountService userAccountService;
    private final TwoFactorAuthService twoFactorAuthService;
    private final SessionManagementServiceInterface sessionManagementService;

    public record LoginRequest(@NotBlank String username, @NotBlank String password, String twoFactorCode) {}

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        String username = auth.getName();
        UserAccount user = userAccountService.loadByUsername(username);
        
        // 2FA 검증
        TwoFactorAuth twoFactorAuth = twoFactorAuthService.getTwoFactorAuth(user.getId());
        if (twoFactorAuth != null && twoFactorAuth.isEnabled()) {
            if (request.twoFactorCode() == null || request.twoFactorCode().isEmpty()) {
                throw new CommonException(ErrorCode.UNAUTHORIZED, "2FA 인증 코드가 필요합니다.");
            }
            if (!twoFactorAuthService.verifyCode(user.getId(), request.twoFactorCode())) {
                throw new CommonException(ErrorCode.UNAUTHORIZED, "2FA 인증 코드가 올바르지 않습니다.");
            }
        }
        
        List<String> roles = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        String access = jwtTokenProvider.createAccessToken(username, roles);
        String refresh = jwtTokenProvider.createRefreshToken(username, roles);
        
        // 세션 생성
        sessionManagementService.createSession(username, access, httpRequest);
        
        return ok(Map.of("accessToken", access, "refreshToken", refresh));
    }

    public record RefreshRequest(@NotBlank String refreshToken) {}

    @PostMapping("/refresh")
    @Operation(summary = "토큰 갱신")
    public ResponseEntity<ApiResponse<Map<String, String>>> refresh(@RequestBody RefreshRequest request) {
        var claims = jwtTokenProvider.parseClaims(request.refreshToken());
        String username = claims.getSubject();
        UserAccount user = userAccountService.loadByUsername(username);
        List<String> roles = user.getRoles().stream().toList();
        String access = jwtTokenProvider.createAccessToken(username, roles);
        return ok(Map.of("accessToken", access));
    }
}


