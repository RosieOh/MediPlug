package com.emrsystem.domain.doctor.response;

import com.emrsystem.domain.doctor.entity.Doctor;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class DoctorResponses {

    @Getter
    @AllArgsConstructor
    public static class DoctorSummary {
        private Long id;
        private Long departmentId;
        private String name;
        private String phone;
        private String licenseNumber;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static DoctorSummary from(Doctor doctor) {
            return new DoctorSummary(
                    doctor.getId(),
                    doctor.getDepartment().getId(),
                    doctor.getName(),
                    doctor.getPhone(),
                    doctor.getLicenseNumber(),
                    doctor.getCreatedAt(),
                    doctor.getUpdatedAt()
            );
        }
    }
}


