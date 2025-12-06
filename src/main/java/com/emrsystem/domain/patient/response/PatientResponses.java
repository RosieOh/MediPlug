package com.emrsystem.domain.patient.response;

import com.emrsystem.domain.patient.entity.Patient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class PatientResponses {

    @Getter
    @AllArgsConstructor
    public static class PatientSummary {
        private Long id;
        private String name;
        private LocalDate birthDate;
        private String gender;
        private String phone;
        private String address;
        private String identifier;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static PatientSummary from(Patient patient) {
            return new PatientSummary(
                    patient.getId(),
                    patient.getName(),
                    patient.getBirthDate(),
                    patient.getGender(),
                    patient.getPhone(),
                    patient.getAddress(),
                    patient.getIdentifier(),
                    patient.getCreatedAt(),
                    patient.getUpdatedAt()
            );
        }
    }

    @Getter
    @AllArgsConstructor
    public static class AllergySummary {
        private Long id;
        private Long patientId;
        private String allergenType;
        private String allergenName;
        private String severity;
        private String reaction;
        private LocalDate diagnosedDate;
        private String notes;
        private boolean active;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static AllergySummary from(com.emrsystem.domain.patient.entity.PatientAllergy allergy) {
            return new AllergySummary(
                    allergy.getId(),
                    allergy.getPatient().getId(),
                    allergy.getAllergenType(),
                    allergy.getAllergenName(),
                    allergy.getSeverity(),
                    allergy.getReaction(),
                    allergy.getDiagnosedDate(),
                    allergy.getNotes(),
                    allergy.isActive(),
                    allergy.getCreatedAt(),
                    allergy.getUpdatedAt()
            );
        }
    }

    @Getter
    @AllArgsConstructor
    public static class BloodTypeSummary {
        private Long bloodTypeId;
        private Long patientId;
        private String aboType;
        private String rhType;
        private String fullBloodType;
        private String testedAt;
        private LocalDate testedDate;
        private String notes;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static BloodTypeSummary from(com.emrsystem.domain.patient.entity.BloodType bloodType) {
            return new BloodTypeSummary(
                    bloodType.getId(),
                    bloodType.getPatient().getId(),
                    bloodType.getAboType(),
                    bloodType.getRhType(),
                    bloodType.getFullBloodType(),
                    bloodType.getTestedAt(),
                    bloodType.getTestedDate(),
                    bloodType.getNotes(),
                    bloodType.getCreatedAt(),
                    bloodType.getUpdatedAt()
            );
        }
    }
}


