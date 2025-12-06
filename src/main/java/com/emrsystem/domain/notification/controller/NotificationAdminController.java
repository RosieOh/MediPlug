package com.emrsystem.domain.notification.controller;

import com.emrsystem.domain.notification.entity.NotificationJob;
import com.emrsystem.domain.notification.entity.NotificationTemplate;
import com.emrsystem.domain.notification.facade.NotificationFacade;
import com.emrsystem.domain.notification.request.NotificationRequests;
import com.emrsystem.domain.notification.response.NotificationResponses;
import com.emrsystem.global.common.controller.BaseController;
import com.emrsystem.global.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications/admin")
@RequiredArgsConstructor
public class NotificationAdminController extends BaseController {

    private final NotificationFacade notificationFacade;

    @GetMapping("/templates")
    @Operation(summary = "알림 템플릿 목록")
    public ResponseEntity<ApiResponse<List<NotificationResponses.TemplateSummary>>> templates() {
        List<NotificationResponses.TemplateSummary> summaries = notificationFacade.getTemplates().stream()
                .map(NotificationResponses.TemplateSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @PostMapping("/templates")
    @Operation(summary = "알림 템플릿 생성")
    public ResponseEntity<ApiResponse<NotificationResponses.TemplateSummary>> createTemplate(@RequestBody NotificationRequests.CreateTemplateRequest request) {
        NotificationTemplate template = notificationFacade.createTemplate(request.getChannel(), request.getCode(), request.getTitle(), request.getBody());
        return created(NotificationResponses.TemplateSummary.of(template));
    }

    @PutMapping("/templates/{templateId}")
    @Operation(summary = "알림 템플릿 수정")
    public ResponseEntity<ApiResponse<NotificationResponses.TemplateSummary>> updateTemplate(@PathVariable Long templateId, @RequestBody NotificationRequests.UpdateTemplateRequest request) {
        NotificationTemplate template = notificationFacade.updateTemplate(templateId, request.getChannel(), request.getTitle(), request.getBody());
        return ok(NotificationResponses.TemplateSummary.of(template));
    }

    @DeleteMapping("/templates/{templateId}")
    @Operation(summary = "알림 템플릿 삭제")
    public ResponseEntity<ApiResponse<String>> deleteTemplate(@PathVariable Long templateId) {
        notificationFacade.deleteTemplate(templateId);
        return okMessage("deleted");
    }

    @GetMapping("/jobs")
    @Operation(summary = "알림 잡 목록")
    public ResponseEntity<ApiResponse<List<NotificationResponses.JobSummary>>> jobs() { 
        List<NotificationResponses.JobSummary> summaries = notificationFacade.getJobs().stream()
                .map(NotificationResponses.JobSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @PostMapping("/jobs")
    @Operation(summary = "알림 잡 등록")
    public ResponseEntity<ApiResponse<NotificationResponses.JobSummary>> enqueue(@RequestBody NotificationRequests.EnqueueNotificationRequest request) {
        NotificationJob job = notificationFacade.enqueueNotification(request.getChannel(), request.getRecipient(), request.getTemplateCode(), request.getPayloadJson());
        return created(NotificationResponses.JobSummary.of(job));
    }
}


