package com.emrsystem.domain.doctor.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class DoctorRequests {

    @Getter
    @NoArgsConstructor
    public static class CreateDoctorRequest {
        @NotNull
        private Long departmentId;

        @NotBlank
        @Size(max = 150)
        private String name;

        @NotBlank
        @Pattern(regexp = "^[0-9\\-]{7,30}$")
        private String phone;

        @NotBlank
        @Size(max = 100)
        private String licenseNumber;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateDoctorRequest {
        @NotNull
        private Long departmentId;

        @NotBlank
        @Size(max = 150)
        private String name;

        @NotBlank
        @Pattern(regexp = "^[0-9\\-]{7,30}$")
        private String phone;

        @NotBlank
        @Size(max = 100)
        private String licenseNumber;
    }
}


