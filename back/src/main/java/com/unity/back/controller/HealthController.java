package com.unity.back.controller;

import com.unity.back.dto.HealthDataRequest;
import com.unity.back.entity.HealthData;
import com.unity.back.enums.DataType;
import com.unity.back.service.HealthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    private final HealthService healthService;

    public HealthController(HealthService healthService) {
        this.healthService = healthService;
    }

    @PostMapping("/data")
    public Map<String, Object> upload(@Valid @RequestBody HealthDataRequest req) {
        Long id = healthService.upload(req);
        Map<String, Object> resp = new HashMap<>();
        resp.put("id", id);
        resp.put("message", "数据已上传");
        return resp;
    }

    @GetMapping("/latest/{deviceId}")
    public HealthData latest(@PathVariable Long deviceId) {
        return healthService.latest(deviceId);
    }

    @GetMapping("/history")
    public List<HealthData> history(@RequestParam Long deviceId,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return healthService.history(deviceId, start, end);
    }

    @GetMapping("/latest/{deviceId}/type/{dataType}")
    public HealthData latestByType(@PathVariable Long deviceId, @PathVariable DataType dataType) {
        return healthService.latestByType(deviceId, dataType);
    }
}
