package com.emrsystem.domain.appointment.controller;

import com.emrsystem.domain.appointment.service.SlotService;
import com.emrsystem.domain.appointment.service.SlotService.Slot;
import com.emrsystem.global.common.controller.BaseController;
import com.emrsystem.global.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/slots")
@RequiredArgsConstructor
public class SlotController extends BaseController {

    private final SlotService slotService;

    @GetMapping
    @Operation(summary = "가용 슬롯 조회", description = "의사/날짜 기준 가용 슬롯 생성")
    public ResponseEntity<ApiResponse<List<Slot>>> list(@RequestParam Long doctorId,
                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                        @RequestParam(defaultValue = "20") int minutesPerSlot
    ) {
        return ok(slotService.generateSlots(doctorId, date, minutesPerSlot));
    }
}


