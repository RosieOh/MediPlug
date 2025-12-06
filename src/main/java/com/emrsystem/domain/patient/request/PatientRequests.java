package com.emrsystem.domain.patient.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PatientRequests {

    @Getter
    @NoArgsConstructor
    public static class CreatePatientRequest {
        @NotBlank
        @Size(max = 150)
        private String name;

        @NotNull
        private LocalDate birthDate;

        @NotBlank
        @Size(max = 10)
        private String gender;

        @NotBlank
        @Pattern(regexp = "^[0-9\\-]{7,30}$")
        private String phone;

        @NotBlank
        @Size(max = 300)
        private String address;

        @NotBlank
        @Size(max = 100)
        private String identifier;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdatePatientRequest {
        @NotBlank
        @Size(max = 150)
        private String name;

        @NotNull
        private LocalDate birthDate;

        @NotBlank
        @Size(max = 10)
        private String gender;

        @NotBlank
        @Pattern(regexp = "^[0-9\\-]{7,30}$")
        private String phone;

        @NotBlank
        @Size(max = 300)
        private String address;
    }

    @Getter
    @NoArgsConstructor
    public static class CreateAllergyRequest {
        @NotBlank
        @Size(max = 50)
        private String allergenType; // DRUG, FOOD, ENVIRONMENTAL, OTHER

        @NotBlank
        @Size(max = 200)
        private String allergenName;

        @NotBlank
        @Size(max = 50)
        private String severity; // MILD, MODERATE, SEVERE, LIFE_THREATENING

        private String reaction;

        private LocalDate diagnosedDate;

        private String notes;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateAllergyRequest {
        @NotBlank
        @Size(max = 50)
        private String allergenType;

        @NotBlank
        @Size(max = 200)
        private String allergenName;

        @NotBlank
        @Size(max = 50)
        private String severity;

        private String reaction;

        private LocalDate diagnosedDate;

        private String notes;
    }

    @Getter
    @NoArgsConstructor
    public static class CreateBloodTypeRequest {
        @NotBlank(message = "ABO 혈액형은 필수입니다.")
        @Size(max = 10, message = "ABO 혈액형은 10자를 초과할 수 없습니다.")
        private String aboType; // A, B, AB, O

        @NotBlank(message = "Rh 혈액형은 필수입니다.")
        @Size(max = 10, message = "Rh 혈액형은 10자를 초과할 수 없습니다.")
        private String rhType; // +, -

        @Size(max = 200, message = "검사 기관은 200자를 초과할 수 없습니다.")
        private String testedAt;

        private LocalDate testedDate;

        private String notes;
    }
}


