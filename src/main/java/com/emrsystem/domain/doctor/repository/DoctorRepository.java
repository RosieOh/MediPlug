package com.emrsystem.domain.doctor.repository;

import com.emrsystem.domain.doctor.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DoctorRepository extends JpaRepository<Doctor, Long>, JpaSpecificationExecutor<Doctor> {
    Page<Doctor> findByDepartment_Id(Long departmentId, Pageable pageable);
    Page<Doctor> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Doctor> findByDepartment_IdAndNameContainingIgnoreCase(Long departmentId, String name, Pageable pageable);
}


