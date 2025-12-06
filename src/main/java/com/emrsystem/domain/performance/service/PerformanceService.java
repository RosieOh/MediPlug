package com.emrsystem.domain.performance.service;

import com.emrsystem.domain.appointment.repository.AppointmentRepository;
import com.emrsystem.domain.doctor.entity.Doctor;
import com.emrsystem.domain.doctor.repository.DoctorRepository;
import com.emrsystem.domain.doctor.store.DoctorStore;
import com.emrsystem.domain.emr.repository.MedicalRecordRepository;
import com.emrsystem.domain.performance.response.PerformanceResponses;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PerformanceService {

    private final DoctorStore doctorStore;
    private final DoctorRepository doctorRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final AppointmentRepository appointmentRepository;

    public PerformanceResponses.DoctorPerformance getDoctorPerformance(Long doctorId, LocalDateTime startDate, LocalDateTime endDate) {
        Doctor doctor = doctorStore.findById(doctorId)
                .orElseThrow(() -> new CommonException(ErrorCode.DOCTOR_NOT_FOUND));

        // 진료 건수
        long medicalRecordCount = medicalRecordRepository.findByDoctor_DoctorId(doctorId, 
                org.springframework.data.domain.Pageable.unpaged()).getContent().stream()
                .filter(mr -> mr.getCreatedAt().isAfter(startDate) && mr.getCreatedAt().isBefore(endDate))
                .count();

        // 예약 건수
        long appointmentCount = appointmentRepository.findByDoctor_Id(doctorId, 
                org.springframework.data.domain.Pageable.unpaged()).getContent().stream()
                .filter(a -> a.getStartAt().isAfter(startDate) && a.getStartAt().isBefore(endDate))
                .count();

        // 완료된 예약 건수
        long completedAppointmentCount = appointmentRepository.findByDoctor_Id(doctorId, 
                org.springframework.data.domain.Pageable.unpaged()).getContent().stream()
                .filter(a -> a.getStartAt().isAfter(startDate) && a.getStartAt().isBefore(endDate))
                .filter(a -> "COMPLETED".equals(a.getStatus()))
                .count();

        // 완료율 계산
        double completionRate = appointmentCount > 0
                ? (double) completedAppointmentCount / appointmentCount * 100
                : 0.0;

        return PerformanceResponses.DoctorPerformance.builder()
                .doctorId(doctorId)
                .doctorName(doctor.getName())
                .startDate(startDate.toLocalDate())
                .endDate(endDate.toLocalDate())
                .medicalRecordCount(medicalRecordCount)
                .appointmentCount(appointmentCount)
                .completedAppointmentCount(completedAppointmentCount)
                .completionRate(java.math.BigDecimal.valueOf(completionRate).setScale(2, java.math.RoundingMode.HALF_UP))
                .build();
    }

    public List<PerformanceResponses.DoctorPerformance> getAllDoctorsPerformance(LocalDateTime startDate, LocalDateTime endDate) {
        List<Doctor> doctors = doctorRepository.findAll();
        return doctors.stream()
                .map(doctor -> getDoctorPerformance(doctor.getDoctorId(), startDate, endDate))
                .collect(Collectors.toList());
    }
}

