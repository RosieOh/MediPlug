package com.emrsystem.domain.resource.service;

import com.emrsystem.domain.resource.entity.Device;
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
public class DeviceService {

    private final ResourceStore resourceStore;

    public List<Device> list() { 
        return resourceStore.findAllDevices(); 
    }

    public Device get(Long deviceId) { 
        return resourceStore.findDeviceById(deviceId)
                .orElseThrow(() -> new CommonException(ErrorCode.DEVICE_NOT_FOUND));
    }

    @Transactional
    public Device create(String code, String name) { 
        if (resourceStore.existsDeviceByCode(code)) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION, "Device code already exists: " + code);
        }
        return resourceStore.saveDevice(Device.of(code, name)); 
    }

    @Transactional
    public Device update(Long deviceId, String code, String name) {
        Device device = resourceStore.findDeviceById(deviceId)
                .orElseThrow(() -> new CommonException(ErrorCode.DEVICE_NOT_FOUND));
        
        if (resourceStore.existsDeviceByCode(code) && !device.getCode().equals(code)) {
            throw new CommonException(ErrorCode.DATA_INTEGRITY_VIOLATION, "Device code already exists: " + code);
        }
        
        // Update existing device
        device.update(code, name);
        return resourceStore.saveDevice(device);
    }

    @Transactional
    public void delete(Long deviceId) { 
        if (!resourceStore.findDeviceById(deviceId).isPresent()) {
            throw new CommonException(ErrorCode.DEVICE_NOT_FOUND);
        }
        resourceStore.deleteDeviceById(deviceId); 
    }
}


