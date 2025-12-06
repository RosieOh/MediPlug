package com.emrsystem.domain.resource.controller;

import com.emrsystem.domain.resource.entity.Device;
import com.emrsystem.domain.resource.entity.Room;
import com.emrsystem.domain.resource.facade.ResourceFacade;
import com.emrsystem.domain.resource.request.ResourceRequests;
import com.emrsystem.domain.resource.response.ResourceResponses;
import com.emrsystem.global.common.controller.BaseController;
import com.emrsystem.global.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/resources")
@RequiredArgsConstructor
public class ResourceController extends BaseController {

    private final ResourceFacade resourceFacade;

    // Rooms
    @GetMapping("/rooms")
    @Operation(summary = "진료실 목록")
    public ResponseEntity<ApiResponse<List<ResourceResponses.RoomSummary>>> rooms() { 
        List<ResourceResponses.RoomSummary> summaries = resourceFacade.getRooms().stream()
                .map(ResourceResponses.RoomSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @PostMapping("/rooms")
    @Operation(summary = "진료실 생성")
    public ResponseEntity<ApiResponse<ResourceResponses.RoomSummary>> createRoom(@RequestBody ResourceRequests.CreateRoomRequest request) {
        Room room = resourceFacade.createRoom(request.getCode(), request.getName());
        return created(ResourceResponses.RoomSummary.of(room));
    }

    @PutMapping("/rooms/{roomId}")
    @Operation(summary = "진료실 수정")
    public ResponseEntity<ApiResponse<ResourceResponses.RoomSummary>> updateRoom(@PathVariable Long roomId, @RequestBody ResourceRequests.UpdateRoomRequest request) {
        Room room = resourceFacade.updateRoom(roomId, request.getCode(), request.getName());
        return ok(ResourceResponses.RoomSummary.of(room));
    }

    @DeleteMapping("/rooms/{roomId}")
    @Operation(summary = "진료실 삭제")
    public ResponseEntity<ApiResponse<String>> deleteRoom(@PathVariable Long roomId) {
        resourceFacade.deleteRoom(roomId);
        return okMessage("deleted");
    }

    // Devices
    @GetMapping("/devices")
    @Operation(summary = "장비 목록")
    public ResponseEntity<ApiResponse<List<ResourceResponses.DeviceSummary>>> devices() { 
        List<ResourceResponses.DeviceSummary> summaries = resourceFacade.getDevices().stream()
                .map(ResourceResponses.DeviceSummary::of)
                .collect(Collectors.toList());
        return ok(summaries);
    }

    @PostMapping("/devices")
    @Operation(summary = "장비 생성")
    public ResponseEntity<ApiResponse<ResourceResponses.DeviceSummary>> createDevice(@RequestBody ResourceRequests.CreateDeviceRequest request) {
        Device device = resourceFacade.createDevice(request.getCode(), request.getName());
        return created(ResourceResponses.DeviceSummary.of(device));
    }

    @PutMapping("/devices/{deviceId}")
    @Operation(summary = "장비 수정")
    public ResponseEntity<ApiResponse<ResourceResponses.DeviceSummary>> updateDevice(@PathVariable Long deviceId, @RequestBody ResourceRequests.UpdateDeviceRequest request) {
        Device device = resourceFacade.updateDevice(deviceId, request.getCode(), request.getName());
        return ok(ResourceResponses.DeviceSummary.of(device));
    }

    @DeleteMapping("/devices/{deviceId}")
    @Operation(summary = "장비 삭제")
    public ResponseEntity<ApiResponse<String>> deleteDevice(@PathVariable Long deviceId) {
        resourceFacade.deleteDevice(deviceId);
        return okMessage("deleted");
    }
}


