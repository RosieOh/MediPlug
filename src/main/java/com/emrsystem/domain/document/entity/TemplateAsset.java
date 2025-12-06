package com.emrsystem.domain.document.entity;

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
@Table(name = "template_asset")
@EntityListeners(AuditingEntityListener.class)
public class TemplateAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asset_id")
    private Long assetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private DocumentTemplate template; // 특정 템플릿에 속한 경우 (null이면 공용)

    @Column(nullable = false, length = 200)
    private String assetName; // 에셋 이름

    @Column(nullable = false, length = 50)
    private String assetType; // IMAGE, CHART, LOGO, SIGNATURE 등

    @Column(nullable = false, length = 500)
    private String filePath; // 파일 경로 또는 URL

    @Column(length = 100)
    private String mimeType; // MIME 타입 (image/png, image/jpeg 등)

    @Column
    private Long fileSize; // 파일 크기 (bytes)

    @Column(length = 500)
    private String description; // 에셋 설명

    @Column(nullable = false)
    private boolean active = true; // 활성화 여부

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private TemplateAsset(DocumentTemplate template, String assetName, String assetType,
                         String filePath, String mimeType, Long fileSize, String description) {
        this.template = template;
        this.assetName = assetName;
        this.assetType = assetType;
        this.filePath = filePath;
        this.mimeType = mimeType;
        this.fileSize = fileSize;
        this.description = description;
    }

    public static TemplateAsset create(DocumentTemplate template, String assetName, String assetType,
                                      String filePath, String mimeType, Long fileSize, String description) {
        return new TemplateAsset(template, assetName, assetType, filePath, mimeType, fileSize, description);
    }

    public void update(String assetName, String description) {
        this.assetName = assetName;
        this.description = description;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public Long getId() {
        return this.assetId;
    }
}
