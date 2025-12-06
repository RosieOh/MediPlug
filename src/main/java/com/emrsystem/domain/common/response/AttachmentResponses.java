package com.emrsystem.domain.common.response;

import com.emrsystem.domain.common.entity.Attachment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class AttachmentResponses {

    @Getter
    @AllArgsConstructor
    public static class AttachmentSummary {
        private Long attachmentId;
        private String entityType;
        private Long entityId;
        private String fileName;
        private String filePath;
        private Long fileSize;
        private String mimeType;
        private String category;
        private String description;
        private boolean active;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static AttachmentSummary from(Attachment attachment) {
            return new AttachmentSummary(
                    attachment.getId(),
                    attachment.getEntityType(),
                    attachment.getEntityId(),
                    attachment.getFileName(),
                    attachment.getFilePath(),
                    attachment.getFileSize(),
                    attachment.getMimeType(),
                    attachment.getCategory(),
                    attachment.getDescription(),
                    attachment.isActive(),
                    attachment.getCreatedAt(),
                    attachment.getUpdatedAt()
            );
        }
    }
}

