package com.emrsystem.domain.appointment.store;

import com.emrsystem.domain.appointment.entity.Appointment;
import com.emrsystem.domain.appointment.repository.AppointmentRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentStore {

    private final AppointmentRepository appointmentRepository;

    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public Optional<Appointment> findById(Long id) {
        return appointmentRepository.findById(id);
    }

    public Page<Appointment> page(Pageable pageable) { return appointmentRepository.findAll(pageable); }

    public Page<Appointment> search(Long doctorId, Long patientId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        if (doctorId != null) return appointmentRepository.findByDoctor_Id(doctorId, pageable);
        if (patientId != null) return appointmentRepository.findByPatient_Id(patientId, pageable);
        if (start != null && end != null) return appointmentRepository.findByStartAtBetween(start, end, pageable);
        return appointmentRepository.findAll(pageable);
    }

    public boolean existsOverlap(Long doctorId, LocalDateTime startAt, LocalDateTime endAt) {
        return appointmentRepository.existsOverlapping(doctorId, startAt, endAt);
    }

    public void deleteById(Long id) {
        appointmentRepository.deleteById(id);
    }
}


