package com.emrsystem.domain.notification.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class NotificationRequests {

    @Getter
    @NoArgsConstructor
    public static class CreateTemplateRequest {
        @NotBlank(message = "채널은 필수입니다.")
        @Size(max = 20, message = "채널은 20자를 초과할 수 없습니다.")
        private String channel;

        @NotBlank(message = "템플릿 코드는 필수입니다.")
        @Size(max = 50, message = "템플릿 코드는 50자를 초과할 수 없습니다.")
        private String code;

        @NotBlank(message = "제목은 필수입니다.")
        @Size(max = 200, message = "제목은 200자를 초과할 수 없습니다.")
        private String title;

        @NotBlank(message = "내용은 필수입니다.")
        @Size(max = 1000, message = "내용은 1000자를 초과할 수 없습니다.")
        private String body;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateTemplateRequest {
        @NotBlank(message = "채널은 필수입니다.")
        @Size(max = 20, message = "채널은 20자를 초과할 수 없습니다.")
        private String channel;

        @NotBlank(message = "제목은 필수입니다.")
        @Size(max = 200, message = "제목은 200자를 초과할 수 없습니다.")
        private String title;

        @NotBlank(message = "내용은 필수입니다.")
        @Size(max = 1000, message = "내용은 1000자를 초과할 수 없습니다.")
        private String body;
    }

    @Getter
    @NoArgsConstructor
    public static class EnqueueNotificationRequest {
        @NotBlank(message = "채널은 필수입니다.")
        @Size(max = 20, message = "채널은 20자를 초과할 수 없습니다.")
        private String channel;

        @NotBlank(message = "수신자는 필수입니다.")
        @Size(max = 100, message = "수신자는 100자를 초과할 수 없습니다.")
        private String recipient;

        @NotBlank(message = "템플릿 코드는 필수입니다.")
        @Size(max = 50, message = "템플릿 코드는 50자를 초과할 수 없습니다.")
        private String templateCode;

        @NotBlank(message = "페이로드는 필수입니다.")
        @Size(max = 2000, message = "페이로드는 2000자를 초과할 수 없습니다.")
        private String payloadJson;
    }
}
