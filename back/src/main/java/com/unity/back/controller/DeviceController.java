package com.unity.back.controller;

import com.unity.back.entity.Device;
import com.unity.back.service.DeviceService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping
    public Map<String, Object> add(@Valid @RequestBody Device device) {
        Long id = deviceService.add(device);
        Map<String, Object> resp = new HashMap<>();
        resp.put("deviceId", id);
        resp.put("message", "设备已添加");
        return resp;
    }

    @GetMapping("/user/{userId}")
    public List<Device> list(@PathVariable Long userId) {
        return deviceService.listByUser(userId);
    }

    @PutMapping("/{id}/connect")
    public Map<String, Object> connect(@PathVariable Long id) {
        deviceService.setConnection(id, true);
        return Map.of("message", "设备已连接");
    }

    @PutMapping("/{id}/disconnect")
    public Map<String, Object> disconnect(@PathVariable Long id) {
        deviceService.setConnection(id, false);
        return Map.of("message", "设备已断开");
    }
}
