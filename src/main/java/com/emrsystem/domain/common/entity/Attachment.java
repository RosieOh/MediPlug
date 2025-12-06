package com.emrsystem.domain.common.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "attachment")
@EntityListeners(AuditingEntityListener.class)
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attachment_id")
    private Long attachmentId;

    @Column(nullable = false, length = 100)
    private String entityType; // MEDICAL_RECORD, PRESCRIPTION, LAB_ORDER, MEDICAL_IMAGE, BILLING, etc.

    @Column(nullable = false)
    private Long entityId; // 연결된 엔티티의 ID

    @Column(nullable = false, length = 200)
    private String fileName;

    @Column(nullable = false, length = 500)
    private String filePath;

    @Column(nullable = false)
    private Long fileSize; // 파일 크기 (bytes)

    @Column(length = 100)
    private String mimeType; // MIME 타입

    @Column(length = 50)
    private String category; // IMAGE, DOCUMENT, LAB_RESULT, CERTIFICATE, etc.

    @Column(length = 500)
    private String description; // 설명

    @Column(nullable = false)
    private boolean active = true; // 활성 여부

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private Attachment(String entityType, Long entityId, String fileName, String filePath, Long fileSize,
                      String mimeType, String category, String description, boolean active,
                      LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.mimeType = mimeType;
        this.category = category;
        this.description = description;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Attachment create(String entityType, Long entityId, String fileName, String filePath,
                                    Long fileSize, String mimeType, String category, String description) {
        LocalDateTime now = LocalDateTime.now();
        return new Attachment(entityType, entityId, fileName, filePath, fileSize, mimeType, category, description, true, now, now);
    }

    public void update(String fileName, String description) {
        this.fileName = fileName;
        this.description = description;
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    public Long getId() {
        return this.attachmentId;
    }
}

