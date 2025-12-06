package com.emrsystem.domain.pharmacy.entity;

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
@Table(name = "drug_interaction")
@EntityListeners(AuditingEntityListener.class)
public class DrugInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interaction_id")
    private Long interactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drug1_id", nullable = false)
    private DrugMaster drug1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drug2_id", nullable = false)
    private DrugMaster drug2;

    @Column(nullable = false, length = 50)
    private String severity; // MINOR, MODERATE, MAJOR, CONTRAINDICATED

    @Column(columnDefinition = "TEXT")
    private String description; // 상호작용 설명

    @Column(columnDefinition = "TEXT")
    private String clinicalSignificance; // 임상적 의미

    @Column(columnDefinition = "TEXT")
    private String management; // 관리 방법

    @Column(nullable = false)
    private boolean active = true; // 활성 여부

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private DrugInteraction(DrugMaster drug1, DrugMaster drug2, String severity, String description,
                           String clinicalSignificance, String management, boolean active,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.drug1 = drug1;
        this.drug2 = drug2;
        this.severity = severity;
        this.description = description;
        this.clinicalSignificance = clinicalSignificance;
        this.management = management;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static DrugInteraction create(DrugMaster drug1, DrugMaster drug2, String severity,
                                       String description, String clinicalSignificance, String management) {
        LocalDateTime now = LocalDateTime.now();
        // drug1과 drug2의 ID를 비교하여 항상 작은 ID가 drug1이 되도록 정렬
        if (drug1.getDrugMasterId() > drug2.getDrugMasterId()) {
            DrugMaster temp = drug1;
            drug1 = drug2;
            drug2 = temp;
        }
        return new DrugInteraction(drug1, drug2, severity, description, clinicalSignificance, management, true, now, now);
    }

    public void update(String severity, String description, String clinicalSignificance, String management) {
        this.severity = severity;
        this.description = description;
        this.clinicalSignificance = clinicalSignificance;
        this.management = management;
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    public boolean isContraindicated() {
        return "CONTRAINDICATED".equals(severity);
    }

    public boolean isMajor() {
        return "MAJOR".equals(severity) || isContraindicated();
    }

    public Long getId() {
        return this.interactionId;
    }
}

