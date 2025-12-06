package com.emrsystem.domain.patient.service;

import com.emrsystem.domain.patient.entity.Patient;
import com.emrsystem.domain.patient.entity.PatientAllergy;
import com.emrsystem.domain.patient.request.PatientRequests.CreateAllergyRequest;
import com.emrsystem.domain.patient.request.PatientRequests.UpdateAllergyRequest;
import com.emrsystem.domain.patient.store.PatientAllergyStore;
import com.emrsystem.domain.patient.store.PatientStore;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PatientAllergyService {

    private final PatientAllergyStore allergyStore;
    private final PatientStore patientStore;

    public List<PatientAllergy> getByPatientId(Long patientId) {
        return allergyStore.findByPatientId(patientId);
    }

    public PatientAllergy get(Long id) {
        return allergyStore.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "알레르기 정보를 찾을 수 없습니다."));
    }

    @Transactional
    public PatientAllergy create(Long patientId, CreateAllergyRequest request) {
        Patient patient = patientStore.findById(patientId)
                .orElseThrow(() -> new CommonException(ErrorCode.PATIENT_NOT_FOUND));

        // 중복 체크
        if (allergyStore.existsByPatientIdAndAllergenName(patientId, request.getAllergenName())) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION, 
                    "이미 등록된 알레르기 정보입니다: " + request.getAllergenName());
        }

        PatientAllergy allergy = PatientAllergy.create(
                patient,
                request.getAllergenType(),
                request.getAllergenName(),
                request.getSeverity(),
                request.getReaction(),
                request.getDiagnosedDate(),
                request.getNotes()
        );

        return allergyStore.save(allergy);
    }

    @Transactional
    public PatientAllergy update(Long id, UpdateAllergyRequest request) {
        PatientAllergy allergy = get(id);
        allergy.update(
                request.getAllergenType(),
                request.getAllergenName(),
                request.getSeverity(),
                request.getReaction(),
                request.getDiagnosedDate(),
                request.getNotes()
        );
        return allergyStore.save(allergy);
    }

    @Transactional
    public void deactivate(Long id) {
        PatientAllergy allergy = get(id);
        allergy.deactivate();
        allergyStore.save(allergy);
    }

    @Transactional
    public void activate(Long id) {
        PatientAllergy allergy = get(id);
        allergy.activate();
        allergyStore.save(allergy);
    }

    @Transactional
    public void delete(Long id) {
        get(id);
        allergyStore.deleteById(id);
    }

    /**
     * 약물명으로 알레르기 체크
     * @param patientId 환자 ID
     * @param drugName 약물명
     * @return 알레르기가 있으면 true
     */
    public boolean hasDrugAllergy(Long patientId, String drugName) {
        List<PatientAllergy> allergies = allergyStore.findByPatientId(patientId);
        return allergies.stream()
                .filter(allergy -> "DRUG".equals(allergy.getAllergenType()))
                .anyMatch(allergy -> allergy.getAllergenName().equalsIgnoreCase(drugName));
    }

    /**
     * 약물명으로 심각한 알레르기 체크
     * @param patientId 환자 ID
     * @param drugName 약물명
     * @return 심각한 알레르기가 있으면 true
     */
    public boolean hasSevereDrugAllergy(Long patientId, String drugName) {
        List<PatientAllergy> allergies = allergyStore.findByPatientId(patientId);
        return allergies.stream()
                .filter(allergy -> "DRUG".equals(allergy.getAllergenType()))
                .filter(allergy -> allergy.getAllergenName().equalsIgnoreCase(drugName))
                .anyMatch(PatientAllergy::isSevere);
    }
}

