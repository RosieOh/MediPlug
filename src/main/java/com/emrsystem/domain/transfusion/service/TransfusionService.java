package com.emrsystem.domain.transfusion.service;

import com.emrsystem.domain.doctor.entity.Doctor;
import com.emrsystem.domain.doctor.store.DoctorStore;
import com.emrsystem.domain.patient.entity.BloodType;
import com.emrsystem.domain.patient.entity.Patient;
import com.emrsystem.domain.patient.store.BloodTypeStore;
import com.emrsystem.domain.patient.store.PatientStore;
import com.emrsystem.domain.transfusion.entity.TransfusionRecord;
import com.emrsystem.domain.transfusion.entity.TransfusionReaction;
import com.emrsystem.domain.transfusion.entity.TransfusionRequest;
import com.emrsystem.domain.transfusion.request.TransfusionRequests;
import com.emrsystem.domain.transfusion.store.TransfusionStore;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransfusionService {

    private final TransfusionStore transfusionStore;
    private final PatientStore patientStore;
    private final DoctorStore doctorStore;
    private final BloodTypeStore bloodTypeStore;

    public TransfusionRequest getRequest(Long id) {
        return transfusionStore.findTransfusionRequestById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "수혈 요청을 찾을 수 없습니다."));
    }

    public List<TransfusionRequest> getRequestsByPatient(Long patientId) {
        return transfusionStore.findTransfusionRequestsByPatient(patientId);
    }

    public Page<TransfusionRequest> getRequestsByPatient(Long patientId, Pageable pageable) {
        return transfusionStore.findTransfusionRequestsByPatient(patientId, pageable);
    }

    public List<TransfusionRequest> getRequestsByStatus(String status) {
        return transfusionStore.findTransfusionRequestsByStatus(status);
    }

    @Transactional
    public TransfusionRequest createRequest(TransfusionRequests.CreateTransfusionRequest request) {
        Patient patient = patientStore.findById(request.getPatientId())
                .orElseThrow(() -> new CommonException(ErrorCode.PATIENT_NOT_FOUND));

        Doctor requestingDoctor = doctorStore.findById(request.getRequestingDoctorId())
                .orElseThrow(() -> new CommonException(ErrorCode.DOCTOR_NOT_FOUND));

        TransfusionRequest transfusionRequest = TransfusionRequest.create(
                patient,
                requestingDoctor,
                request.getBloodComponent(),
                request.getUnits(),
                request.getReason()
        );

        return transfusionStore.saveTransfusionRequest(transfusionRequest);
    }

    @Transactional
    public TransfusionRequest approveRequest(Long id, Long approvingDoctorId) {
        TransfusionRequest request = getRequest(id);
        Doctor approvingDoctor = doctorStore.findById(approvingDoctorId)
                .orElseThrow(() -> new CommonException(ErrorCode.DOCTOR_NOT_FOUND));

        if (!"PENDING".equals(request.getStatus())) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION,
                    "대기 중인 수혈 요청만 승인할 수 있습니다.");
        }

        request.approve(approvingDoctor);
        return transfusionStore.saveTransfusionRequest(request);
    }

    @Transactional
    public TransfusionRequest rejectRequest(Long id, String rejectionReason) {
        TransfusionRequest request = getRequest(id);
        if (!"PENDING".equals(request.getStatus())) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION,
                    "대기 중인 수혈 요청만 거부할 수 있습니다.");
        }
        request.reject(rejectionReason);
        return transfusionStore.saveTransfusionRequest(request);
    }

    @Transactional
    public TransfusionRecord createRecord(TransfusionRequests.CreateTransfusionRecordRequest request) {
        TransfusionRequest transfusionRequest = getRequest(request.getTransfusionRequestId());

        if (!"APPROVED".equals(transfusionRequest.getStatus())) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION,
                    "승인된 수혈 요청만 기록할 수 있습니다.");
        }

        Patient patient = transfusionRequest.getPatient();
        Doctor administeringDoctor = doctorStore.findById(request.getAdministeringDoctorId())
                .orElseThrow(() -> new CommonException(ErrorCode.DOCTOR_NOT_FOUND));

        // 환자 혈액형 확인
        BloodType patientBloodType = bloodTypeStore.findByPatientId(patient.getId())
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND,
                        "환자의 혈액형 정보가 등록되어 있지 않습니다."));

        // 혈액형 호환성 체크
        if (!isBloodTypeCompatible(patientBloodType.getFullBloodType(), request.getBloodType())) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION,
                    String.format("혈액형이 호환되지 않습니다. 환자: %s, 수혈 혈액: %s",
                            patientBloodType.getFullBloodType(), request.getBloodType()));
        }

        TransfusionRecord record = TransfusionRecord.create(
                transfusionRequest,
                patient,
                administeringDoctor,
                request.getBloodComponent(),
                request.getBloodType(),
                request.getBloodBagNumber(),
                request.getUnits()
        );

        record = transfusionStore.saveTransfusionRecord(record);
        transfusionRequest.complete();
        transfusionStore.saveTransfusionRequest(transfusionRequest);

        return record;
    }

    @Transactional
    public TransfusionRecord completeRecord(Long id) {
        TransfusionRecord record = transfusionStore.findTransfusionRecordById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "수혈 기록을 찾을 수 없습니다."));
        record.complete(LocalDateTime.now());
        return transfusionStore.saveTransfusionRecord(record);
    }

    @Transactional
    public TransfusionReaction addReaction(TransfusionRequests.CreateTransfusionReactionRequest request) {
        TransfusionRecord record = transfusionStore.findTransfusionRecordById(request.getTransfusionRecordId())
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "수혈 기록을 찾을 수 없습니다."));

        TransfusionReaction reaction = TransfusionReaction.create(
                record,
                request.getReactionType(),
                request.getSeverity(),
                request.getSymptoms(),
                request.getTreatment()
        );

        // 심각한 반응이면 수혈 중단
        if ("SEVERE".equals(request.getSeverity()) || "LIFE_THREATENING".equals(request.getSeverity())) {
            record.stop("심각한 수혈 반응 발생");
            transfusionStore.saveTransfusionRecord(record);
        }

        return transfusionStore.saveTransfusionReaction(reaction);
    }

    public List<TransfusionReaction> getReactionsByRecord(Long recordId) {
        return transfusionStore.findTransfusionReactionsByRecord(recordId);
    }

    /**
     * 혈액형 호환성 체크
     */
    private boolean isBloodTypeCompatible(String patientBloodType, String donorBloodType) {
        // 간단한 호환성 체크 (실제로는 더 복잡한 로직 필요)
        // AB+는 모든 혈액형 수혈 가능
        if ("AB+".equals(patientBloodType)) {
            return true;
        }
        // O-는 모든 혈액형에 수혈 가능
        if ("O-".equals(donorBloodType)) {
            return true;
        }
        // 같은 혈액형은 호환
        if (patientBloodType.equals(donorBloodType)) {
            return true;
        }
        // ABO 호환성 체크
        String patientABO = patientBloodType.substring(0, patientBloodType.length() - 1);
        String donorABO = donorBloodType.substring(0, donorBloodType.length() - 1);
        String patientRh = patientBloodType.substring(patientBloodType.length() - 1);
        String donorRh = donorBloodType.substring(donorBloodType.length() - 1);

        // Rh 호환성: Rh- 환자는 Rh+ 혈액 수혈 불가
        if ("-".equals(patientRh) && "+".equals(donorRh)) {
            return false;
        }

        // ABO 호환성: 환자가 받을 수 있는 혈액형
        if ("A".equals(patientABO) && ("A".equals(donorABO) || "O".equals(donorABO))) {
            return true;
        }
        if ("B".equals(patientABO) && ("B".equals(donorABO) || "O".equals(donorABO))) {
            return true;
        }
        if ("AB".equals(patientABO)) {
            return true; // AB는 모든 ABO 수혈 가능
        }
        if ("O".equals(patientABO) && "O".equals(donorABO)) {
            return true;
        }

        return false;
    }
}

