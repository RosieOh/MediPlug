package com.emrsystem.domain.report.facade;

import com.emrsystem.domain.report.request.ReportRequests;
import com.emrsystem.domain.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportFacade {

    private final ReportService reportService;

    public Resource generateReport(ReportRequests.GenerateReportRequest request) {
        return reportService.generateReport(request);
    }
}

