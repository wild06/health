package com.unity.back.service;

import com.unity.back.entity.Location;
import com.unity.back.mapper.LocationMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class LocationService {
    private final LocationMapper locationMapper;

    public LocationService(LocationMapper locationMapper) {
        this.locationMapper = locationMapper;
    }

    public Long upload(Location loc) {
        Assert.notNull(loc.getUserId(), "userId 不能为空");
        Assert.notNull(loc.getDeviceId(), "deviceId 不能为空");
        Assert.notNull(loc.getLatitude(), "latitude 不能为空");
        Assert.notNull(loc.getLongitude(), "longitude 不能为空");
        if (loc.getTimestamp() == null) {
            loc.setTimestamp(LocalDateTime.now());
        }
        locationMapper.insert(loc);
        return loc.getId();
    }

    public Location latest(Long deviceId) {
        return locationMapper.findLatestByDevice(deviceId);
    }

    public List<Location> history(Long deviceId, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);
        return locationMapper.findHistory(deviceId, start, end);
    }
}
