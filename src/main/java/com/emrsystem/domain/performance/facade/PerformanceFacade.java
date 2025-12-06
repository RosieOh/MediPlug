package com.emrsystem.domain.performance.facade;

import com.emrsystem.domain.performance.response.PerformanceResponses;
import com.emrsystem.domain.performance.service.PerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PerformanceFacade {

    private final PerformanceService performanceService;

    public PerformanceResponses.DoctorPerformance getDoctorPerformance(Long doctorId, LocalDateTime startDate, LocalDateTime endDate) {
        return performanceService.getDoctorPerformance(doctorId, startDate, endDate);
    }

    public List<PerformanceResponses.DoctorPerformance> getAllDoctorsPerformance(LocalDateTime startDate, LocalDateTime endDate) {
        return performanceService.getAllDoctorsPerformance(startDate, endDate);
    }
}

