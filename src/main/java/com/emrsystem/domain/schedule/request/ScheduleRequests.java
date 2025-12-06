package com.emrsystem.domain.schedule.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleRequests {

    @Getter
    @NoArgsConstructor
    public static class CreateDoctorScheduleRequest {
        @NotNull(message = "의사 ID는 필수입니다.")
        private Long doctorId;

        @NotNull(message = "근무일은 필수입니다.")
        private LocalDate scheduleDate;

        @NotNull(message = "시작 시간은 필수입니다.")
        private LocalTime startTime;

        @NotNull(message = "종료 시간은 필수입니다.")
        private LocalTime endTime;

        @NotBlank(message = "교대 유형은 필수입니다.")
        @Size(max = 50, message = "교대 유형은 50자를 초과할 수 없습니다.")
        private String shiftType;

        @Size(max = 200, message = "부서는 200자를 초과할 수 없습니다.")
        private String department;

        @Size(max = 200, message = "근무 장소는 200자를 초과할 수 없습니다.")
        private String location;
    }

    @Getter
    @NoArgsConstructor
    public static class CreateStaffScheduleRequest {
        @NotBlank(message = "직원 이름은 필수입니다.")
        @Size(max = 200, message = "직원 이름은 200자를 초과할 수 없습니다.")
        private String staffName;

        @NotBlank(message = "직원 유형은 필수입니다.")
        @Size(max = 50, message = "직원 유형은 50자를 초과할 수 없습니다.")
        private String staffType;

        @NotNull(message = "근무일은 필수입니다.")
        private LocalDate scheduleDate;

        @NotNull(message = "시작 시간은 필수입니다.")
        private LocalTime startTime;

        @NotNull(message = "종료 시간은 필수입니다.")
        private LocalTime endTime;

        @NotBlank(message = "교대 유형은 필수입니다.")
        @Size(max = 50, message = "교대 유형은 50자를 초과할 수 없습니다.")
        private String shiftType;

        @Size(max = 200, message = "부서는 200자를 초과할 수 없습니다.")
        private String department;

        @Size(max = 200, message = "근무 장소는 200자를 초과할 수 없습니다.")
        private String location;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateScheduleRequest {
        @NotNull(message = "시작 시간은 필수입니다.")
        private LocalTime startTime;

        @NotNull(message = "종료 시간은 필수입니다.")
        private LocalTime endTime;

        @NotBlank(message = "교대 유형은 필수입니다.")
        @Size(max = 50, message = "교대 유형은 50자를 초과할 수 없습니다.")
        private String shiftType;

        @Size(max = 200, message = "부서는 200자를 초과할 수 없습니다.")
        private String department;

        @Size(max = 200, message = "근무 장소는 200자를 초과할 수 없습니다.")
        private String location;

        private String notes;
    }

    @Getter
    @NoArgsConstructor
    public static class CreateShiftRequest {
        @NotBlank(message = "교대 유형은 필수입니다.")
        @Size(max = 50, message = "교대 유형은 50자를 초과할 수 없습니다.")
        private String shiftType;

        @NotNull(message = "시작 시간은 필수입니다.")
        private LocalTime startTime;

        @NotNull(message = "종료 시간은 필수입니다.")
        private LocalTime endTime;

        @Size(max = 200, message = "설명은 200자를 초과할 수 없습니다.")
        private String description;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateShiftRequest {
        @NotNull(message = "시작 시간은 필수입니다.")
        private LocalTime startTime;

        @NotNull(message = "종료 시간은 필수입니다.")
        private LocalTime endTime;

        @Size(max = 200, message = "설명은 200자를 초과할 수 없습니다.")
        private String description;
    }
}

