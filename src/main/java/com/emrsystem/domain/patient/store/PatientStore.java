package com.emrsystem.domain.patient.store;

import com.emrsystem.domain.patient.entity.Patient;
import com.emrsystem.domain.patient.repository.PatientRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PatientStore {

    private final PatientRepository patientRepository;

    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }

    public Page<Patient> page(Pageable pageable) { return patientRepository.findAll(pageable); }

    public Page<Patient> search(String name, String identifier, Pageable pageable) {
        boolean hasName = name != null && !name.isBlank();
        boolean hasId = identifier != null && !identifier.isBlank();
        if (hasName && hasId) return patientRepository.findByNameContainingIgnoreCaseAndIdentifierContainingIgnoreCase(name, identifier, pageable);
        if (hasName) return patientRepository.findByNameContainingIgnoreCase(name, pageable);
        if (hasId) return patientRepository.findByIdentifierContainingIgnoreCase(identifier, pageable);
        return patientRepository.findAll(pageable);
    }

    public void deleteById(Long id) {
        patientRepository.deleteById(id);
    }
}


