-- Users table
CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(100) UNIQUE,
  phone VARCHAR(20),
  real_name VARCHAR(50),
  age INT,
  gender ENUM('MALE', 'FEMALE', 'OTHER'),
  height DECIMAL(5,2) COMMENT '身高(cm)',
  weight DECIMAL(5,2) COMMENT '体重(kg)',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
  role ENUM('USER', 'ADMIN') DEFAULT 'USER'
);

-- Devices table
CREATE TABLE IF NOT EXISTS devices (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  device_id VARCHAR(100) UNIQUE NOT NULL COMMENT '手环设备ID',
  device_name VARCHAR(100),
  device_type VARCHAR(50) COMMENT '设备类型',
  mac_address VARCHAR(17),
  connection_status ENUM('CONNECTED', 'DISCONNECTED') DEFAULT 'DISCONNECTED',
  last_connected TIMESTAMP NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Health data table
CREATE TABLE IF NOT EXISTS health_data (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  device_id BIGINT NOT NULL,
  data_type ENUM('HEART_RATE', 'BLOOD_OXYGEN', 'BLOOD_PRESSURE_SYS', 'BLOOD_PRESSURE_DIA', 'STEPS', 'TEMPERATURE', 'CALORIES', 'SLEEP') NOT NULL,
  value DECIMAL(10,2) NOT NULL COMMENT '数据值',
  unit VARCHAR(20) NOT NULL COMMENT '单位',
  timestamp TIMESTAMP NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_user_type_time (user_id, data_type, timestamp),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (device_id) REFERENCES devices(id) ON DELETE CASCADE
);

-- Alert rules table
CREATE TABLE IF NOT EXISTS alert_rules (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  data_type ENUM('HEART_RATE', 'BLOOD_OXYGEN', 'BLOOD_PRESSURE_SYS', 'BLOOD_PRESSURE_DIA', 'STEPS', 'TEMPERATURE') NOT NULL,
  min_value DECIMAL(10,2) COMMENT '最小值阈值',
  max_value DECIMAL(10,2) COMMENT '最大值阈值',
  severity ENUM('LOW', 'MEDIUM', 'HIGH') DEFAULT 'MEDIUM',
  message_template VARCHAR(500) NOT NULL,
  is_active BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Alerts table
CREATE TABLE IF NOT EXISTS alerts (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  device_id BIGINT NOT NULL,
  alert_rule_id BIGINT NOT NULL,
  data_type ENUM('HEART_RATE', 'BLOOD_OXYGEN', 'STEPS', 'TEMPERATURE') NOT NULL,
  measured_value DECIMAL(10,2) NOT NULL,
  threshold_value DECIMAL(10,2) NOT NULL,
  alert_message VARCHAR(500) NOT NULL,
  severity ENUM('LOW', 'MEDIUM', 'HIGH') NOT NULL,
  is_read BOOLEAN DEFAULT FALSE,
  triggered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  read_at TIMESTAMP NULL,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (device_id) REFERENCES devices(id) ON DELETE CASCADE,
  FOREIGN KEY (alert_rule_id) REFERENCES alert_rules(id)
);

-- Default alert rules
INSERT IGNORE INTO alert_rules (data_type, min_value, max_value, severity, message_template)
VALUES
  ('HEART_RATE', 40, 120, 'HIGH', '心率异常：当前值 {value}，正常范围 40-120'),
  ('BLOOD_OXYGEN', 92, NULL, 'HIGH', '血氧饱和度低：当前值 {value}%，低于正常阈值 92%'),
  ('TEMPERATURE', 36, 38, 'MEDIUM', '体温异常：当前值 {value}℃，正常范围 36-38℃'),
  ('BLOOD_PRESSURE_SYS', 90, 140, 'HIGH', '收缩压异常：当前值 {value} mmHg，正常范围 90-140'),
  ('BLOOD_PRESSURE_DIA', 60, 90, 'HIGH', '舒张压异常：当前值 {value} mmHg，正常范围 60-90');

-- Location table
CREATE TABLE IF NOT EXISTS location (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  device_id BIGINT NOT NULL,
  latitude DECIMAL(10,7) NOT NULL,
  longitude DECIMAL(10,7) NOT NULL,
  timestamp TIMESTAMP NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (device_id) REFERENCES devices(id) ON DELETE CASCADE,
  INDEX idx_loc_device_time (device_id, timestamp)
);
