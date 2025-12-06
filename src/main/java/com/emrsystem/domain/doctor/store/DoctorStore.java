package com.emrsystem.domain.doctor.store;

import com.emrsystem.domain.doctor.entity.Doctor;
import com.emrsystem.domain.doctor.repository.DoctorRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DoctorStore {

    private final DoctorRepository doctorRepository;

    public Doctor save(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public Optional<Doctor> findById(Long id) {
        return doctorRepository.findById(id);
    }

    public Page<Doctor> page(Pageable pageable) { return doctorRepository.findAll(pageable); }

    public Page<Doctor> search(Long departmentId, String name, Pageable pageable) {
        if (departmentId != null && name != null && !name.isBlank()) {
            return doctorRepository.findByDepartment_IdAndNameContainingIgnoreCase(departmentId, name, pageable);
        }
        if (departmentId != null) {
            return doctorRepository.findByDepartment_Id(departmentId, pageable);
        }
        if (name != null && !name.isBlank()) {
            return doctorRepository.findByNameContainingIgnoreCase(name, pageable);
        }
        return doctorRepository.findAll(pageable);
    }

    public void deleteById(Long id) {
        doctorRepository.deleteById(id);
    }
}


