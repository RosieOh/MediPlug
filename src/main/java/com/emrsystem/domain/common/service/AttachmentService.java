package com.emrsystem.domain.common.service;

import com.emrsystem.domain.common.entity.Attachment;
import com.emrsystem.domain.common.request.AttachmentRequests;
import com.emrsystem.domain.common.response.AttachmentResponses;
import com.emrsystem.domain.common.store.AttachmentStore;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttachmentService {

    private final AttachmentStore attachmentStore;

    public Attachment get(Long id) {
        return attachmentStore.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "첨부파일을 찾을 수 없습니다."));
    }

    public List<Attachment> getByEntity(String entityType, Long entityId) {
        return attachmentStore.findByEntityTypeAndEntityId(entityType, entityId);
    }

    public List<Attachment> getByCategory(String category) {
        return attachmentStore.findByCategory(category);
    }

    @Transactional
    public Attachment create(AttachmentRequests.CreateAttachmentRequest request) {
        Attachment attachment = Attachment.create(
                request.getEntityType(),
                request.getEntityId(),
                request.getFileName(),
                request.getFilePath(),
                request.getFileSize(),
                request.getMimeType(),
                request.getCategory(),
                request.getDescription()
        );

        return attachmentStore.save(attachment);
    }

    @Transactional
    public Attachment update(Long id, AttachmentRequests.UpdateAttachmentRequest request) {
        Attachment attachment = get(id);
        attachment.update(request.getFileName(), request.getDescription());
        return attachmentStore.save(attachment);
    }

    @Transactional
    public void deactivate(Long id) {
        Attachment attachment = get(id);
        attachment.deactivate();
        attachmentStore.save(attachment);
    }

    @Transactional
    public void activate(Long id) {
        Attachment attachment = get(id);
        attachment.activate();
        attachmentStore.save(attachment);
    }

    @Transactional
    public void delete(Long id) {
        get(id);
        attachmentStore.deleteById(id);
    }
}

