package com.emrsystem.domain.resource.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ResourceRequests {

    @Getter
    @NoArgsConstructor
    public static class CreateRoomRequest {
        @NotBlank(message = "진료실 코드는 필수입니다.")
        @Size(max = 20, message = "진료실 코드는 20자를 초과할 수 없습니다.")
        private String code;

        @NotBlank(message = "진료실 이름은 필수입니다.")
        @Size(max = 100, message = "진료실 이름은 100자를 초과할 수 없습니다.")
        private String name;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateRoomRequest {
        @NotBlank(message = "진료실 코드는 필수입니다.")
        @Size(max = 20, message = "진료실 코드는 20자를 초과할 수 없습니다.")
        private String code;

        @NotBlank(message = "진료실 이름은 필수입니다.")
        @Size(max = 100, message = "진료실 이름은 100자를 초과할 수 없습니다.")
        private String name;
    }

    @Getter
    @NoArgsConstructor
    public static class CreateDeviceRequest {
        @NotBlank(message = "장비 코드는 필수입니다.")
        @Size(max = 20, message = "장비 코드는 20자를 초과할 수 없습니다.")
        private String code;

        @NotBlank(message = "장비 이름은 필수입니다.")
        @Size(max = 100, message = "장비 이름은 100자를 초과할 수 없습니다.")
        private String name;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateDeviceRequest {
        @NotBlank(message = "장비 코드는 필수입니다.")
        @Size(max = 20, message = "장비 코드는 20자를 초과할 수 없습니다.")
        private String code;

        @NotBlank(message = "장비 이름은 필수입니다.")
        @Size(max = 100, message = "장비 이름은 100자를 초과할 수 없습니다.")
        private String name;
    }
}
