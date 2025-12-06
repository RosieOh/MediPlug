package com.emrsystem.domain.resource.response;

import com.emrsystem.domain.resource.entity.Device;
import com.emrsystem.domain.resource.entity.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ResourceResponses {

    @Getter
    @NoArgsConstructor
    public static class RoomSummary {
        private Long roomId;
        private String code;
        private String name;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static RoomSummary of(Room room) {
            RoomSummary summary = new RoomSummary();
            summary.roomId = room.getRoomId();
            summary.code = room.getCode();
            summary.name = room.getName();
            summary.createdAt = room.getCreatedAt();
            summary.updatedAt = room.getUpdatedAt();
            return summary;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class DeviceSummary {
        private Long deviceId;
        private String code;
        private String name;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static DeviceSummary of(Device device) {
            DeviceSummary summary = new DeviceSummary();
            summary.deviceId = device.getDeviceId();
            summary.code = device.getCode();
            summary.name = device.getName();
            summary.createdAt = device.getCreatedAt();
            summary.updatedAt = device.getUpdatedAt();
            return summary;
        }
    }
}
