package com.emrsystem.domain.notification.repository;

import com.emrsystem.domain.notification.entity.NotificationJob;
import com.emrsystem.domain.notification.enums.NotificationStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationJobRepository extends JpaRepository<NotificationJob, Long> {
    List<NotificationJob> findTop50ByStatusInAndNextAttemptAtBeforeOrderByNextAttemptAtAsc(List<NotificationStatus> statuses, LocalDateTime before);
}


