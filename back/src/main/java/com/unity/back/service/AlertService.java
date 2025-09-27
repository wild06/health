package com.unity.back.service;

import com.unity.back.entity.Alert;
import com.unity.back.mapper.AlertMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {
    private final AlertMapper alertMapper;

    public AlertService(AlertMapper alertMapper) {
        this.alertMapper = alertMapper;
    }

    public List<Alert> listByUser(Long userId) {
        return alertMapper.findByUser(userId);
    }

    public void markRead(Long id) {
        alertMapper.markRead(id);
    }

    public Alert getById(Long id) {
        return alertMapper.findById(id);
    }
}
