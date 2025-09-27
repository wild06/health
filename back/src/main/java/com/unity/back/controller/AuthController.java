package com.unity.back.controller;

import com.unity.back.dto.LoginRequest;
import com.unity.back.dto.RegisterRequest;
import com.unity.back.entity.User;
import com.unity.back.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Map<String, Object> register(@Valid @RequestBody RegisterRequest req) {
        Long id = userService.register(req);
        Map<String, Object> resp = new HashMap<>();
        resp.put("userId", id);
        resp.put("message", "注册成功");
        return resp;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@Valid @RequestBody LoginRequest req, HttpSession session) {
        User user = userService.login(req);
        session.setAttribute("userId", user.getId());
        session.setAttribute("role", user.getRole().name());
        Map<String, Object> resp = new HashMap<>();
        resp.put("userId", user.getId());
        resp.put("username", user.getUsername());
        resp.put("role", user.getRole());
        resp.put("message", "登录成功");
        return resp;
    }

    @GetMapping("/logout")
    public Map<String, Object> logout(HttpSession session) {
        session.invalidate();
        Map<String, Object> resp = new HashMap<>();
        resp.put("message", "已退出登录");
        return resp;
    }
}
