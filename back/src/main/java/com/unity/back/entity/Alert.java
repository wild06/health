package com.unity.back.entity;

import com.unity.back.enums.DataType;
import com.unity.back.enums.Severity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Alert {
    private Long id;
    private Long userId;
    private Long deviceId;
    private Long alertRuleId;
    private DataType dataType;
    private Double measuredValue;
    private Double thresholdValue;
    private String alertMessage;
    private Severity severity;
    private Boolean isRead;
    private LocalDateTime triggeredAt;
    private LocalDateTime readAt;
}
