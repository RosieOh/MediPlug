package com.emrsystem.domain.hospital.store;

import com.emrsystem.domain.hospital.entity.Hospital;
import com.emrsystem.domain.hospital.repository.HospitalRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HospitalStore {

    private final HospitalRepository hospitalRepository;

    public Hospital save(Hospital hospital) {
        return hospitalRepository.save(hospital);
    }

    public Optional<Hospital> findById(Long id) {
        return hospitalRepository.findById(id);
    }

    public Page<Hospital> page(Pageable pageable) {
        return hospitalRepository.findAll(pageable);
    }

    public Page<Hospital> searchByName(String name, Pageable pageable) {
        return hospitalRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public void deleteById(Long id) {
        hospitalRepository.deleteById(id);
    }
}


