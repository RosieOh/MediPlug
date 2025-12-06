package com.emrsystem.domain.emr.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class LabTrendResponses {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrendDataPoint {
        private LocalDate date;
        private Double value;
        private String unit;
        private String abnormalFlag;
        private String referenceRange;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrendStatistics {
        private int count;
        private Double min;
        private Double max;
        private BigDecimal average;
        private String referenceRange;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChangeAlert {
        private LocalDate date;
        private Double previousValue;
        private Double currentValue;
        private BigDecimal changePercent;
        private String severity; // WARNING, CRITICAL
        private String message;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LabTrendAnalysis {
        private Long patientId;
        private String patientName;
        private String testItemName;
        private LocalDate startDate;
        private LocalDate endDate;
        private List<TrendDataPoint> trendData;
        private TrendStatistics statistics;
        private List<ChangeAlert> changeAlerts;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComparisonItem {
        private LocalDate date;
        private Double value;
        private String unit;
        private String abnormalFlag;
        private String referenceRange;
        private boolean isLatest;
        private BigDecimal changePercent;
        private String changeDirection; // INCREASE, DECREASE, STABLE
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LabResultComparison {
        private Long patientId;
        private String patientName;
        private String testItemName;
        private List<ComparisonItem> comparisons;
    }
}

