package com.unity.back.mapper;

import com.unity.back.entity.Device;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceMapper {
    int insert(Device device);
    int update(Device device);
    Device findById(@Param("id") Long id);
    Device findByDeviceId(@Param("deviceId") String deviceId);
    List<Device> findByUserId(@Param("userId") Long userId);
}
