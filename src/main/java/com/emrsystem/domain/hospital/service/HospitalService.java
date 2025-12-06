package com.emrsystem.domain.hospital.service;

import com.emrsystem.domain.hospital.entity.Hospital;
import com.emrsystem.domain.hospital.request.HospitalRequests.CreateHospitalRequest;
import com.emrsystem.domain.hospital.request.HospitalRequests.UpdateHospitalRequest;
import com.emrsystem.domain.hospital.store.HospitalStore;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HospitalService {

    private final HospitalStore hospitalStore;

    public Page<Hospital> page(Pageable pageable) {
        return hospitalStore.page(pageable);
    }

    public Page<Hospital> searchByName(String name, Pageable pageable) {
        return hospitalStore.searchByName(name, pageable);
    }

    public Hospital get(Long id) {
        return hospitalStore.findById(id).orElseThrow(() -> new CommonException(ErrorCode.HOSPITAL_NOT_FOUND));
    }

    @Transactional
    public Hospital create(CreateHospitalRequest request) {
        Hospital hospital = Hospital.create(request.getName(), request.getAddress(), request.getPhone());
        return hospitalStore.save(hospital);
    }

    @Transactional
    public Hospital update(Long id, UpdateHospitalRequest request) {
        Hospital hospital = get(id);
        hospital.update(request.getName(), request.getAddress(), request.getPhone());
        return hospitalStore.save(hospital);
    }

    @Transactional
    public void delete(Long id) {
        get(id);
        hospitalStore.deleteById(id);
    }
}


