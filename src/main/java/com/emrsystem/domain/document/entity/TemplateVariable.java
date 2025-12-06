package com.emrsystem.domain.document.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "template_variable")
public class TemplateVariable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "variable_id")
    private Long variableId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    @Setter
    private DocumentTemplate template;

    @Column(nullable = false, length = 100)
    private String variableName; // 변수명 (예: patientName, diagnosis, date)

    @Column(length = 200)
    private String displayName; // 표시명 (예: 환자명, 진단명, 날짜)

    @Column(length = 50)
    private String dataType; // 데이터 타입 (STRING, NUMBER, DATE, BOOLEAN, OBJECT)

    @Column(columnDefinition = "TEXT")
    private String defaultValue; // 기본값

    @Column(columnDefinition = "TEXT")
    private String description; // 변수 설명

    @Column(nullable = false)
    private boolean required = false; // 필수 여부

    @Column(length = 500)
    private String validationRule; // 검증 규칙 (JSON 형식)

    private TemplateVariable(DocumentTemplate template, String variableName, String displayName,
                            String dataType, String defaultValue, String description, boolean required,
                            String validationRule) {
        this.template = template;
        this.variableName = variableName;
        this.displayName = displayName;
        this.dataType = dataType;
        this.defaultValue = defaultValue;
        this.description = description;
        this.required = required;
        this.validationRule = validationRule;
    }

    public static TemplateVariable create(DocumentTemplate template, String variableName, String displayName,
                                         String dataType, String defaultValue, String description,
                                         boolean required, String validationRule) {
        return new TemplateVariable(template, variableName, displayName, dataType, defaultValue,
                description, required, validationRule);
    }

    public void update(String displayName, String dataType, String defaultValue, String description,
                      boolean required, String validationRule) {
        this.displayName = displayName;
        this.dataType = dataType;
        this.defaultValue = defaultValue;
        this.description = description;
        this.required = required;
        this.validationRule = validationRule;
    }
}

