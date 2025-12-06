package com.emrsystem.domain.resource.repository;

import com.emrsystem.domain.resource.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    boolean existsByCode(String code);
}


