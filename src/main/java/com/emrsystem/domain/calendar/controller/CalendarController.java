package com.emrsystem.domain.calendar.controller;

import com.emrsystem.domain.calendar.entity.DoctorWorkingHours;
import com.emrsystem.domain.calendar.repository.HolidayRepository;
import com.emrsystem.domain.calendar.service.DoctorWorkingHoursService;
import com.emrsystem.domain.doctor.store.DoctorStore;
import com.emrsystem.global.common.controller.BaseController;
import com.emrsystem.global.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController extends BaseController {

    private final DoctorWorkingHoursService workingHoursService;
    private final DoctorStore doctorStore;
    private final HolidayRepository holidayRepository;

    public record WorkingHoursRequest(DayOfWeek day, @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime start,
                                      @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime end) {}

    @GetMapping("/doctors/{doctorId}/working-hours")
    @Operation(summary = "의사 근무시간 조회")
    public ResponseEntity<ApiResponse<DoctorWorkingHours>> getWorkingHours(@PathVariable Long doctorId,
                                                                           @RequestParam DayOfWeek day) {
        return ok(workingHoursService.get(doctorId, day).orElse(null));
    }

    @PostMapping("/doctors/{doctorId}/working-hours")
    @Operation(summary = "의사 근무시간 등록/수정")
    public ResponseEntity<ApiResponse<DoctorWorkingHours>> upsertWorkingHours(@PathVariable Long doctorId,
                                                                              @RequestBody WorkingHoursRequest req) {
        var doctor = doctorStore.findById(doctorId).orElseThrow();
        var existing = workingHoursService.get(doctorId, req.day()).orElse(null);
        var saved = workingHoursService.upsert(existing, doctor, req.day(), req.start(), req.end());
        return ok(saved);
    }

    @DeleteMapping("/doctors/{doctorId}/working-hours")
    @Operation(summary = "의사 근무시간 삭제")
    public ResponseEntity<ApiResponse<String>> deleteWorkingHours(@PathVariable Long doctorId,
                                                                  @RequestParam DayOfWeek day) {
        workingHoursService.delete(doctorId, day);
        return okMessage("deleted");
    }

    public record HolidayRequest(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, String name) {}

    @GetMapping("/holidays")
    @Operation(summary = "공휴일 목록 조회")
    public ResponseEntity<ApiResponse<java.util.List<com.emrsystem.domain.calendar.entity.Holiday>>> listHolidays() {
        return ok(holidayRepository.findAll());
    }

    @PostMapping("/holidays")
    @Operation(summary = "공휴일 등록")
    public ResponseEntity<ApiResponse<com.emrsystem.domain.calendar.entity.Holiday>> createHoliday(@RequestBody HolidayRequest req) {
        var saved = holidayRepository.save(com.emrsystem.domain.calendar.entity.Holiday.of(req.date(), req.name()));
        return ok(saved);
    }

    @DeleteMapping("/holidays/{holidayId}")
    @Operation(summary = "공휴일 삭제")
    public ResponseEntity<ApiResponse<String>> deleteHoliday(@PathVariable Long holidayId) {
        holidayRepository.deleteById(holidayId);
        return okMessage("deleted");
    }
}


