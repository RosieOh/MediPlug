package com.emrsystem.domain.vitalsign.facade;

import com.emrsystem.domain.vitalsign.entity.VitalSign;
import com.emrsystem.domain.vitalsign.entity.VitalSignAlert;
import com.emrsystem.domain.vitalsign.request.VitalSignRequests;
import com.emrsystem.domain.vitalsign.service.VitalSignService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VitalSignFacade {

    private final VitalSignService vitalSignService;

    public VitalSign get(Long id) {
        return vitalSignService.get(id);
    }

    public List<VitalSign> getByPatient(Long patientId) {
        return vitalSignService.getByPatient(patientId);
    }

    public Page<VitalSign> getByPatient(Long patientId, Pageable pageable) {
        return vitalSignService.getByPatient(patientId, pageable);
    }

    public List<VitalSign> getByPatientAndDateRange(Long patientId, LocalDateTime start, LocalDateTime end) {
        return vitalSignService.getByPatientAndDateRange(patientId, start, end);
    }

    public List<VitalSign> getLatestByPatient(Long patientId) {
        return vitalSignService.getLatestByPatient(patientId);
    }

    public VitalSign create(VitalSignRequests.CreateVitalSignRequest request) {
        return vitalSignService.create(request);
    }

    public List<VitalSignAlert> getAlertsByVitalSign(Long vitalSignId) {
        return vitalSignService.getAlertsByVitalSign(vitalSignId);
    }

    public List<VitalSignAlert> getUnacknowledgedAlerts() {
        return vitalSignService.getUnacknowledgedAlerts();
    }

    public List<VitalSignAlert> getUnacknowledgedAlertsBySeverity(String severity) {
        return vitalSignService.getUnacknowledgedAlertsBySeverity(severity);
    }

    public VitalSignAlert acknowledgeAlert(Long alertId, String acknowledgedBy) {
        return vitalSignService.acknowledgeAlert(alertId, acknowledgedBy);
    }
}

