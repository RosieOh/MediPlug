package com.emrsystem.domain.hospital.response;

import com.emrsystem.domain.hospital.entity.Hospital;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class HospitalResponses {

    @Getter
    @AllArgsConstructor
    public static class HospitalSummary {
        private Long id;
        private String name;
        private String address;
        private String phone;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static HospitalSummary from(Hospital hospital) {
            return new HospitalSummary(
                    hospital.getId(),
                    hospital.getName(),
                    hospital.getAddress(),
                    hospital.getPhone(),
                    hospital.getCreatedAt(),
                    hospital.getUpdatedAt()
            );
        }
    }
}


