package com.emrsystem.domain.surgery.entity;

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
@Table(name = "pre_op_checklist")
@EntityListeners(AuditingEntityListener.class)
public class PreOpChecklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "checklist_id")
    private Long checklistId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surgery_id", nullable = false)
    private Surgery surgery;

    @Column(nullable = false, length = 200)
    private String item; // 체크 항목 (예: 금식 확인, 알레르기 확인, 혈액형 확인 등)

    @Column(nullable = false)
    private boolean checked = false; // 체크 여부

    @Column(length = 200)
    private String checkedBy; // 체크한 사람

    @Column
    private LocalDateTime checkedAt; // 체크 일시

    @Column(columnDefinition = "TEXT")
    private String notes; // 비고

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private PreOpChecklist(Surgery surgery, String item, boolean checked, String checkedBy,
                          LocalDateTime checkedAt, String notes) {
        this.surgery = surgery;
        this.item = item;
        this.checked = checked;
        this.checkedBy = checkedBy;
        this.checkedAt = checkedAt;
        this.notes = notes;
    }

    public static PreOpChecklist create(Surgery surgery, String item) {
        return new PreOpChecklist(surgery, item, false, null, null, null);
    }

    public void check(String checkedBy) {
        this.checked = true;
        this.checkedBy = checkedBy;
        this.checkedAt = LocalDateTime.now();
    }

    public void uncheck() {
        this.checked = false;
        this.checkedBy = null;
        this.checkedAt = null;
    }

    public void updateNotes(String notes) {
        this.notes = notes;
    }

    public Long getId() {
        return this.checklistId;
    }
}

