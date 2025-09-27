package com.unity.back.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Location {
    private Long id;
    private Long userId;
    private Long deviceId;
    private Double latitude;
    private Double longitude;
    private LocalDateTime timestamp;
}
