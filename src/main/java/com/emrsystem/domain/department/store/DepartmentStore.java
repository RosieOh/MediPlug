package com.emrsystem.domain.department.store;

import com.emrsystem.domain.department.entity.Department;
import com.emrsystem.domain.department.repository.DepartmentRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepartmentStore {

    private final DepartmentRepository departmentRepository;

    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    public Optional<Department> findById(Long id) {
        return departmentRepository.findById(id);
    }

    public Page<Department> page(Pageable pageable) { return departmentRepository.findAll(pageable); }

    public Page<Department> search(Long hospitalId, String name, Pageable pageable) {
        if (hospitalId != null && name != null && !name.isBlank()) {
            return departmentRepository.findByHospital_IdAndNameContainingIgnoreCase(hospitalId, name, pageable);
        }
        if (hospitalId != null) {
            return departmentRepository.findByHospital_Id(hospitalId, pageable);
        }
        if (name != null && !name.isBlank()) {
            return departmentRepository.findByNameContainingIgnoreCase(name, pageable);
        }
        return departmentRepository.findAll(pageable);
    }

    public void deleteById(Long id) {
        departmentRepository.deleteById(id);
    }
}


