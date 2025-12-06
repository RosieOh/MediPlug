package com.emrsystem.domain.surgery.facade;

import com.emrsystem.domain.surgery.entity.PreOpChecklist;
import com.emrsystem.domain.surgery.entity.Surgery;
import com.emrsystem.domain.surgery.entity.SurgeryTeam;
import com.emrsystem.domain.surgery.request.SurgeryRequests;
import com.emrsystem.domain.surgery.service.SurgeryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SurgeryFacade {

    private final SurgeryService surgeryService;

    public Surgery get(Long id) {
        return surgeryService.get(id);
    }

    public List<Surgery> getByPatient(Long patientId) {
        return surgeryService.getByPatient(patientId);
    }

    public Page<Surgery> getByPatient(Long patientId, Pageable pageable) {
        return surgeryService.getByPatient(patientId, pageable);
    }

    public List<Surgery> getBySurgeon(Long surgeonId) {
        return surgeryService.getBySurgeon(surgeonId);
    }

    public List<Surgery> getByDateRange(LocalDateTime start, LocalDateTime end) {
        return surgeryService.getByDateRange(start, end);
    }

    public List<Surgery> getByStatus(String status) {
        return surgeryService.getByStatus(status);
    }

    public Surgery create(SurgeryRequests.CreateSurgeryRequest request) {
        return surgeryService.create(request);
    }

    public Surgery update(Long id, SurgeryRequests.UpdateSurgeryRequest request) {
        return surgeryService.update(id, request);
    }

    public Surgery start(Long id) {
        return surgeryService.start(id);
    }

    public Surgery complete(Long id, SurgeryRequests.CompleteSurgeryRequest request) {
        return surgeryService.complete(id, request);
    }

    public Surgery cancel(Long id, String reason) {
        return surgeryService.cancel(id, reason);
    }

    public Surgery postpone(Long id, LocalDateTime newDateTime, String reason) {
        return surgeryService.postpone(id, newDateTime, reason);
    }

    public Surgery addComplication(Long id, SurgeryRequests.AddComplicationRequest request) {
        return surgeryService.addComplication(id, request);
    }

    public List<SurgeryTeam> getTeamMembers(Long surgeryId) {
        return surgeryService.getTeamMembers(surgeryId);
    }

    public SurgeryTeam addTeamMember(Long surgeryId, SurgeryRequests.TeamMemberRequest request) {
        return surgeryService.addTeamMember(surgeryId, request);
    }

    public void removeTeamMember(Long teamId) {
        surgeryService.removeTeamMember(teamId);
    }

    public List<PreOpChecklist> getChecklist(Long surgeryId) {
        return surgeryService.getChecklist(surgeryId);
    }

    public PreOpChecklist addChecklistItem(Long surgeryId, String item) {
        return surgeryService.addChecklistItem(surgeryId, item);
    }

    public PreOpChecklist checkItem(Long checklistId, String checkedBy) {
        return surgeryService.checkItem(checklistId, checkedBy);
    }

    public void removeChecklistItem(Long checklistId) {
        surgeryService.removeChecklistItem(checklistId);
    }
}

