package com.emrsystem.core.notification;

import com.emrsystem.domain.notification.entity.NotificationJob;
import com.emrsystem.domain.notification.enums.NotificationStatus;
import com.emrsystem.domain.notification.repository.NotificationJobRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final NotificationJobRepository jobRepository;
    private final NotificationService notificationService;

    // 매 분 재시도/대기 작업 처리
    @Scheduled(cron = "0 * * * * *")
    public void processJobs() {
        List<NotificationJob> jobs = jobRepository.findTop50ByStatusInAndNextAttemptAtBeforeOrderByNextAttemptAtAsc(
                List.of(NotificationStatus.PENDING, NotificationStatus.RETRYING), LocalDateTime.now());
        for (NotificationJob job : jobs) {
            try {
                // 실제 전송. 실패 시 예외 발생 가정
                notificationService.sendAppointmentNotification(job.getChannel(), job.getRecipient(), job.getPayload());
                job.markSent();
            } catch (Exception e) {
                if (job.getRetryCount() >= 5) {
                    job.markFailed(e.getMessage());
                } else {
                    job.markRetry(e.getMessage());
                }
            }
            jobRepository.save(job);
        }
    }
}


