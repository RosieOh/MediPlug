package com.emrsystem.domain.common.store;

import com.emrsystem.domain.common.entity.Attachment;
import com.emrsystem.domain.common.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AttachmentStore {

    private final AttachmentRepository attachmentRepository;

    public Attachment save(Attachment attachment) {
        return attachmentRepository.save(attachment);
    }

    public Optional<Attachment> findById(Long id) {
        return attachmentRepository.findById(id);
    }

    public List<Attachment> findByEntityTypeAndEntityId(String entityType, Long entityId) {
        return attachmentRepository.findByEntityTypeAndEntityIdAndActiveTrue(entityType, entityId);
    }

    public List<Attachment> findAllByEntityTypeAndEntityId(String entityType, Long entityId) {
        return attachmentRepository.findByEntityTypeAndEntityId(entityType, entityId);
    }

    public List<Attachment> findByCategory(String category) {
        return attachmentRepository.findByCategoryAndActiveTrue(category);
    }

    public void deleteById(Long id) {
        attachmentRepository.deleteById(id);
    }
}

