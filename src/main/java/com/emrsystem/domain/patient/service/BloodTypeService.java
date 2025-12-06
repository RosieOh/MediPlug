package com.emrsystem.domain.patient.service;

import com.emrsystem.domain.patient.entity.BloodType;
import com.emrsystem.domain.patient.entity.Patient;
import com.emrsystem.domain.patient.request.PatientRequests;
import com.emrsystem.domain.patient.store.BloodTypeStore;
import com.emrsystem.domain.patient.store.PatientStore;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BloodTypeService {

    private final BloodTypeStore bloodTypeStore;
    private final PatientStore patientStore;

    public BloodType getByPatient(Long patientId) {
        return bloodTypeStore.findByPatientId(patientId)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "혈액형 정보를 찾을 수 없습니다."));
    }

    @Transactional
    public BloodType createOrUpdate(Long patientId, PatientRequests.CreateBloodTypeRequest request) {
        Patient patient = patientStore.findById(patientId)
                .orElseThrow(() -> new CommonException(ErrorCode.PATIENT_NOT_FOUND));

        BloodType bloodType = bloodTypeStore.findByPatientId(patientId).orElse(null);

        if (bloodType == null) {
            bloodType = BloodType.create(
                    patient,
                    request.getAboType(),
                    request.getRhType(),
                    request.getTestedAt(),
                    request.getTestedDate(),
                    request.getNotes()
            );
        } else {
            bloodType.update(
                    request.getAboType(),
                    request.getRhType(),
                    request.getTestedAt(),
                    request.getTestedDate(),
                    request.getNotes()
            );
        }

        return bloodTypeStore.save(bloodType);
    }
}

