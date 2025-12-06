package com.emrsystem.domain.transfusion.store;

import com.emrsystem.domain.transfusion.entity.TransfusionRecord;
import com.emrsystem.domain.transfusion.entity.TransfusionReaction;
import com.emrsystem.domain.transfusion.entity.TransfusionRequest;
import com.emrsystem.domain.transfusion.repository.TransfusionRecordRepository;
import com.emrsystem.domain.transfusion.repository.TransfusionReactionRepository;
import com.emrsystem.domain.transfusion.repository.TransfusionRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TransfusionStore {

    private final TransfusionRequestRepository transfusionRequestRepository;
    private final TransfusionRecordRepository transfusionRecordRepository;
    private final TransfusionReactionRepository transfusionReactionRepository;

    // TransfusionRequest operations
    public TransfusionRequest saveTransfusionRequest(TransfusionRequest request) {
        return transfusionRequestRepository.save(request);
    }

    public Optional<TransfusionRequest> findTransfusionRequestById(Long id) {
        return transfusionRequestRepository.findById(id);
    }

    public List<TransfusionRequest> findTransfusionRequestsByPatient(Long patientId) {
        return transfusionRequestRepository.findByPatient_PatientId(patientId);
    }

    public Page<TransfusionRequest> findTransfusionRequestsByPatient(Long patientId, Pageable pageable) {
        return transfusionRequestRepository.findByPatient_PatientId(patientId, pageable);
    }

    public List<TransfusionRequest> findTransfusionRequestsByStatus(String status) {
        return transfusionRequestRepository.findByStatus(status);
    }

    // TransfusionRecord operations
    public TransfusionRecord saveTransfusionRecord(TransfusionRecord record) {
        return transfusionRecordRepository.save(record);
    }

    public Optional<TransfusionRecord> findTransfusionRecordById(Long id) {
        return transfusionRecordRepository.findById(id);
    }

    public List<TransfusionRecord> findTransfusionRecordsByPatient(Long patientId) {
        return transfusionRecordRepository.findByPatient_PatientId(patientId);
    }

    public List<TransfusionRecord> findTransfusionRecordsByRequest(Long requestId) {
        return transfusionRecordRepository.findByTransfusionRequest_TransfusionRequestId(requestId);
    }

    // TransfusionReaction operations
    public TransfusionReaction saveTransfusionReaction(TransfusionReaction reaction) {
        return transfusionReactionRepository.save(reaction);
    }

    public List<TransfusionReaction> findTransfusionReactionsByRecord(Long recordId) {
        return transfusionReactionRepository.findByTransfusionRecord_TransfusionRecordId(recordId);
    }
}

