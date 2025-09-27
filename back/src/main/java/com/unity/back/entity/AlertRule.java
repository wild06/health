package com.unity.back.entity;

import com.unity.back.enums.DataType;
import com.unity.back.enums.Severity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AlertRule {
    private Long id;
    private DataType dataType;
    private Double minValue; // 可为空
    private Double maxValue; // 可为空
    private Severity severity;
    private String messageTemplate;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
