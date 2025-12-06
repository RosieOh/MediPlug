package com.emrsystem.domain.doctor.service;

import com.emrsystem.domain.department.entity.Department;
import com.emrsystem.domain.department.store.DepartmentStore;
import com.emrsystem.domain.doctor.entity.Doctor;
import com.emrsystem.domain.doctor.request.DoctorRequests.CreateDoctorRequest;
import com.emrsystem.domain.doctor.request.DoctorRequests.UpdateDoctorRequest;
import com.emrsystem.domain.doctor.store.DoctorStore;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DoctorService {

    private final DoctorStore doctorStore;
    private final DepartmentStore departmentStore;

    public Page<Doctor> page(Pageable pageable) { return doctorStore.page(pageable); }

    public Page<Doctor> search(Long departmentId, String name, Pageable pageable) {
        return doctorStore.search(departmentId, name, pageable);
    }

    public Doctor get(Long id) {
        return doctorStore.findById(id).orElseThrow(() -> new CommonException(ErrorCode.DOCTOR_NOT_FOUND));
    }

    @Transactional
    public Doctor create(CreateDoctorRequest request) {
        Department department = departmentStore.findById(request.getDepartmentId())
                .orElseThrow(() -> new CommonException(ErrorCode.DEPARTMENT_NOT_FOUND));
        Doctor doctor = Doctor.create(department, request.getName(), request.getPhone(), request.getLicenseNumber());
        return doctorStore.save(doctor);
    }

    @Transactional
    public Doctor update(Long id, UpdateDoctorRequest request) {
        Doctor doctor = get(id);
        Department department = departmentStore.findById(request.getDepartmentId())
                .orElseThrow(() -> new CommonException(ErrorCode.DEPARTMENT_NOT_FOUND));
        doctor.update(department, request.getName(), request.getPhone(), request.getLicenseNumber());
        return doctorStore.save(doctor);
    }

    @Transactional
    public void delete(Long id) {
        get(id);
        doctorStore.deleteById(id);
    }
}


