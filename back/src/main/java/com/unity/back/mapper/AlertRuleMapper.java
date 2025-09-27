package com.unity.back.mapper;

import com.unity.back.entity.AlertRule;
import com.unity.back.enums.DataType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AlertRuleMapper {
    List<AlertRule> findActiveByType(@Param("dataType") DataType dataType);
}
