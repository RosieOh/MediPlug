package com.emrsystem.domain.department.service;

import com.emrsystem.domain.department.entity.Department;
import com.emrsystem.domain.department.request.DepartmentRequests.CreateDepartmentRequest;
import com.emrsystem.domain.department.request.DepartmentRequests.UpdateDepartmentRequest;
import com.emrsystem.domain.department.store.DepartmentStore;
import com.emrsystem.domain.hospital.entity.Hospital;
import com.emrsystem.domain.hospital.store.HospitalStore;
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
public class DepartmentService {

    private final DepartmentStore departmentStore;
    private final HospitalStore hospitalStore;

    public Page<Department> page(Pageable pageable) { return departmentStore.page(pageable); }

    public Page<Department> search(Long hospitalId, String name, Pageable pageable) {
        return departmentStore.search(hospitalId, name, pageable);
    }

    public Department get(Long id) {
        return departmentStore.findById(id).orElseThrow(() -> new CommonException(ErrorCode.DEPARTMENT_NOT_FOUND));
    }

    @Transactional
    public Department create(CreateDepartmentRequest request) {
        Hospital hospital = hospitalStore.findById(request.getHospitalId())
                .orElseThrow(() -> new CommonException(ErrorCode.HOSPITAL_NOT_FOUND));
        Department department = Department.create(hospital, request.getName());
        return departmentStore.save(department);
    }

    @Transactional
    public Department update(Long id, UpdateDepartmentRequest request) {
        Department department = get(id);
        department.update(request.getName());
        return departmentStore.save(department);
    }

    @Transactional
    public void delete(Long id) {
        get(id);
        departmentStore.deleteById(id);
    }
}


