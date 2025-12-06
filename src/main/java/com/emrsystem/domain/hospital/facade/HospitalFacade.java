package com.emrsystem.domain.hospital.facade;

import com.emrsystem.domain.hospital.entity.Hospital;
import com.emrsystem.domain.hospital.request.HospitalRequests.CreateHospitalRequest;
import com.emrsystem.domain.hospital.request.HospitalRequests.UpdateHospitalRequest;
import com.emrsystem.domain.hospital.response.HospitalResponses.HospitalSummary;
import com.emrsystem.domain.hospital.service.HospitalService;
import com.emrsystem.global.common.dto.PageResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HospitalFacade {

    private final HospitalService hospitalService;

    public PageResponse<HospitalSummary> page(Pageable pageable) {
        Page<Hospital> page = hospitalService.page(pageable);
        return PageResponse.of(page.map(HospitalSummary::from));
    }

    public PageResponse<HospitalSummary> search(String name, Pageable pageable) {
        Page<Hospital> page = hospitalService.searchByName(name, pageable);
        return PageResponse.of(page.map(HospitalSummary::from));
    }

    public HospitalSummary get(Long id) {
        Hospital hospital = hospitalService.get(id);
        return HospitalSummary.from(hospital);
    }

    public HospitalSummary create(CreateHospitalRequest request) {
        Hospital hospital = hospitalService.create(request);
        return HospitalSummary.from(hospital);
    }

    public HospitalSummary update(Long id, UpdateHospitalRequest request) {
        Hospital hospital = hospitalService.update(id, request);
        return HospitalSummary.from(hospital);
    }

    public void delete(Long id) {
        hospitalService.delete(id);
    }
}


