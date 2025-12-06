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
@Table(name = "lab_result")
@EntityListeners(AuditingEntityListener.class)
public class LabResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lab_result_id")
    private Long labResultId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_order_id", nullable = false)
    private LabOrder labOrder;

    @Column(nullable = false, length = 200)
    private String testItemName; // 검사 항목명

    @Column(length = 50)
    private String testItemCode; // 검사 항목 코드

    @Column(length = 200)
    private String resultValue; // 결과값

    @Column(length = 50)
    private String unit; // 단위

    @Column(length = 100)
    private String referenceRange; // 정상 범위 (예: "3.5-5.0")

    @Column(length = 10)
    private String abnormalFlag; // H(높음), L(낮음), N(정상), A(비정상)

    @Column(nullable = false, length = 50)
    private String status; // FINAL, PRELIMINARY, CORRECTED, CANCELLED

    @Column(columnDefinition = "TEXT")
    private String notes; // 비고

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private LabResult(LabOrder labOrder, String testItemName, String testItemCode, String resultValue,
                     String unit, String referenceRange, String abnormalFlag, String status, String notes,
                     LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.labOrder = labOrder;
        this.testItemName = testItemName;
        this.testItemCode = testItemCode;
        this.resultValue = resultValue;
        this.unit = unit;
        this.referenceRange = referenceRange;
        this.abnormalFlag = abnormalFlag;
        this.status = status;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static LabResult create(LabOrder labOrder, String testItemName, String testItemCode, String resultValue,
                                  String unit, String referenceRange, String abnormalFlag, String notes) {
        LocalDateTime now = LocalDateTime.now();
        return new LabResult(labOrder, testItemName, testItemCode, resultValue, unit, referenceRange, abnormalFlag, "FINAL", notes, now, now);
    }

    public void update(String resultValue, String unit, String referenceRange, String abnormalFlag, String notes) {
        this.resultValue = resultValue;
        this.unit = unit;
        this.referenceRange = referenceRange;
        this.abnormalFlag = abnormalFlag;
        this.notes = notes;
    }

    public void correct() {
        this.status = "CORRECTED";
    }

    public void cancel() {
        this.status = "CANCELLED";
    }

    public boolean isAbnormal() {
        return "H".equals(abnormalFlag) || "L".equals(abnormalFlag) || "A".equals(abnormalFlag);
    }

    public Long getId() {
        return this.labResultId;
    }
}

