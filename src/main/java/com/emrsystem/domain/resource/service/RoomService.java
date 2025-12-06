package com.emrsystem.domain.resource.service;

import com.emrsystem.domain.resource.entity.Room;
import com.emrsystem.domain.resource.store.ResourceStore;
import com.emrsystem.global.common.enums.ErrorCode;
import com.emrsystem.global.common.exception.CommonException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {

    private final ResourceStore resourceStore;

    public List<Room> list() { 
        return resourceStore.findAllRooms(); 
    }

    public Room get(Long roomId) { 
        return resourceStore.findRoomById(roomId)
                .orElseThrow(() -> new CommonException(ErrorCode.ROOM_NOT_FOUND));
    }

    @Transactional
    public Room create(String code, String name) { 
        if (resourceStore.existsRoomByCode(code)) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION, "Room code already exists: " + code);
        }
        return resourceStore.saveRoom(Room.of(code, name)); 
    }

    @Transactional
    public Room update(Long roomId, String code, String name) {
        Room room = resourceStore.findRoomById(roomId)
                .orElseThrow(() -> new CommonException(ErrorCode.ROOM_NOT_FOUND));
        
        if (resourceStore.existsRoomByCode(code) && !room.getCode().equals(code)) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION, "Room code already exists: " + code);
        }
        
        // Update existing room
        room.update(code, name);
        return resourceStore.saveRoom(room);
    }

    @Transactional
    public void delete(Long roomId) { 
        if (!resourceStore.findRoomById(roomId).isPresent()) {
            throw new CommonException(ErrorCode.ROOM_NOT_FOUND);
        }
        resourceStore.deleteRoomById(roomId); 
    }
}


