package com.emrsystem.global.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
    List<AccessLog> findByAccessedAtBetween(LocalDateTime start, LocalDateTime end);
}


