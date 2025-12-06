package com.emrsystem.domain.notification.service;

import com.emrsystem.domain.notification.entity.NotificationJob;
import com.emrsystem.domain.notification.entity.NotificationTemplate;
import com.emrsystem.domain.notification.store.NotificationStore;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationAdminService {

    private final NotificationStore notificationStore;

    public List<NotificationTemplate> listTemplates() { 
        return notificationStore.findAllTemplates(); 
    }

    @Transactional
    public NotificationTemplate createTemplate(String channel, String code, String title, String body) {
        if (notificationStore.existsTemplateByCode(code)) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION, "Template code already exists: " + code);
        }
        return notificationStore.saveTemplate(NotificationTemplate.of(channel, code, title, body));
    }

    @Transactional
    public NotificationTemplate updateTemplate(Long templateId, String channel, String title, String body) {
        NotificationTemplate template = notificationStore.findTemplateById(templateId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOTIFICATION_TEMPLATE_NOT_FOUND));
        
        // Update existing template
        template.update(channel, title, body);
        return notificationStore.saveTemplate(template);
    }

    @Transactional
    public void deleteTemplate(Long templateId) { 
        if (!notificationStore.findTemplateById(templateId).isPresent()) {
            throw new CommonException(ErrorCode.NOTIFICATION_TEMPLATE_NOT_FOUND);
        }
        notificationStore.deleteTemplateById(templateId); 
    }

    @Transactional
    public NotificationJob enqueue(String channel, String recipient, String templateCode, String payloadJson) {
        // Validate template exists
        notificationStore.findTemplateByCode(templateCode)
                .orElseThrow(() -> new CommonException(ErrorCode.NOTIFICATION_TEMPLATE_NOT_FOUND));
        
        return notificationStore.saveJob(NotificationJob.pending(channel, recipient, templateCode, payloadJson));
    }

    public List<NotificationJob> listJobs() { 
        return notificationStore.findAllJobs(); 
    }
}


