package com.unity.back.service;

import com.unity.back.dto.LoginRequest;
import com.unity.back.dto.RegisterRequest;
import com.unity.back.entity.User;
import com.unity.back.enums.UserRole;
import com.unity.back.enums.UserStatus;
import com.unity.back.mapper.UserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Long register(RegisterRequest req) {
        Assert.notNull(req, "Register request is null");
        if (userMapper.findByUsername(req.getUsername()) != null) {
            throw new IllegalArgumentException("用户名已存在");
        }
        User u = new User();
        u.setUsername(req.getUsername());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setEmail(req.getEmail());
        u.setPhone(req.getPhone());
        u.setRealName(req.getRealName());
        u.setStatus(UserStatus.ACTIVE);
        u.setRole(UserRole.USER);
        int rows = userMapper.insertUser(u);
        if (rows != 1) {
            throw new IllegalStateException("注册失败");
        }
        return u.getId();
    }

    public User login(LoginRequest req) {
        User db = userMapper.findByUsername(req.getUsername());
        if (db == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        if (!passwordEncoder.matches(req.getPassword(), db.getPassword())) {
            throw new IllegalArgumentException("密码错误");
        }
        return db;
    }

    public User getById(Long id) {
        return Objects.requireNonNull(userMapper.findById(id), "用户不存在");
    }
}
