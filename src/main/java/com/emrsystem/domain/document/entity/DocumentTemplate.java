package com.emrsystem.domain.document.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "document_template")
@EntityListeners(AuditingEntityListener.class)
public class DocumentTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    private Long templateId;

    @Column(nullable = false, length = 100)
    private String templateName; // 템플릿 이름

    @Column(nullable = false, length = 50)
    private String documentType; // 문서 타입 (DIAGNOSIS_CERTIFICATE, MEDICAL_OPINION, DISCHARGE_SUMMARY, etc.)

    @Column(nullable = false, length = 50)
    private String format; // PDF, WORD, EXCEL, HTML

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 템플릿 내용 (HTML, Markdown, 또는 템플릿 문법)

    @Column(length = 500)
    private String description; // 템플릿 설명

    @Column(nullable = false)
    private boolean active = true; // 활성화 여부

    @Column(nullable = false)
    private boolean isDefault = false; // 기본 템플릿 여부

    @Column(length = 100)
    private String version; // 템플릿 버전

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TemplateVariable> variables = new ArrayList<>(); // 템플릿 변수 목록

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private DocumentTemplate(String templateName, String documentType, String format, String content,
                            String description, boolean isDefault, String version) {
        this.templateName = templateName;
        this.documentType = documentType;
        this.format = format;
        this.content = content;
        this.description = description;
        this.isDefault = isDefault;
        this.version = version;
    }

    public static DocumentTemplate create(String templateName, String documentType, String format,
                                          String content, String description, boolean isDefault, String version) {
        return new DocumentTemplate(templateName, documentType, format, content, description, isDefault, version);
    }

    public void update(String templateName, String content, String description, String version) {
        this.templateName = templateName;
        this.content = content;
        this.description = description;
        this.version = version;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public void setAsDefault() {
        this.isDefault = true;
    }

    public void unsetAsDefault() {
        this.isDefault = false;
    }

    public void addVariable(TemplateVariable variable) {
        this.variables.add(variable);
        variable.setTemplate(this);
    }

    public void removeVariable(TemplateVariable variable) {
        this.variables.remove(variable);
        variable.setTemplate(null);
    }

    public Long getId() {
        return this.templateId;
    }
}

