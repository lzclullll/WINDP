DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS role_permissions;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS permissions;
DROP TABLE IF EXISTS stations;
DROP TABLE IF EXISTS devices;
DROP TABLE IF EXISTS power_readings;
DROP TABLE IF EXISTS prediction_tasks;
DROP TABLE IF EXISTS predictions;
DROP TABLE IF EXISTS prediction_models;
DROP TABLE IF EXISTS work_orders;
DROP TABLE IF EXISTS alarms;
DROP TABLE IF EXISTS alarm_rules;
DROP TABLE IF EXISTS reports;
DROP TABLE IF EXISTS operation_logs;
DROP TABLE IF EXISTS login_logs;

CREATE TABLE users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(64) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  real_name VARCHAR(64),
  email VARCHAR(128),
  phone VARCHAR(32),
  status VARCHAR(20) DEFAULT 'ENABLED',
  station_id BIGINT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0
);
CREATE TABLE roles (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  role_code VARCHAR(64) NOT NULL UNIQUE,
  role_name VARCHAR(64) NOT NULL,
  description VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0
);
CREATE TABLE permissions (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  permission_code VARCHAR(128) NOT NULL UNIQUE,
  permission_name VARCHAR(128) NOT NULL,
  permission_type VARCHAR(32),
  path VARCHAR(255),
  parent_id BIGINT,
  sort_order INT DEFAULT 0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0
);
CREATE TABLE user_roles (user_id BIGINT NOT NULL, role_id BIGINT NOT NULL, PRIMARY KEY(user_id, role_id));
CREATE TABLE role_permissions (role_id BIGINT NOT NULL, permission_id BIGINT NOT NULL, PRIMARY KEY(role_id, permission_id));
CREATE TABLE stations (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(128) NOT NULL,
  code VARCHAR(64) NOT NULL UNIQUE,
  type VARCHAR(20) NOT NULL,
  capacity DECIMAL(12,2),
  location VARCHAR(255),
  latitude DECIMAL(10,6),
  longitude DECIMAL(10,6),
  grid_connection_date DATE,
  status VARCHAR(32),
  manager_id BIGINT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0
);
CREATE TABLE devices (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  station_id BIGINT NOT NULL,
  device_name VARCHAR(128) NOT NULL,
  device_code VARCHAR(64) NOT NULL UNIQUE,
  device_type VARCHAR(64),
  model VARCHAR(64),
  manufacturer VARCHAR(128),
  rated_power DECIMAL(12,2),
  install_date DATE,
  health_status VARCHAR(32),
  running_status VARCHAR(32),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0
);
CREATE TABLE power_readings (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  station_id BIGINT NOT NULL,
  record_time TIMESTAMP NOT NULL,
  power_kw DECIMAL(12,2),
  wind_speed DECIMAL(8,2),
  irradiance DECIMAL(8,2),
  temperature DECIMAL(8,2),
  humidity DECIMAL(8,2),
  device_status VARCHAR(32),
  data_quality VARCHAR(32),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0
);
CREATE INDEX idx_power_station_time ON power_readings(station_id, record_time);
CREATE TABLE prediction_tasks (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  station_id BIGINT NOT NULL,
  model_type VARCHAR(64),
  horizon INT,
  confidence_level INT,
  start_time TIMESTAMP,
  end_time TIMESTAMP,
  status VARCHAR(32),
  error_message VARCHAR(512),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0
);
CREATE TABLE predictions (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  station_id BIGINT NOT NULL,
  task_id BIGINT,
  model_type VARCHAR(64),
  prediction_time TIMESTAMP,
  horizon INT,
  metrics TEXT,
  result_data TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0
);
CREATE TABLE prediction_models (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  model_name VARCHAR(128) NOT NULL,
  model_type VARCHAR(64) NOT NULL,
  version VARCHAR(32),
  status VARCHAR(32),
  description VARCHAR(512),
  metrics TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0
);
CREATE TABLE work_orders (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(128) NOT NULL,
  description TEXT,
  station_id BIGINT,
  device_id BIGINT,
  type VARCHAR(64),
  priority VARCHAR(32),
  status VARCHAR(32),
  assignee_id BIGINT,
  creator_id BIGINT,
  completed_at TIMESTAMP,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0
);
CREATE TABLE alarms (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  station_id BIGINT,
  device_id BIGINT,
  alarm_type VARCHAR(64),
  alarm_level VARCHAR(32),
  title VARCHAR(128),
  content VARCHAR(512),
  status VARCHAR(32),
  handled_by BIGINT,
  handled_at TIMESTAMP,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0
);
CREATE TABLE alarm_rules (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  rule_name VARCHAR(128),
  alarm_type VARCHAR(64),
  threshold_value DECIMAL(12,2),
  enabled TINYINT DEFAULT 1,
  description VARCHAR(512),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0
);
CREATE TABLE reports (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  report_name VARCHAR(128),
  report_type VARCHAR(32),
  station_id BIGINT,
  content TEXT,
  created_by BIGINT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  deleted TINYINT DEFAULT 0
);
CREATE TABLE operation_logs (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(64),
  operation VARCHAR(128),
  method VARCHAR(255),
  request_uri VARCHAR(255),
  ip VARCHAR(64),
  status VARCHAR(32),
  error_message VARCHAR(512),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE login_logs (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(64),
  ip VARCHAR(64),
  status VARCHAR(32),
  message VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
