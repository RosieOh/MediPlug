package com.emrsystem.domain.vitalsign.store;

import com.emrsystem.domain.vitalsign.entity.VitalSign;
import com.emrsystem.domain.vitalsign.entity.VitalSignAlert;
import com.emrsystem.domain.vitalsign.repository.VitalSignAlertRepository;
import com.emrsystem.domain.vitalsign.repository.VitalSignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VitalSignStore {

    private final VitalSignRepository vitalSignRepository;
    private final VitalSignAlertRepository vitalSignAlertRepository;

    // VitalSign operations
    public VitalSign saveVitalSign(VitalSign vitalSign) {
        return vitalSignRepository.save(vitalSign);
    }

    public Optional<VitalSign> findVitalSignById(Long id) {
        return vitalSignRepository.findById(id);
    }

    public List<VitalSign> findVitalSignsByPatient(Long patientId) {
        return vitalSignRepository.findByPatient_PatientId(patientId);
    }

    public Page<VitalSign> findVitalSignsByPatient(Long patientId, Pageable pageable) {
        return vitalSignRepository.findByPatient_PatientId(patientId, pageable);
    }

    public List<VitalSign> findVitalSignsByPatientAndDateRange(Long patientId, LocalDateTime start, LocalDateTime end) {
        return vitalSignRepository.findByPatient_PatientIdAndMeasuredAtBetween(patientId, start, end);
    }

    public List<VitalSign> findLatestVitalSignsByPatient(Long patientId) {
        return vitalSignRepository.findByPatient_PatientIdOrderByMeasuredAtDesc(patientId);
    }

    // VitalSignAlert operations
    public VitalSignAlert saveVitalSignAlert(VitalSignAlert alert) {
        return vitalSignAlertRepository.save(alert);
    }

    public List<VitalSignAlert> findVitalSignAlertsByVitalSign(Long vitalSignId) {
        return vitalSignAlertRepository.findByVitalSign_VitalSignId(vitalSignId);
    }

    public List<VitalSignAlert> findUnacknowledgedAlerts() {
        return vitalSignAlertRepository.findByAcknowledgedFalse();
    }

    public List<VitalSignAlert> findUnacknowledgedAlertsBySeverity(String severity) {
        return vitalSignAlertRepository.findBySeverityAndAcknowledgedFalse(severity);
    }

    public Optional<VitalSignAlert> findVitalSignAlertById(Long id) {
        return vitalSignAlertRepository.findById(id);
    }
}

