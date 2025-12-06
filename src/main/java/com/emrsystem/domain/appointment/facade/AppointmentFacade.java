package com.emrsystem.domain.appointment.facade;

import com.emrsystem.domain.appointment.entity.Appointment;
import com.emrsystem.domain.appointment.request.AppointmentRequests.CreateAppointmentRequest;
import com.emrsystem.domain.appointment.request.AppointmentRequests.UpdateAppointmentRequest;
import com.emrsystem.domain.appointment.request.AppointmentRequests.UpdateAppointmentStatusRequest;
import com.emrsystem.domain.appointment.response.AppointmentResponses.AppointmentSummary;
import com.emrsystem.domain.appointment.service.AppointmentService;
import com.emrsystem.global.common.dto.PageResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppointmentFacade {

    private final AppointmentService appointmentService;

    public PageResponse<AppointmentSummary> page(Pageable pageable) {
        Page<com.emrsystem.domain.appointment.entity.Appointment> page = appointmentService.page(pageable);
        return PageResponse.of(page.map(AppointmentSummary::from));
    }

    public PageResponse<AppointmentSummary> search(Long doctorId, Long patientId, java.time.LocalDateTime start, java.time.LocalDateTime end, Pageable pageable) {
        Page<com.emrsystem.domain.appointment.entity.Appointment> page = appointmentService.search(doctorId, patientId, start, end, pageable);
        return PageResponse.of(page.map(AppointmentSummary::from));
    }

    public AppointmentSummary get(Long id) {
        Appointment appointment = appointmentService.get(id);
        return AppointmentSummary.from(appointment);
    }

    public AppointmentSummary create(CreateAppointmentRequest request) {
        Appointment appointment = appointmentService.create(request);
        return AppointmentSummary.from(appointment);
    }

    public AppointmentSummary update(Long id, UpdateAppointmentRequest request) {
        Appointment appointment = appointmentService.update(id, request);
        return AppointmentSummary.from(appointment);
    }

    public AppointmentSummary updateStatus(Long id, UpdateAppointmentStatusRequest request) {
        Appointment appointment = appointmentService.updateStatus(id, request);
        return AppointmentSummary.from(appointment);
    }

    public void delete(Long id) {
        appointmentService.delete(id);
    }
}


