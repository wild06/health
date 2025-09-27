package com.unity.back.mapper;

import com.unity.back.entity.HealthData;
import com.unity.back.enums.DataType;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface HealthDataMapper {
    int insert(HealthData data);
    HealthData findLatestByDevice(@Param("deviceId") Long deviceId);
    List<HealthData> findHistory(@Param("deviceId") Long deviceId,
                                 @Param("start") LocalDateTime start,
                                 @Param("end") LocalDateTime end);
    HealthData findLatestByDeviceAndType(@Param("deviceId") Long deviceId,
                                         @Param("dataType") DataType dataType);
}
