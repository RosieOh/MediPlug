package com.emrsystem.domain.emr.entity;

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
@Table(name = "chart_template")
@EntityListeners(AuditingEntityListener.class)
public class ChartTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chart_template_id")
    private Long chartTemplateId;

    @Column(nullable = false, length = 100)
    private String name; // 템플릿 이름

    @Column(nullable = false, length = 50)
    private String category; // 내과, 외과, 소아과 등

    @Column(nullable = false, length = 100)
    private String code; // 템플릿 코드

    @Column(nullable = false, columnDefinition = "TEXT")
    private String templateContent; // JSON 형태의 템플릿 내용

    @Column(length = 500)
    private String description; // 템플릿 설명

    @Column(nullable = false)
    private boolean active = true; // 활성화 여부

    @Column(nullable = false)
    private int version = 1; // 버전

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private ChartTemplate(String name, String category, String code, String templateContent, String description) {
        this.name = name;
        this.category = category;
        this.code = code;
        this.templateContent = templateContent;
        this.description = description;
    }

    public static ChartTemplate create(String name, String category, String code, String templateContent, String description) {
        return new ChartTemplate(name, category, code, templateContent, description);
    }

    public void update(String name, String category, String templateContent, String description) {
        this.name = name;
        this.category = category;
        this.templateContent = templateContent;
        this.description = description;
        this.version++;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public Long getId() { return this.chartTemplateId; }
}
