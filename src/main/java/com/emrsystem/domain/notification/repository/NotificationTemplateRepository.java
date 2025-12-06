package com.emrsystem.domain.notification.repository;

import com.emrsystem.domain.notification.entity.NotificationTemplate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {
    Optional<NotificationTemplate> findByCode(String code);
    boolean existsByCode(String code);
}


