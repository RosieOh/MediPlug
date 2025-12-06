package com.emrsystem.domain.user.controller;

import com.emrsystem.domain.user.enums.RoleType;
import com.emrsystem.domain.user.request.UserRequests;
import com.emrsystem.domain.user.response.UserResponses;
import com.emrsystem.domain.user.service.UserAccountService;
import com.emrsystem.global.common.controller.BaseController;
import com.emrsystem.global.common.dto.ApiResponse;
import com.emrsystem.global.common.dto.PageResponse;
import com.emrsystem.global.security.authorization.RequireRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Management", description = "사용자 관리 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final UserAccountService userAccountService;

    @GetMapping
    @Operation(summary = "사용자 목록 조회", description = "사용자 목록을 조회합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<PageResponse<UserResponses.UserSummary>>> list(
            @Parameter(description = "페이지 정보") @PageableDefault(size = 20) Pageable pageable) {
        Page<UserResponses.UserSummary> users = userAccountService.list(pageable)
                .map(UserResponses.UserSummary::from);
        return ok(PageResponse.of(users));
    }

    @GetMapping("/{id}")
    @Operation(summary = "사용자 상세 조회", description = "특정 사용자의 상세 정보를 조회합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<UserResponses.UserSummary>> get(
            @Parameter(description = "사용자 ID") @PathVariable Long id) {
        return ok(UserResponses.UserSummary.from(userAccountService.get(id)));
    }

    @PostMapping
    @Operation(summary = "사용자 생성", description = "새로운 사용자를 생성합니다.")
    @RequireRole(RoleType.MASTER_ADMIN)
    public ResponseEntity<ApiResponse<UserResponses.UserSummary>> create(
            @Valid @RequestBody UserRequests.CreateUserRequest request) {
        return created(UserResponses.UserSummary.from(userAccountService.create(request)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "사용자 정보 수정", description = "사용자 정보를 수정합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<UserResponses.UserSummary>> update(
            @Parameter(description = "사용자 ID") @PathVariable Long id,
            @Valid @RequestBody UserRequests.UpdateUserRequest request) {
        return ok(UserResponses.UserSummary.from(userAccountService.update(id, request)));
    }

    @PutMapping("/{id}/password")
    @Operation(summary = "비밀번호 변경", description = "사용자 비밀번호를 변경합니다.")
    @PreAuthorize("hasAuthority('ROLE_MASTER_ADMIN') or (hasAuthority('ROLE_ADMIN') and @userAccountService.get(#id).username == authentication.name)")
    public ResponseEntity<ApiResponse<String>> updatePassword(
            @Parameter(description = "사용자 ID") @PathVariable Long id,
            @Valid @RequestBody UserRequests.UpdatePasswordRequest request) {
        userAccountService.updatePassword(id, request);
        return okMessage("password updated");
    }

    @PutMapping("/{id}/roles")
    @Operation(summary = "사용자 역할 변경", description = "사용자의 역할을 변경합니다.")
    @RequireRole(RoleType.MASTER_ADMIN)
    public ResponseEntity<ApiResponse<UserResponses.UserSummary>> updateRoles(
            @Parameter(description = "사용자 ID") @PathVariable Long id,
            @Valid @RequestBody UserRequests.UpdateRolesRequest request) {
        return ok(UserResponses.UserSummary.from(userAccountService.updateRoles(id, request)));
    }

    @PostMapping("/{id}/activate")
    @Operation(summary = "사용자 활성화", description = "사용자를 활성화합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<UserResponses.UserSummary>> activate(
            @Parameter(description = "사용자 ID") @PathVariable Long id) {
        return ok(UserResponses.UserSummary.from(userAccountService.activate(id)));
    }

    @PostMapping("/{id}/deactivate")
    @Operation(summary = "사용자 비활성화", description = "사용자를 비활성화합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<UserResponses.UserSummary>> deactivate(
            @Parameter(description = "사용자 ID") @PathVariable Long id) {
        return ok(UserResponses.UserSummary.from(userAccountService.deactivate(id)));
    }

    @PostMapping("/{id}/lock")
    @Operation(summary = "사용자 잠금", description = "사용자 계정을 잠급니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<UserResponses.UserSummary>> lock(
            @Parameter(description = "사용자 ID") @PathVariable Long id) {
        return ok(UserResponses.UserSummary.from(userAccountService.lock(id)));
    }

    @PostMapping("/{id}/unlock")
    @Operation(summary = "사용자 잠금 해제", description = "사용자 계정 잠금을 해제합니다.")
    @RequireRole(RoleType.ADMIN)
    public ResponseEntity<ApiResponse<UserResponses.UserSummary>> unlock(
            @Parameter(description = "사용자 ID") @PathVariable Long id) {
        return ok(UserResponses.UserSummary.from(userAccountService.unlock(id)));
    }
}

