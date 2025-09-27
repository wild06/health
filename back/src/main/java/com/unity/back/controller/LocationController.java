package com.unity.back.controller;

import com.unity.back.entity.Location;
import com.unity.back.service.LocationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    public Map<String, Object> upload(@RequestBody Location loc) {
        Long id = locationService.upload(loc);
        Map<String, Object> resp = new HashMap<>();
        resp.put("id", id);
        resp.put("message", "位置已上传");
        return resp;
    }

    @GetMapping("/latest/{deviceId}")
    public Location latest(@PathVariable Long deviceId) {
        return locationService.latest(deviceId);
    }

    @GetMapping("/history")
    public List<Location> history(@RequestParam Long deviceId,
                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return locationService.history(deviceId, date);
    }
}
