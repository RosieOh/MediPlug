package com.emrsystem.domain.transfusion.facade;

import com.emrsystem.domain.transfusion.entity.TransfusionRecord;
import com.emrsystem.domain.transfusion.entity.TransfusionReaction;
import com.emrsystem.domain.transfusion.entity.TransfusionRequest;
import com.emrsystem.domain.transfusion.request.TransfusionRequests;
import com.emrsystem.domain.transfusion.service.TransfusionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TransfusionFacade {

    private final TransfusionService transfusionService;

    public TransfusionRequest getRequest(Long id) {
        return transfusionService.getRequest(id);
    }

    public List<TransfusionRequest> getRequestsByPatient(Long patientId) {
        return transfusionService.getRequestsByPatient(patientId);
    }

    public Page<TransfusionRequest> getRequestsByPatient(Long patientId, Pageable pageable) {
        return transfusionService.getRequestsByPatient(patientId, pageable);
    }

    public List<TransfusionRequest> getRequestsByStatus(String status) {
        return transfusionService.getRequestsByStatus(status);
    }

    public TransfusionRequest createRequest(TransfusionRequests.CreateTransfusionRequest request) {
        return transfusionService.createRequest(request);
    }

    public TransfusionRequest approveRequest(Long id, Long approvingDoctorId) {
        return transfusionService.approveRequest(id, approvingDoctorId);
    }

    public TransfusionRequest rejectRequest(Long id, String rejectionReason) {
        return transfusionService.rejectRequest(id, rejectionReason);
    }

    public TransfusionRecord createRecord(TransfusionRequests.CreateTransfusionRecordRequest request) {
        return transfusionService.createRecord(request);
    }

    public TransfusionRecord completeRecord(Long id) {
        return transfusionService.completeRecord(id);
    }

    public TransfusionReaction addReaction(TransfusionRequests.CreateTransfusionReactionRequest request) {
        return transfusionService.addReaction(request);
    }

    public List<TransfusionReaction> getReactionsByRecord(Long recordId) {
        return transfusionService.getReactionsByRecord(recordId);
    }
}

