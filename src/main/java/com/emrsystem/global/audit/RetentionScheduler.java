package com.emrsystem.global.audit;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RetentionScheduler {

    private final AccessLogRepository accessLogRepository;

    @Value("${retention.accessLog.days:1825}")
    private int accessLogRetentionDays; // default 5 years

    // 매일 새벽 3시
    @Scheduled(cron = "0 0 3 * * *")
    public void purgeOldAccessLogs() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(accessLogRetentionDays);
        // Simple delete-all strategy: in real code, use a custom query for cutoff
        accessLogRepository.findAll().stream()
                .filter(l -> l.getAccessedAt().isBefore(cutoff))
                .forEach(accessLogRepository::delete);
    }
}


