package com.unity.back.service;

import com.unity.back.entity.Device;
import com.unity.back.enums.ConnectionStatus;
import com.unity.back.mapper.DeviceMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeviceService {
    private final DeviceMapper deviceMapper;

    public DeviceService(DeviceMapper deviceMapper) {
        this.deviceMapper = deviceMapper;
    }

    public Long add(Device device) {
        Assert.notNull(device.getUserId(), "userId 不能为空");
        Assert.hasText(device.getDeviceId(), "deviceId 不能为空");
        if (deviceMapper.findByDeviceId(device.getDeviceId()) != null) {
            throw new IllegalArgumentException("设备ID已存在");
        }
        if (device.getConnectionStatus() == null) {
            device.setConnectionStatus(ConnectionStatus.DISCONNECTED);
        }
        deviceMapper.insert(device);
        return device.getId();
    }

    public List<Device> listByUser(Long userId) {
        return deviceMapper.findByUserId(userId);
    }

    public void setConnection(Long id, boolean connected) {
        Device d = deviceMapper.findById(id);
        if (d == null) throw new IllegalArgumentException("设备不存在");
        d.setConnectionStatus(connected ? ConnectionStatus.CONNECTED : ConnectionStatus.DISCONNECTED);
        d.setLastConnected(connected ? LocalDateTime.now() : d.getLastConnected());
        deviceMapper.update(d);
    }
}
