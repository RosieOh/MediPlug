package com.emrsystem.domain.common.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AttachmentRequests {

    @Getter
    @NoArgsConstructor
    public static class CreateAttachmentRequest {
        @NotBlank(message = "엔티티 타입은 필수입니다.")
        @Size(max = 100, message = "엔티티 타입은 100자를 초과할 수 없습니다.")
        private String entityType; // MEDICAL_RECORD, PRESCRIPTION, LAB_ORDER, etc.

        @NotNull(message = "엔티티 ID는 필수입니다.")
        private Long entityId;

        @NotBlank(message = "파일명은 필수입니다.")
        @Size(max = 200, message = "파일명은 200자를 초과할 수 없습니다.")
        private String fileName;

        @NotBlank(message = "파일 경로는 필수입니다.")
        @Size(max = 500, message = "파일 경로는 500자를 초과할 수 없습니다.")
        private String filePath;

        @NotNull(message = "파일 크기는 필수입니다.")
        private Long fileSize;

        @Size(max = 100, message = "MIME 타입은 100자를 초과할 수 없습니다.")
        private String mimeType;

        @Size(max = 50, message = "카테고리는 50자를 초과할 수 없습니다.")
        private String category; // IMAGE, DOCUMENT, LAB_RESULT, etc.

        private String description;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateAttachmentRequest {
        @NotBlank(message = "파일명은 필수입니다.")
        @Size(max = 200, message = "파일명은 200자를 초과할 수 없습니다.")
        private String fileName;

        private String description;
    }
}

