package com.unity.back.controller;

import com.unity.back.entity.Alert;
import com.unity.back.service.AlertService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping("/user/{userId}")
    public List<Alert> list(@PathVariable Long userId) {
        return alertService.listByUser(userId);
    }

    @PutMapping("/{id}/read")
    public Map<String, Object> markRead(@PathVariable Long id) {
        alertService.markRead(id);
        Map<String, Object> resp = new HashMap<>();
        resp.put("message", "已标记为已读");
        return resp;
    }

    @GetMapping("/{id}")
    public Alert get(@PathVariable Long id) {
        return alertService.getById(id);
    }
}
