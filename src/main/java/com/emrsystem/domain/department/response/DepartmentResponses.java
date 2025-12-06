package com.emrsystem.domain.department.response;

import com.emrsystem.domain.department.entity.Department;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class DepartmentResponses {

    @Getter
    @AllArgsConstructor
    public static class DepartmentSummary {
        private Long id;
        private Long hospitalId;
        private String name;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static DepartmentSummary from(Department department) {
            return new DepartmentSummary(
                    department.getId(),
                    department.getHospital().getId(),
                    department.getName(),
                    department.getCreatedAt(),
                    department.getUpdatedAt()
            );
        }
    }
}


