package com.unity.back.mapper;

import com.unity.back.entity.Location;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface LocationMapper {
    int insert(Location loc);
    Location findLatestByDevice(@Param("deviceId") Long deviceId);
    List<Location> findHistory(@Param("deviceId") Long deviceId,
                               @Param("start") LocalDateTime start,
                               @Param("end") LocalDateTime end);
}
