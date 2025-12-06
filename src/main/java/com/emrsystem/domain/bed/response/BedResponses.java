package com.emrsystem.domain.bed.response;

import com.emrsystem.domain.bed.entity.Admission;
import com.emrsystem.domain.bed.entity.Bed;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class BedResponses {

    @Getter
    @NoArgsConstructor
    public static class BedSummary {
        private Long bedId;
        private Long roomId;
        private String roomCode;
        private String roomName;
        private String bedNumber;
        private String bedType;
        private String status;
        private String description;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static BedSummary of(Bed bed) {
            BedSummary summary = new BedSummary();
            summary.bedId = bed.getBedId();
            summary.roomId = bed.getRoom().getRoomId();
            summary.roomCode = bed.getRoom().getCode();
            summary.roomName = bed.getRoom().getName();
            summary.bedNumber = bed.getBedNumber();
            summary.bedType = bed.getBedType();
            summary.status = bed.getStatus();
            summary.description = bed.getDescription();
            summary.createdAt = bed.getCreatedAt();
            summary.updatedAt = bed.getUpdatedAt();
            return summary;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class AdmissionSummary {
        private Long admissionId;
        private Long patientId;
        private String patientName;
        private Long bedId;
        private String bedNumber;
        private String roomCode;
        private String roomName;
        private LocalDateTime admissionDate;
        private LocalDateTime dischargeDate;
        private String status;
        private String reason;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static AdmissionSummary of(Admission admission) {
            AdmissionSummary summary = new AdmissionSummary();
            summary.admissionId = admission.getAdmissionId();
            summary.patientId = admission.getPatient().getPatientId();
            summary.patientName = admission.getPatient().getName();
            summary.bedId = admission.getBed().getBedId();
            summary.bedNumber = admission.getBed().getBedNumber();
            summary.roomCode = admission.getBed().getRoom().getCode();
            summary.roomName = admission.getBed().getRoom().getName();
            summary.admissionDate = admission.getAdmissionDate();
            summary.dischargeDate = admission.getDischargeDate();
            summary.status = admission.getStatus();
            summary.reason = admission.getReason();
            summary.createdAt = admission.getCreatedAt();
            summary.updatedAt = admission.getUpdatedAt();
            return summary;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class BedAvailabilitySummary {
        private Long bedId;
        private String bedNumber;
        private String bedType;
        private String roomCode;
        private String roomName;
        private String status;
        private LocalDateTime lastOccupiedAt;
        private LocalDateTime estimatedAvailableAt;

        public BedAvailabilitySummary(Long bedId, String bedNumber, String bedType, 
                String roomCode, String roomName, String status, 
                LocalDateTime lastOccupiedAt, LocalDateTime estimatedAvailableAt) {
            this.bedId = bedId;
            this.bedNumber = bedNumber;
            this.bedType = bedType;
            this.roomCode = roomCode;
            this.roomName = roomName;
            this.status = status;
            this.lastOccupiedAt = lastOccupiedAt;
            this.estimatedAvailableAt = estimatedAvailableAt;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class BedOccupancySummary {
        private Long roomId;
        private String roomCode;
        private String roomName;
        private int totalBeds;
        private int occupiedBeds;
        private int availableBeds;
        private int maintenanceBeds;
        private double occupancyRate;

        public BedOccupancySummary(Long roomId, String roomCode, String roomName, 
                int totalBeds, int occupiedBeds, int availableBeds, int maintenanceBeds) {
            this.roomId = roomId;
            this.roomCode = roomCode;
            this.roomName = roomName;
            this.totalBeds = totalBeds;
            this.occupiedBeds = occupiedBeds;
            this.availableBeds = availableBeds;
            this.maintenanceBeds = maintenanceBeds;
            this.occupancyRate = totalBeds > 0 ? (double) occupiedBeds / totalBeds * 100 : 0;
        }
    }
}
