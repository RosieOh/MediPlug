package com.emrsystem.domain.notification.facade;

import com.emrsystem.domain.notification.entity.NotificationJob;
import com.emrsystem.domain.notification.entity.NotificationTemplate;
import com.emrsystem.domain.notification.service.NotificationAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationFacade {

    private final NotificationAdminService notificationAdminService;

    // Template operations
    public List<NotificationTemplate> getTemplates() {
        return notificationAdminService.listTemplates();
    }

    public NotificationTemplate createTemplate(String channel, String code, String title, String body) {
        return notificationAdminService.createTemplate(channel, code, title, body);
    }

    public NotificationTemplate updateTemplate(Long templateId, String channel, String title, String body) {
        return notificationAdminService.updateTemplate(templateId, channel, title, body);
    }

    public void deleteTemplate(Long templateId) {
        notificationAdminService.deleteTemplate(templateId);
    }

    // Job operations
    public List<NotificationJob> getJobs() {
        return notificationAdminService.listJobs();
    }

    public NotificationJob enqueueNotification(String channel, String recipient, String templateCode, String payloadJson) {
        return notificationAdminService.enqueue(channel, recipient, templateCode, payloadJson);
    }
}
