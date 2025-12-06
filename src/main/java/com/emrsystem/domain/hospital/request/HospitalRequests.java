package com.emrsystem.domain.hospital.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class HospitalRequests {

    @Getter
    @NoArgsConstructor
    public static class CreateHospitalRequest {
        @NotBlank
        @Size(max = 200)
        private String name;

        @NotBlank
        @Size(max = 300)
        private String address;

        @NotBlank
        @Pattern(regexp = "^[0-9\\-]{7,30}$")
        private String phone;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateHospitalRequest {
        @NotBlank
        @Size(max = 200)
        private String name;

        @NotBlank
        @Size(max = 300)
        private String address;

        @NotBlank
        @Pattern(regexp = "^[0-9\\-]{7,30}$")
        private String phone;
    }
}


