package com.emrsystem.domain.patientportal.service;

import com.emrsystem.domain.appointment.entity.Appointment;
import com.emrsystem.domain.appointment.repository.AppointmentRepository;
import com.emrsystem.domain.emr.entity.MedicalRecord;
import com.emrsystem.domain.emr.entity.Prescription;
import com.emrsystem.domain.emr.repository.MedicalRecordRepository;
import com.emrsystem.domain.emr.repository.PrescriptionRepository;
import com.emrsystem.domain.patient.entity.Patient;
import com.emrsystem.domain.patient.store.PatientStore;
import com.emrsystem.domain.patientportal.response.PatientPortalResponses;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PatientPortalService {

    private final PatientStore patientStore;
    private final MedicalRecordRepository medicalRecordRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final AppointmentRepository appointmentRepository;

    public PatientPortalResponses.PatientSummary getPatientSummary(Long patientId) {
        Patient patient = patientStore.findById(patientId)
                .orElseThrow(() -> new CommonException(ErrorCode.PATIENT_NOT_FOUND));

        long medicalRecordCount = medicalRecordRepository.findByPatient_PatientId(patientId, Pageable.unpaged()).getTotalElements();
        long appointmentCount = appointmentRepository.findByPatient_Id(patientId, Pageable.unpaged()).getTotalElements();
        long prescriptionCount = prescriptionRepository.findByPatient_PatientId(patientId, Pageable.unpaged()).getTotalElements();

        return PatientPortalResponses.PatientSummary.builder()
                .patientId(patientId)
                .patientName(patient.getName())
                .medicalRecordCount(medicalRecordCount)
                .appointmentCount(appointmentCount)
                .prescriptionCount(prescriptionCount)
                .build();
    }

    public Page<MedicalRecord> getMedicalRecords(Long patientId, Pageable pageable) {
        return medicalRecordRepository.findByPatient_PatientId(patientId, pageable);
    }

    public List<Appointment> getAppointments(Long patientId) {
        return appointmentRepository.findByPatient_Id(patientId, Pageable.unpaged()).getContent();
    }

    public List<Prescription> getPrescriptions(Long patientId) {
        return prescriptionRepository.findByPatient_PatientId(patientId, Pageable.unpaged()).getContent();
    }
}

