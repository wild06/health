package com.unity.back.dto;

import com.unity.back.enums.DataType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HealthDataRequest {
    @NotNull
    private Long userId;
    @NotNull
    private Long deviceId; // devices.id
    @NotNull
    private DataType dataType;
    @NotNull
    private Double value;
    @NotNull
    private String unit;

    private LocalDateTime timestamp; // 若为空则使用服务器时间
}
