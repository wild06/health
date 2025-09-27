package com.unity.back.service;

import com.unity.back.dto.HealthDataRequest;
import com.unity.back.entity.Alert;
import com.unity.back.entity.AlertRule;
import com.unity.back.entity.HealthData;
import com.unity.back.enums.DataType;
import com.unity.back.mapper.AlertMapper;
import com.unity.back.mapper.AlertRuleMapper;
import com.unity.back.mapper.HealthDataMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class HealthService {
    private final HealthDataMapper healthDataMapper;
    private final AlertRuleMapper alertRuleMapper;
    private final AlertMapper alertMapper;

    public HealthService(HealthDataMapper healthDataMapper,
                         AlertRuleMapper alertRuleMapper,
                         AlertMapper alertMapper) {
        this.healthDataMapper = healthDataMapper;
        this.alertRuleMapper = alertRuleMapper;
        this.alertMapper = alertMapper;
    }

    public Long upload(HealthDataRequest req) {
        Assert.notNull(req.getUserId(), "userId 不能为空");
        Assert.notNull(req.getDeviceId(), "deviceId 不能为空");
        Assert.notNull(req.getDataType(), "dataType 不能为空");
        Assert.notNull(req.getValue(), "value 不能为空");
        Assert.notNull(req.getUnit(), "unit 不能为空");

        HealthData data = new HealthData();
        data.setUserId(req.getUserId());
        data.setDeviceId(req.getDeviceId());
        data.setDataType(req.getDataType());
        data.setValue(req.getValue());
        data.setUnit(req.getUnit());
        data.setTimestamp(req.getTimestamp() != null ? req.getTimestamp() : LocalDateTime.now());
        healthDataMapper.insert(data);

        evaluateAndAlert(data);
        return data.getId();
    }

    private void evaluateAndAlert(HealthData data) {
        DataType type = data.getDataType();
        List<AlertRule> rules = alertRuleMapper.findActiveByType(type);
        if (rules == null || rules.isEmpty()) return;
        for (AlertRule rule : rules) {
            boolean violated = false;
            Double threshold = null;
            if (rule.getMinValue() != null && data.getValue() < rule.getMinValue()) {
                violated = true;
                threshold = rule.getMinValue();
            }
            if (rule.getMaxValue() != null && data.getValue() > rule.getMaxValue()) {
                violated = true;
                threshold = rule.getMaxValue();
            }
            if (violated) {
                String msg = rule.getMessageTemplate();
                if (msg != null) {
                    msg = msg.replace("{value}", String.valueOf(data.getValue()));
                }
                Alert alert = new Alert();
                alert.setUserId(data.getUserId());
                alert.setDeviceId(data.getDeviceId());
                alert.setAlertRuleId(rule.getId());
                alert.setDataType(data.getDataType());
                alert.setMeasuredValue(data.getValue());
                alert.setThresholdValue(threshold != null ? threshold : 0);
                alert.setAlertMessage(msg);
                alert.setSeverity(rule.getSeverity());
                alert.setIsRead(false);
                alertMapper.insert(alert);
            }
        }
    }

    public HealthData latest(Long deviceId) {
        return healthDataMapper.findLatestByDevice(deviceId);
    }

    public List<HealthData> history(Long deviceId, LocalDate start, LocalDate end) {
        LocalDateTime s = start.atStartOfDay();
        LocalDateTime e = end.atTime(LocalTime.MAX);
        return healthDataMapper.findHistory(deviceId, s, e);
    }

    public HealthData latestByType(Long deviceId, DataType dataType) {
        Assert.notNull(deviceId, "deviceId 不能为空");
        Assert.notNull(dataType, "dataType 不能为空");
        return healthDataMapper.findLatestByDeviceAndType(deviceId, dataType);
    }
}
