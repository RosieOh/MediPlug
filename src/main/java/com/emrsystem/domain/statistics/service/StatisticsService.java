package com.emrsystem.domain.statistics.service;

import com.emrsystem.domain.appointment.repository.AppointmentRepository;
import com.emrsystem.domain.bed.repository.AdmissionRepository;
import com.emrsystem.domain.bed.repository.BedRepository;
import com.emrsystem.domain.billing.repository.BillingRepository;
import com.emrsystem.domain.emr.repository.MedicalRecordRepository;
import com.emrsystem.domain.pharmacy.repository.DrugInventoryRepository;
import com.emrsystem.domain.statistics.response.StatisticsResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatisticsService {

    private final AppointmentRepository appointmentRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final AdmissionRepository admissionRepository;
    private final BedRepository bedRepository;
    private final BillingRepository billingRepository;
    private final DrugInventoryRepository drugInventoryRepository;

    /**
     * 대시보드 통계 조회
     */
    public StatisticsResponses.DashboardSummary getDashboardSummary() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        // 오늘 예약 건수
        long todayAppointments = appointmentRepository.countByStartAtBetween(startOfDay, endOfDay);

        // 오늘 진료 건수
        long todayMedicalRecords = medicalRecordRepository.countByCreatedAtBetween(startOfDay, endOfDay);

        // 현재 입원 환자 수
        long currentInpatients = admissionRepository.countByStatus("ACTIVE");

        // 전체 병상 수
        long totalBeds = bedRepository.count();

        // 사용 중인 병상 수
        long occupiedBeds = bedRepository.countByStatus("OCCUPIED");

        // 병상 가용률 계산
        double bedOccupancyRate = totalBeds > 0 
                ? (double) occupiedBeds / totalBeds * 100 
                : 0.0;

        // 오늘 수납 금액
        BigDecimal todayRevenue = billingRepository.sumTotalAmountByCreatedAtBetween(startOfDay, endOfDay)
                .orElse(BigDecimal.ZERO);

        // 유통기한 임박 약품 수 (30일 이내)
        LocalDate expiryThreshold = today.plusDays(30);
        long expiringDrugs = drugInventoryRepository.countByExpirationDateBetween(today, expiryThreshold);

        return StatisticsResponses.DashboardSummary.builder()
                .todayAppointments(todayAppointments)
                .todayMedicalRecords(todayMedicalRecords)
                .currentInpatients(currentInpatients)
                .totalBeds(totalBeds)
                .occupiedBeds(occupiedBeds)
                .bedOccupancyRate(BigDecimal.valueOf(bedOccupancyRate).setScale(2, RoundingMode.HALF_UP))
                .todayRevenue(todayRevenue)
                .expiringDrugs(expiringDrugs)
                .build();
    }

    /**
     * 과별 진료 통계
     */
    public List<StatisticsResponses.DepartmentStatistics> getDepartmentStatistics(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);

        // 실제로는 Department와 MedicalRecord를 조인해서 통계를 계산해야 함
        // 여기서는 간단한 예시로 구현
        return List.of(); // TODO: 실제 구현 필요
    }

    /**
     * 수익 통계
     */
    public StatisticsResponses.RevenueStatistics getRevenueStatistics(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);

        BigDecimal totalRevenue = billingRepository.sumTotalAmountByCreatedAtBetween(start, end)
                .orElse(BigDecimal.ZERO);

        BigDecimal insuranceAmount = billingRepository.sumInsuranceAmountByCreatedAtBetween(start, end)
                .orElse(BigDecimal.ZERO);

        BigDecimal patientAmount = billingRepository.sumCopayAmountByCreatedAtBetween(start, end)
                .orElse(BigDecimal.ZERO);

        long billingCount = billingRepository.countByCreatedAtBetween(start, end);

        return StatisticsResponses.RevenueStatistics.builder()
                .startDate(startDate)
                .endDate(endDate)
                .totalRevenue(totalRevenue)
                .insuranceAmount(insuranceAmount)
                .patientAmount(patientAmount)
                .billingCount(billingCount)
                .averageRevenue(billingCount > 0 
                        ? totalRevenue.divide(BigDecimal.valueOf(billingCount), 2, RoundingMode.HALF_UP)
                        : BigDecimal.ZERO)
                .build();
    }

    /**
     * 재고 통계
     */
    public StatisticsResponses.InventoryStatistics getInventoryStatistics() {
        long totalDrugs = drugInventoryRepository.count();
        long lowStockDrugs = drugInventoryRepository.countByQuantityLessThan(10); // 재고 10개 미만
        long expiredDrugs = drugInventoryRepository.countByExpirationDateBefore(LocalDate.now());
        
        LocalDate expiryThreshold = LocalDate.now().plusDays(30);
        long expiringSoonDrugs = drugInventoryRepository.countByExpirationDateBetween(LocalDate.now(), expiryThreshold);

        return StatisticsResponses.InventoryStatistics.builder()
                .totalDrugs(totalDrugs)
                .lowStockDrugs(lowStockDrugs)
                .expiredDrugs(expiredDrugs)
                .expiringSoonDrugs(expiringSoonDrugs)
                .build();
    }

    /**
     * 일별 통계 (최근 30일)
     */
    public List<StatisticsResponses.DailyStatistics> getDailyStatistics(int days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);

        Map<LocalDate, StatisticsResponses.DailyStatistics> dailyStatsMap = new HashMap<>();

        // 각 날짜별로 통계 계산
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.atTime(23, 59, 59);

            long appointments = appointmentRepository.countByStartAtBetween(start, end);
            long medicalRecords = medicalRecordRepository.countByCreatedAtBetween(start, end);
            BigDecimal revenue = billingRepository.sumTotalAmountByCreatedAtBetween(start, end)
                    .orElse(BigDecimal.ZERO);

            dailyStatsMap.put(date, StatisticsResponses.DailyStatistics.builder()
                    .date(date)
                    .appointments(appointments)
                    .medicalRecords(medicalRecords)
                    .revenue(revenue)
                    .build());
        }

        return dailyStatsMap.values().stream()
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .toList();
    }
}

