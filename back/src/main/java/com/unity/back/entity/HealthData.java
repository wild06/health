package com.unity.back.entity;

import com.unity.back.enums.DataType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HealthData {
    private Long id;
    private Long userId;
    private Long deviceId; // 外键到 devices.id
    private DataType dataType;
    private Double value;
    private String unit;
    private LocalDateTime timestamp;
    private LocalDateTime createdAt;
}
