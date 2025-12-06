package com.emrsystem.domain.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

public class UserRequests {

    @Getter
    @NoArgsConstructor
    public static class CreateUserRequest {
        @NotBlank(message = "사용자명은 필수입니다.")
        @Size(max = 100, message = "사용자명은 100자를 초과할 수 없습니다.")
        private String username;

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 8, max = 200, message = "비밀번호는 8자 이상 200자 이하여야 합니다.")
        private String password;

        @NotBlank(message = "이름은 필수입니다.")
        @Size(max = 100, message = "이름은 100자를 초과할 수 없습니다.")
        private String name;

        @Email(message = "올바른 이메일 형식이 아닙니다.")
        @Size(max = 100, message = "이메일은 100자를 초과할 수 없습니다.")
        private String email;

        @Size(max = 30, message = "전화번호는 30자를 초과할 수 없습니다.")
        private String phone;

        @NotNull(message = "역할은 필수입니다.")
        private Set<String> roles; // ROLE_MASTER_ADMIN, ROLE_ADMIN, ROLE_DOCTOR, ROLE_NURSE
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateUserRequest {
        @NotBlank(message = "이름은 필수입니다.")
        @Size(max = 100, message = "이름은 100자를 초과할 수 없습니다.")
        private String name;

        @Email(message = "올바른 이메일 형식이 아닙니다.")
        @Size(max = 100, message = "이메일은 100자를 초과할 수 없습니다.")
        private String email;

        @Size(max = 30, message = "전화번호는 30자를 초과할 수 없습니다.")
        private String phone;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdatePasswordRequest {
        @NotBlank(message = "현재 비밀번호는 필수입니다.")
        private String currentPassword;

        @NotBlank(message = "새 비밀번호는 필수입니다.")
        @Size(min = 8, max = 200, message = "비밀번호는 8자 이상 200자 이하여야 합니다.")
        private String newPassword;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateRolesRequest {
        @NotNull(message = "역할은 필수입니다.")
        private Set<String> roles;
    }
}

