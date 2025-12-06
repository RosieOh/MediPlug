package com.emrsystem.domain.inventory.repository;

import com.emrsystem.domain.inventory.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    List<Supplier> findByStatus(String status);
    Optional<Supplier> findByName(String name);
    boolean existsByName(String name);
}

