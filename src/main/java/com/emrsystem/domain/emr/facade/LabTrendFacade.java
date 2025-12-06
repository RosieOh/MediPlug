package com.emrsystem.domain.emr.facade;

import com.emrsystem.domain.emr.response.LabTrendResponses;
import com.emrsystem.domain.emr.service.LabTrendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LabTrendFacade {

    private final LabTrendService labTrendService;

    public LabTrendResponses.LabTrendAnalysis getTrendAnalysis(Long patientId, String testItemName,
                                                               LocalDateTime startDate, LocalDateTime endDate) {
        return labTrendService.getTrendAnalysis(patientId, testItemName, startDate, endDate);
    }

    public List<String> getTestItemNames(Long patientId) {
        return labTrendService.getTestItemNames(patientId);
    }

    public LabTrendResponses.LabResultComparison compareResults(Long patientId, String testItemName, int compareCount) {
        return labTrendService.compareResults(patientId, testItemName, compareCount);
    }
}

