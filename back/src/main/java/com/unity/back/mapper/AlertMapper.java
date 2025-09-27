package com.unity.back.mapper;

import com.unity.back.entity.Alert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AlertMapper {
    int insert(Alert alert);
    List<Alert> findByUser(@Param("userId") Long userId);
    int markRead(@Param("id") Long id);
    Alert findById(@Param("id") Long id);
}
