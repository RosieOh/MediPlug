package com.emrsystem.domain.document.entity;

import com.emrsystem.domain.hospital.entity.Hospital;
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
@Table(name = "hospital_template", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"template_id", "hospital_id"})
})
@EntityListeners(AuditingEntityListener.class)
public class HospitalTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospital_template_id")
    private Long hospitalTemplateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private DocumentTemplate template;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @Column(nullable = false)
    private boolean shared = false; // 공유 여부 (다른 병원에서 사용 가능)

    @Column(nullable = false)
    private boolean active = true; // 활성화 여부

    @Column(length = 500)
    private String notes; // 병원별 메모

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private HospitalTemplate(DocumentTemplate template, Hospital hospital, boolean shared, String notes) {
        this.template = template;
        this.hospital = hospital;
        this.shared = shared;
        this.notes = notes;
    }

    public static HospitalTemplate create(DocumentTemplate template, Hospital hospital, boolean shared, String notes) {
        return new HospitalTemplate(template, hospital, shared, notes);
    }

    public void update(boolean shared, String notes) {
        this.shared = shared;
        this.notes = notes;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public void share() {
        this.shared = true;
    }

    public void unshare() {
        this.shared = false;
    }

    public Long getId() {
        return this.hospitalTemplateId;
    }
}
