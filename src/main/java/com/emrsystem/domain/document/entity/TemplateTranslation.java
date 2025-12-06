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
@Table(name = "template_translation", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"template_id", "language_code"})
})
@EntityListeners(AuditingEntityListener.class)
public class TemplateTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "translation_id")
    private Long translationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private DocumentTemplate template;

    @Column(nullable = false, length = 10)
    private String languageCode; // 언어 코드 (ko, en, zh, ja 등)

    @Column(nullable = false, length = 100)
    private String templateName; // 번역된 템플릿 이름

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 번역된 템플릿 내용

    @Column(length = 500)
    private String description; // 번역된 설명

    @Column(nullable = false)
    private boolean active = true; // 활성화 여부

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private TemplateTranslation(DocumentTemplate template, String languageCode, String templateName,
                                String content, String description) {
        this.template = template;
        this.languageCode = languageCode;
        this.templateName = templateName;
        this.content = content;
        this.description = description;
    }

    public static TemplateTranslation create(DocumentTemplate template, String languageCode,
                                           String templateName, String content, String description) {
        return new TemplateTranslation(template, languageCode, templateName, content, description);
    }

    public void update(String templateName, String content, String description) {
        this.templateName = templateName;
        this.content = content;
        this.description = description;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public Long getId() {
        return this.translationId;
    }
}
