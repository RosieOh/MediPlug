package com.emrsystem.domain.patientportal.facade;

import com.emrsystem.domain.appointment.entity.Appointment;
import com.emrsystem.domain.emr.entity.MedicalRecord;
import com.emrsystem.domain.emr.entity.Prescription;
import com.emrsystem.domain.patientportal.response.PatientPortalResponses;
import com.emrsystem.domain.patientportal.service.PatientPortalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PatientPortalFacade {

    private final PatientPortalService patientPortalService;

    public PatientPortalResponses.PatientSummary getPatientSummary(Long patientId) {
        return patientPortalService.getPatientSummary(patientId);
    }

    public Page<MedicalRecord> getMedicalRecords(Long patientId, Pageable pageable) {
        return patientPortalService.getMedicalRecords(patientId, pageable);
    }

    public List<Appointment> getAppointments(Long patientId) {
        return patientPortalService.getAppointments(patientId);
    }

    public List<Prescription> getPrescriptions(Long patientId) {
        return patientPortalService.getPrescriptions(patientId);
    }
}

