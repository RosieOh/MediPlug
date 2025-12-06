package com.emrsystem.domain.department.repository;

import com.emrsystem.domain.department.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DepartmentRepository extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {
    Page<Department> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Department> findByHospital_Id(Long hospitalId, Pageable pageable);
    Page<Department> findByHospital_IdAndNameContainingIgnoreCase(Long hospitalId, String name, Pageable pageable);
}


