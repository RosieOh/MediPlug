package com.emrsystem.domain.vitalsign.service;

import com.emrsystem.domain.patient.entity.Patient;
import com.emrsystem.domain.patient.store.PatientStore;
import com.emrsystem.domain.vitalsign.entity.VitalSign;
import com.emrsystem.domain.vitalsign.entity.VitalSignAlert;
import com.emrsystem.domain.vitalsign.request.VitalSignRequests;
import com.emrsystem.domain.vitalsign.store.VitalSignStore;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VitalSignService {

    private final VitalSignStore vitalSignStore;
    private final PatientStore patientStore;

    public VitalSign get(Long id) {
        return vitalSignStore.findVitalSignById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "생체 신호를 찾을 수 없습니다."));
    }

    public List<VitalSign> getByPatient(Long patientId) {
        return vitalSignStore.findVitalSignsByPatient(patientId);
    }

    public Page<VitalSign> getByPatient(Long patientId, Pageable pageable) {
        return vitalSignStore.findVitalSignsByPatient(patientId, pageable);
    }

    public List<VitalSign> getByPatientAndDateRange(Long patientId, LocalDateTime start, LocalDateTime end) {
        return vitalSignStore.findVitalSignsByPatientAndDateRange(patientId, start, end);
    }

    public List<VitalSign> getLatestByPatient(Long patientId) {
        List<VitalSign> signs = vitalSignStore.findLatestVitalSignsByPatient(patientId);
        return signs.isEmpty() ? new ArrayList<>() : List.of(signs.get(0));
    }

    @Transactional
    public VitalSign create(VitalSignRequests.CreateVitalSignRequest request) {
        Patient patient = patientStore.findById(request.getPatientId())
                .orElseThrow(() -> new CommonException(ErrorCode.PATIENT_NOT_FOUND));

        VitalSign vitalSign = VitalSign.create(
                patient,
                request.getMeasuredAt() != null ? request.getMeasuredAt() : LocalDateTime.now(),
                request.getSystolicBP(),
                request.getDiastolicBP(),
                request.getHeartRate(),
                request.getTemperature(),
                request.getRespiratoryRate(),
                request.getOxygenSaturation(),
                request.getBloodSugar(),
                request.getPainScore(),
                request.getMeasuredBy(),
                request.getDevice(),
                request.getNotes()
        );

        vitalSign = vitalSignStore.saveVitalSign(vitalSign);

        // 이상치 체크 및 알림 생성
        checkAndCreateAlerts(vitalSign);

        return vitalSign;
    }

    /**
     * 생체 신호 이상치 체크 및 알림 생성
     */
    private void checkAndCreateAlerts(VitalSign vitalSign) {
        List<VitalSignAlert> alerts = new ArrayList<>();

        // 혈압 체크
        if (vitalSign.getSystolicBP() != null) {
            if (vitalSign.getSystolicBP().compareTo(new BigDecimal("180")) > 0) {
                alerts.add(VitalSignAlert.create(vitalSign, "HIGH_BP", "CRITICAL",
                        String.format("고혈압 경고: 수축기 혈압 %s mmHg", vitalSign.getSystolicBP())));
            } else if (vitalSign.getSystolicBP().compareTo(new BigDecimal("90")) < 0) {
                alerts.add(VitalSignAlert.create(vitalSign, "LOW_BP", "CRITICAL",
                        String.format("저혈압 경고: 수축기 혈압 %s mmHg", vitalSign.getSystolicBP())));
            }
        }

        // 체온 체크
        if (vitalSign.getTemperature() != null) {
            if (vitalSign.getTemperature().compareTo(new BigDecimal("38.0")) > 0) {
                alerts.add(VitalSignAlert.create(vitalSign, "HIGH_TEMP", "WARNING",
                        String.format("발열 경고: 체온 %s°C", vitalSign.getTemperature())));
            } else if (vitalSign.getTemperature().compareTo(new BigDecimal("35.0")) < 0) {
                alerts.add(VitalSignAlert.create(vitalSign, "LOW_TEMP", "CRITICAL",
                        String.format("저체온 경고: 체온 %s°C", vitalSign.getTemperature())));
            }
        }

        // 맥박 체크
        if (vitalSign.getHeartRate() != null) {
            if (vitalSign.getHeartRate().compareTo(new BigDecimal("100")) > 0) {
                alerts.add(VitalSignAlert.create(vitalSign, "HIGH_HR", "WARNING",
                        String.format("빈맥 경고: 맥박 %s bpm", vitalSign.getHeartRate())));
            } else if (vitalSign.getHeartRate().compareTo(new BigDecimal("60")) < 0) {
                alerts.add(VitalSignAlert.create(vitalSign, "LOW_HR", "WARNING",
                        String.format("서맥 경고: 맥박 %s bpm", vitalSign.getHeartRate())));
            }
        }

        // 산소포화도 체크
        if (vitalSign.getOxygenSaturation() != null) {
            if (vitalSign.getOxygenSaturation().compareTo(new BigDecimal("95")) < 0) {
                alerts.add(VitalSignAlert.create(vitalSign, "LOW_SPO2", "CRITICAL",
                        String.format("저산소 경고: 산소포화도 %s%%", vitalSign.getOxygenSaturation())));
            }
        }

        // 알림 저장
        for (VitalSignAlert alert : alerts) {
            vitalSignStore.saveVitalSignAlert(alert);
        }
    }

    // VitalSignAlert operations
    public List<VitalSignAlert> getAlertsByVitalSign(Long vitalSignId) {
        return vitalSignStore.findVitalSignAlertsByVitalSign(vitalSignId);
    }

    public List<VitalSignAlert> getUnacknowledgedAlerts() {
        return vitalSignStore.findUnacknowledgedAlerts();
    }

    public List<VitalSignAlert> getUnacknowledgedAlertsBySeverity(String severity) {
        return vitalSignStore.findUnacknowledgedAlertsBySeverity(severity);
    }

    @Transactional
    public VitalSignAlert acknowledgeAlert(Long alertId, String acknowledgedBy) {
        VitalSignAlert alert = vitalSignStore.findVitalSignAlertById(alertId)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND, "알림을 찾을 수 없습니다."));
        alert.acknowledge(acknowledgedBy);
        return vitalSignStore.saveVitalSignAlert(alert);
    }
}

