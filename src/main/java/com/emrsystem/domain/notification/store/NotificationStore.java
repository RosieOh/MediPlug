package com.emrsystem.domain.notification.store;

import com.emrsystem.domain.notification.entity.NotificationJob;
import com.emrsystem.domain.notification.entity.NotificationTemplate;
import com.emrsystem.domain.notification.repository.NotificationJobRepository;
import com.emrsystem.domain.notification.repository.NotificationTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NotificationStore {

    private final NotificationTemplateRepository templateRepository;
    private final NotificationJobRepository jobRepository;

    // Template operations
    public NotificationTemplate saveTemplate(NotificationTemplate template) {
        return templateRepository.save(template);
    }

    public Optional<NotificationTemplate> findTemplateById(Long templateId) {
        return templateRepository.findById(templateId);
    }

    public Optional<NotificationTemplate> findTemplateByCode(String code) {
        return templateRepository.findByCode(code);
    }

    public List<NotificationTemplate> findAllTemplates() {
        return templateRepository.findAll();
    }

    public void deleteTemplateById(Long templateId) {
        templateRepository.deleteById(templateId);
    }

    public boolean existsTemplateByCode(String code) {
        return templateRepository.existsByCode(code);
    }

    // Job operations
    public NotificationJob saveJob(NotificationJob job) {
        return jobRepository.save(job);
    }

    public Optional<NotificationJob> findJobById(Long jobId) {
        return jobRepository.findById(jobId);
    }

    public List<NotificationJob> findAllJobs() {
        return jobRepository.findAll();
    }

    public void deleteJobById(Long jobId) {
        jobRepository.deleteById(jobId);
    }
}
