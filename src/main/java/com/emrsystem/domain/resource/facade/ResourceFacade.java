package com.emrsystem.domain.resource.facade;

import com.emrsystem.domain.resource.entity.Device;
import com.emrsystem.domain.resource.entity.Room;
import com.emrsystem.domain.resource.service.DeviceService;
import com.emrsystem.domain.resource.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ResourceFacade {

    private final RoomService roomService;
    private final DeviceService deviceService;

    // Room operations
    public List<Room> getRooms() {
        return roomService.list();
    }

    public Room getRoom(Long roomId) {
        return roomService.get(roomId);
    }

    public Room createRoom(String code, String name) {
        return roomService.create(code, name);
    }

    public Room updateRoom(Long roomId, String code, String name) {
        return roomService.update(roomId, code, name);
    }

    public void deleteRoom(Long roomId) {
        roomService.delete(roomId);
    }

    // Device operations
    public List<Device> getDevices() {
        return deviceService.list();
    }

    public Device getDevice(Long deviceId) {
        return deviceService.get(deviceId);
    }

    public Device createDevice(String code, String name) {
        return deviceService.create(code, name);
    }

    public Device updateDevice(Long deviceId, String code, String name) {
        return deviceService.update(deviceId, code, name);
    }

    public void deleteDevice(Long deviceId) {
        deviceService.delete(deviceId);
    }
}
