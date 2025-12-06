package com.emrsystem.domain.doctor.facade;

import com.emrsystem.domain.doctor.entity.Doctor;
import com.emrsystem.domain.doctor.request.DoctorRequests.CreateDoctorRequest;
import com.emrsystem.domain.doctor.request.DoctorRequests.UpdateDoctorRequest;
import com.emrsystem.domain.doctor.response.DoctorResponses.DoctorSummary;
import com.emrsystem.domain.doctor.service.DoctorService;
import com.emrsystem.global.common.dto.PageResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DoctorFacade {

    private final DoctorService doctorService;

    public PageResponse<DoctorSummary> page(Pageable pageable) {
        Page<com.emrsystem.domain.doctor.entity.Doctor> page = doctorService.page(pageable);
        return PageResponse.of(page.map(DoctorSummary::from));
    }

    public PageResponse<DoctorSummary> search(Long departmentId, String name, Pageable pageable) {
        Page<com.emrsystem.domain.doctor.entity.Doctor> page = doctorService.search(departmentId, name, pageable);
        return PageResponse.of(page.map(DoctorSummary::from));
    }

    public DoctorSummary get(Long id) {
        Doctor doctor = doctorService.get(id);
        return DoctorSummary.from(doctor);
    }

    public DoctorSummary create(CreateDoctorRequest request) {
        Doctor doctor = doctorService.create(request);
        return DoctorSummary.from(doctor);
    }

    public DoctorSummary update(Long id, UpdateDoctorRequest request) {
        Doctor doctor = doctorService.update(id, request);
        return DoctorSummary.from(doctor);
    }

    public void delete(Long id) {
        doctorService.delete(id);
    }
}


