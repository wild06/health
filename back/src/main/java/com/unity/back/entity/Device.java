package com.unity.back.entity;

import com.unity.back.enums.ConnectionStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Device {
    private Long id;
    private Long userId;
    private String deviceId; // 设备唯一ID
    private String deviceName;
    private String deviceType;
    private String macAddress;
    private ConnectionStatus connectionStatus;
    private LocalDateTime lastConnected;
    private LocalDateTime createdAt;
}
