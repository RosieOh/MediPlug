package com.emrsystem.domain.calendar.service;

import com.emrsystem.domain.calendar.entity.DoctorWorkingHours;
import com.emrsystem.domain.calendar.repository.DoctorWorkingHoursRepository;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DoctorWorkingHoursService {

    private final DoctorWorkingHoursRepository repository;

    public Optional<DoctorWorkingHours> get(Long doctorId, DayOfWeek day) {
        return repository.findByDoctor_IdAndDayOfWeek(doctorId, day);
    }

    @Transactional
    public DoctorWorkingHours upsert(DoctorWorkingHours existingOrNull, com.emrsystem.domain.doctor.entity.Doctor doctor,
                                     DayOfWeek day, LocalTime start, LocalTime end) {
        if (existingOrNull != null) {
            // recreate for simplicity; could mutate if setters are present
            repository.delete(existingOrNull);
        }
        return repository.save(DoctorWorkingHours.of(doctor, day, start, end));
    }

    @Transactional
    public void delete(Long doctorId, DayOfWeek day) {
        repository.findByDoctor_IdAndDayOfWeek(doctorId, day).ifPresent(repository::delete);
    }
}


