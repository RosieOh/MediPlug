package com.emrsystem.domain.resource.store;

import com.emrsystem.domain.resource.entity.Device;
import com.emrsystem.domain.resource.entity.Room;
import com.emrsystem.domain.resource.repository.DeviceRepository;
import com.emrsystem.domain.resource.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ResourceStore {

    private final RoomRepository roomRepository;
    private final DeviceRepository deviceRepository;

    // Room operations
    public Room saveRoom(Room room) {
        return roomRepository.save(room);
    }

    public Optional<Room> findRoomById(Long roomId) {
        return roomRepository.findById(roomId);
    }

    public List<Room> findAllRooms() {
        return roomRepository.findAll();
    }

    public void deleteRoomById(Long roomId) {
        roomRepository.deleteById(roomId);
    }

    public boolean existsRoomByCode(String code) {
        return roomRepository.existsByCode(code);
    }

    // Device operations
    public Device saveDevice(Device device) {
        return deviceRepository.save(device);
    }

    public Optional<Device> findDeviceById(Long deviceId) {
        return deviceRepository.findById(deviceId);
    }

    public List<Device> findAllDevices() {
        return deviceRepository.findAll();
    }

    public void deleteDeviceById(Long deviceId) {
        deviceRepository.deleteById(deviceId);
    }

    public boolean existsDeviceByCode(String code) {
        return deviceRepository.existsByCode(code);
    }

    public Optional<Room> findRoomByCode(String code) {
        return roomRepository.findByCode(code);
    }
}
