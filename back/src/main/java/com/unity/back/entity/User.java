package com.unity.back.entity;

import com.unity.back.enums.Gender;
import com.unity.back.enums.UserRole;
import com.unity.back.enums.UserStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String realName;
    private Integer age;
    private Gender gender;
    private Double height; // cm
    private Double weight; // kg
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserStatus status;
    private UserRole role;
}
