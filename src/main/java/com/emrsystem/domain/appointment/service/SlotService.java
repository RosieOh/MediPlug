package com.emrsystem.domain.appointment.service;

import com.emrsystem.domain.appointment.store.AppointmentStore;
import com.emrsystem.domain.calendar.entity.DoctorWorkingHours;
import com.emrsystem.domain.calendar.repository.DoctorWorkingHoursRepository;
import com.emrsystem.domain.calendar.repository.HolidayRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SlotService {

    private final DoctorWorkingHoursRepository workingHoursRepository;
    private final HolidayRepository holidayRepository;
    private final AppointmentStore appointmentStore;

    public record Slot(LocalDateTime startAt, LocalDateTime endAt) {}

    public List<Slot> generateSlots(Long doctorId, LocalDate date, int minutesPerSlot) {
        var day = date.getDayOfWeek();
        var workingOpt = workingHoursRepository.findByDoctor_IdAndDayOfWeek(doctorId, day);
        if (workingOpt.isEmpty()) return List.of();
        if (holidayRepository.findByDate(date).isPresent()) return List.of();

        DoctorWorkingHours wh = workingOpt.get();
        LocalTime start = wh.getStartTime();
        LocalTime end = wh.getEndTime();

        List<Slot> result = new ArrayList<>();
        LocalDateTime cursor = LocalDateTime.of(date, start);
        LocalDateTime endOfDay = LocalDateTime.of(date, end);
        var existing = appointmentStore.page(org.springframework.data.domain.Pageable.ofSize(Integer.MAX_VALUE)).getContent();

        while (!cursor.plusMinutes(minutesPerSlot).isAfter(endOfDay)) {
            final LocalDateTime startCandidate = cursor;
            final LocalDateTime endCandidate = cursor.plusMinutes(minutesPerSlot);
            boolean overlaps = existing.stream().anyMatch(a -> a.getDoctor().getId().equals(doctorId)
                    && a.getStartAt().isBefore(endCandidate) && a.getEndAt().isAfter(startCandidate));
            if (!overlaps) {
                result.add(new Slot(startCandidate, endCandidate));
            }
            cursor = endCandidate;
        }
        return result;
    }
}


