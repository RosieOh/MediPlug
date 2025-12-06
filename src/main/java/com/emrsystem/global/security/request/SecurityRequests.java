package com.emrsystem.global.security.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SecurityRequests {

    @Getter
    @NoArgsConstructor
    public static class EnableTwoFactorRequest {
        @NotBlank(message = "인증 코드는 필수입니다.")
        @Size(min = 6, max = 6, message = "인증 코드는 6자리여야 합니다.")
        private String code;
    }

    @Getter
    @NoArgsConstructor
    public static class VerifyTwoFactorRequest {
        @NotBlank(message = "인증 코드는 필수입니다.")
        @Size(min = 6, max = 6, message = "인증 코드는 6자리여야 합니다.")
        private String code;
    }
}

