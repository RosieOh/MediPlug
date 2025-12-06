package com.emrsystem.domain.document.entity;

import com.emrsystem.domain.user.entity.UserAccount;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "template_version")
@EntityListeners(AuditingEntityListener.class)
public class TemplateVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "version_id")
    private Long versionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private DocumentTemplate template;

    @Column(nullable = false, length = 100)
    private String version; // 버전 번호 (예: 1.0, 1.1, 2.0)

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 해당 버전의 템플릿 내용

    @Column(length = 500)
    private String changeDescription; // 변경 사항 설명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private UserAccount createdBy; // 버전 생성자

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TEXT")
    private String metadata; // 추가 메타데이터 (JSON 형식)

    private TemplateVersion(DocumentTemplate template, String version, String content,
                           String changeDescription, UserAccount createdBy, String metadata) {
        this.template = template;
        this.version = version;
        this.content = content;
        this.changeDescription = changeDescription;
        this.createdBy = createdBy;
        this.metadata = metadata;
    }

    public static TemplateVersion create(DocumentTemplate template, String version, String content,
                                        String changeDescription, UserAccount createdBy, String metadata) {
        return new TemplateVersion(template, version, content, changeDescription, createdBy, metadata);
    }

    public Long getId() {
        return this.versionId;
    }
}
