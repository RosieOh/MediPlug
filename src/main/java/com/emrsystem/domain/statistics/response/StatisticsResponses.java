package com.emrsystem.domain.statistics.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class StatisticsResponses {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DashboardSummary {
        private long todayAppointments;
        private long todayMedicalRecords;
        private long currentInpatients;
        private long totalBeds;
        private long occupiedBeds;
        private BigDecimal bedOccupancyRate; // 병상 가용률 (%)
        private BigDecimal todayRevenue;
        private long expiringDrugs; // 유통기한 임박 약품 수
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepartmentStatistics {
        private Long departmentId;
        private String departmentName;
        private long medicalRecordCount;
        private long appointmentCount;
        private BigDecimal revenue;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RevenueStatistics {
        private LocalDate startDate;
        private LocalDate endDate;
        private BigDecimal totalRevenue;
        private BigDecimal insuranceAmount;
        private BigDecimal patientAmount;
        private long billingCount;
        private BigDecimal averageRevenue; // 평균 청구 금액
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InventoryStatistics {
        private long totalDrugs;
        private long lowStockDrugs; // 재고 부족 약품
        private long expiredDrugs; // 만료된 약품
        private long expiringSoonDrugs; // 곧 만료될 약품 (30일 이내)
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyStatistics {
        private LocalDate date;
        private long appointments;
        private long medicalRecords;
        private BigDecimal revenue;
    }
}

