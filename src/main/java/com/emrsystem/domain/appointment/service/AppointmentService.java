package com.emrsystem.domain.appointment.service;

import com.emrsystem.domain.appointment.entity.Appointment;
import com.emrsystem.domain.appointment.request.AppointmentRequests.CreateAppointmentRequest;
import com.emrsystem.domain.appointment.request.AppointmentRequests.UpdateAppointmentRequest;
import com.emrsystem.domain.appointment.request.AppointmentRequests.UpdateAppointmentStatusRequest;
import com.emrsystem.domain.appointment.store.AppointmentStore;
import com.emrsystem.domain.doctor.entity.Doctor;
import com.emrsystem.domain.doctor.store.DoctorStore;
import com.emrsystem.domain.patient.entity.Patient;
import com.emrsystem.domain.patient.store.PatientStore;
import com.emrsystem.domain.calendar.repository.DoctorWorkingHoursRepository;
import com.emrsystem.domain.calendar.repository.HolidayRepository;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppointmentService {

    private final AppointmentStore appointmentStore;
    private final PatientStore patientStore;
    private final DoctorStore doctorStore;
    private final DoctorWorkingHoursRepository doctorWorkingHoursRepository;
    private final HolidayRepository holidayRepository;

    public Page<Appointment> page(Pageable pageable) { return appointmentStore.page(pageable); }

    public Page<Appointment> search(Long doctorId, Long patientId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return appointmentStore.search(doctorId, patientId, start, end, pageable);
    }

    public Appointment get(Long id) {
        return appointmentStore.findById(id).orElseThrow(() -> new CommonException(ErrorCode.APPOINTMENT_NOT_FOUND));
    }

    @Transactional
    public Appointment create(CreateAppointmentRequest request) {
        Patient patient = patientStore.findById(request.getPatientId())
                .orElseThrow(() -> new CommonException(ErrorCode.PATIENT_NOT_FOUND));
        Doctor doctor = doctorStore.findById(request.getDoctorId())
                .orElseThrow(() -> new CommonException(ErrorCode.DOCTOR_NOT_FOUND));
        ensureWithinWorkingHoursAndNotHoliday(doctor.getId(), request.getStartAt(), request.getEndAt());
        if (appointmentStore.existsOverlap(doctor.getId(), request.getStartAt(), request.getEndAt())) {
            throw new CommonException(ErrorCode.APPOINTMENT_TIME_OVERLAP);
        }
        Appointment appointment = Appointment.create(patient, doctor, request.getStartAt(), request.getEndAt());
        return appointmentStore.save(appointment);
    }

    @Transactional
    public Appointment update(Long id, UpdateAppointmentRequest request) {
        Appointment appointment = get(id);
        ensureWithinWorkingHoursAndNotHoliday(appointment.getDoctor().getId(), request.getStartAt(), request.getEndAt());
        if (appointmentStore.existsOverlap(appointment.getDoctor().getId(), request.getStartAt(), request.getEndAt())) {
            throw new CommonException(ErrorCode.APPOINTMENT_TIME_OVERLAP);
        }
        appointment.reschedule(request.getStartAt(), request.getEndAt());
        return appointmentStore.save(appointment);
    }

    private void ensureWithinWorkingHoursAndNotHoliday(Long doctorId, LocalDateTime start, LocalDateTime end) {
        var day = start.getDayOfWeek();
        var working = doctorWorkingHoursRepository.findByDoctor_IdAndDayOfWeek(doctorId, day)
                .orElseThrow(() -> new CommonException(ErrorCode.WRONG_ENTRY_POINT));
        boolean inRange = !start.toLocalTime().isBefore(working.getStartTime()) && !end.toLocalTime().isAfter(working.getEndTime());
        if (!inRange) throw new CommonException(ErrorCode.WRONG_ENTRY_POINT);
        if (holidayRepository.findByDate(start.toLocalDate()).isPresent()) {
            throw new CommonException(ErrorCode.WRONG_ENTRY_POINT);
        }
    }

    @Transactional
    public Appointment updateStatus(Long id, UpdateAppointmentStatusRequest request) {
        Appointment appointment = get(id);
        appointment.updateStatus(request.getStatus());
        return appointmentStore.save(appointment);
    }

    @Transactional
    public void delete(Long id) {
        get(id);
        appointmentStore.deleteById(id);
    }
}


