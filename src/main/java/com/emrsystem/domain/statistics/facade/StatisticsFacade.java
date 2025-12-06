package com.emrsystem.domain.statistics.facade;

import com.emrsystem.domain.statistics.response.StatisticsResponses;
import com.emrsystem.domain.statistics.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StatisticsFacade {

    private final StatisticsService statisticsService;

    public StatisticsResponses.DashboardSummary getDashboardSummary() {
        return statisticsService.getDashboardSummary();
    }

    public List<StatisticsResponses.DepartmentStatistics> getDepartmentStatistics(LocalDate startDate, LocalDate endDate) {
        return statisticsService.getDepartmentStatistics(startDate, endDate);
    }

    public StatisticsResponses.RevenueStatistics getRevenueStatistics(LocalDate startDate, LocalDate endDate) {
        return statisticsService.getRevenueStatistics(startDate, endDate);
    }

    public StatisticsResponses.InventoryStatistics getInventoryStatistics() {
        return statisticsService.getInventoryStatistics();
    }

    public List<StatisticsResponses.DailyStatistics> getDailyStatistics(int days) {
        return statisticsService.getDailyStatistics(days);
    }
}

