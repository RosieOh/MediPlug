package com.emrsystem.global.audit;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessLogAnomalyService {

    private final AccessLogRepository accessLogRepository;

    @Value("${app.anomaly.access-log-threshold:10}")
    private int accessLogThreshold;

    @Value("${app.anomaly.access-log-window-minutes:60}")
    private int accessLogWindowMinutes;

    @Value("${app.anomaly.access-log-start-hour:22}")
    private int offHoursStart;

    @Value("${app.anomaly.access-log-end-hour:6}")
    private int offHoursEnd;

    // 매시간 야간 과다 조회 탐지 (예: 22:00~06:00, 1시간 내 10회 이상)
    @Scheduled(cron = "0 0 * * * *")
    public void detectNightBurstAccess() {
        LocalDateTime now = LocalDateTime.now();
        LocalTime nightStart = LocalTime.of(offHoursStart, 0);
        LocalTime nightEnd = LocalTime.of(offHoursEnd, 0);
        
        if (isOffHours(now.toLocalTime(), nightStart, nightEnd)) {
            LocalDateTime windowStart = now.minusMinutes(accessLogWindowMinutes);
            List<AccessLog> logs = accessLogRepository.findByAccessedAtBetween(windowStart, now);
            
            // 사용자별 접근 횟수 집계
            Map<String, Long> userAccessCounts = logs.stream()
                    .collect(Collectors.groupingBy(AccessLog::getUsername, Collectors.counting()));
            
            // 임계값 초과 사용자 탐지
            userAccessCounts.forEach((username, count) -> {
                if (count > accessLogThreshold) {
                    log.warn("SECURITY ALERT: User '{}' accessed sensitive resources {} times during off-hours ({} - {})", 
                            username, count, offHoursStart, offHoursEnd);
                }
            });
        }
    }

    private boolean isOffHours(LocalTime time, LocalTime start, LocalTime end) {
        if (start.isBefore(end)) { // e.g., 09:00 - 17:00
            return time.isBefore(start) || time.isAfter(end);
        } else { // e.g., 22:00 - 06:00 (spans midnight)
            return time.isAfter(start) || time.isBefore(end);
        }
    }
}


