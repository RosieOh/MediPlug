package com.emrsystem.domain.department.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class DepartmentRequests {

    @Getter
    @NoArgsConstructor
    public static class CreateDepartmentRequest {
        @NotNull
        private Long hospitalId;

        @NotBlank
        @Size(max = 150)
        private String name;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateDepartmentRequest {
        @NotBlank
        @Size(max = 150)
        private String name;
    }
}


