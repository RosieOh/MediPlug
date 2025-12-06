package com.emrsystem.domain.hospital.repository;

import com.emrsystem.domain.hospital.entity.Hospital;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HospitalRepository extends JpaRepository<Hospital, Long>, JpaSpecificationExecutor<Hospital> {
    Page<Hospital> findByNameContainingIgnoreCase(String name, Pageable pageable);
}


