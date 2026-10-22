INSERT INTO roles(id, role_code, role_name, description) VALUES
(1,'ADMIN','系统管理员','拥有全部权限'),
(2,'STATION_MANAGER','电站管理员','管理所属电站'),
(4,'OPERATOR','运维人员','查看并处理分配的维修工单');
INSERT INTO users(id, username, password, real_name, email, phone, status, station_id) VALUES
(1,'admin','{bcrypt}$2a$10$z5HCRlCZHDlge29GJRrdPuPIiI11pj.mqB0N2BTC22jOpeCTaLbh2','系统管理员','admin@example.com','13800000000','ENABLED',NULL),
(2,'manager','{bcrypt}$2a$10$z5HCRlCZHDlge29GJRrdPuPIiI11pj.mqB0N2BTC22jOpeCTaLbh2','电站管理员','manager@example.com','13800000001','ENABLED',1),
(4,'operator','{bcrypt}$2a$10$z5HCRlCZHDlge29GJRrdPuPIiI11pj.mqB0N2BTC22jOpeCTaLbh2','运维人员','operator@example.com','13800000003','ENABLED',1);
INSERT INTO user_roles(user_id, role_id) VALUES (1,1),(2,2),(4,4);
INSERT INTO permissions(id, permission_code, permission_name, permission_type, path, sort_order) VALUES
(1,'dashboard:view','综合看板','MENU','/dashboard',1),
(2,'station:manage','电站管理','MENU','/stations',2),
(3,'device:manage','设备管理','MENU','/devices',3),
(4,'power:view','功率数据','MENU','/power/realtime',4),
(5,'prediction:manage','功率预测','MENU','/prediction/tasks',5),
(6,'alarm:manage','告警管理','MENU','/alarms',6),
(7,'workorder:manage','工单管理','MENU','/work-orders',7),
(8,'report:view','报表管理','MENU','/reports',8),
(9,'system:manage','系统管理','MENU','/system/users',9),
(10,'assistant:use','运维知识助手','MENU','/assistant',10),
(11,'workorder:view','查看工单','BUTTON','/work-orders',11),
(12,'workorder:resolve','解决工单','BUTTON','/work-orders/*/complete',12);
INSERT INTO role_permissions(role_id, permission_id) SELECT 1, id FROM permissions;
INSERT INTO role_permissions(role_id, permission_id) VALUES
(2,1),(2,2),(2,3),(2,4),(2,5),(2,6),(2,7),(2,10),(2,11),(2,12),
(4,1),(4,11),(4,12);
INSERT INTO stations(id,name,code,type,capacity,location,latitude,longitude,grid_connection_date,status,manager_id) VALUES
(1,'张北风电一场','WIND-ZB-001','WIND',150000,'河北张家口',41.15,114.72,'2023-03-01','RUNNING',2),
(2,'酒泉风电基地','WIND-JQ-002','WIND',220000,'甘肃酒泉',39.73,98.49,'2022-08-18','RUNNING',2),
(3,'达坂城风电场','WIND-DBC-003','WIND',180000,'新疆乌鲁木齐',43.36,88.31,'2021-11-10','MAINTENANCE',2),
(4,'格尔木光伏站','SOLAR-GEM-004','SOLAR',120000,'青海格尔木',36.41,94.90,'2023-06-20','RUNNING',2),
(5,'敦煌光伏站','SOLAR-DH-005','SOLAR',100000,'甘肃敦煌',40.14,94.66,'2022-05-30','RUNNING',2);
INSERT INTO devices(station_id,device_name,device_code,device_type,model,manufacturer,rated_power,install_date,health_status,running_status) VALUES
(1,'1号风机','WT-001-01','TURBINE','GW-2500','金风科技',2500,'2023-03-01','GOOD','RUNNING'),
(1,'箱变A','BX-001-A','TRANSFORMER','SCB13','特变电工',5000,'2023-03-01','GOOD','RUNNING'),
(2,'3号风机','WT-002-03','TURBINE','MY-3200','明阳智能',3200,'2022-08-18','GOOD','RUNNING'),
(3,'7号风机','WT-003-07','TURBINE','EN-3000','远景能源',3000,'2021-11-10','WARN','MAINTENANCE'),
(4,'逆变器A1','PV-004-A1','INVERTER','SG250HX','阳光电源',250,'2023-06-20','GOOD','RUNNING'),
(5,'汇流箱B2','PV-005-B2','COMBINER','PVS-16','华为数字能源',160,'2022-05-30','GOOD','RUNNING');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (1, DATEADD('HOUR', -24, CURRENT_TIMESTAMP), 1160, 7.0, 80, 20, 50, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (1, DATEADD('HOUR', -23, CURRENT_TIMESTAMP), 1240, 7.1, 83, 21, 51, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (1, DATEADD('HOUR', -22, CURRENT_TIMESTAMP), 1312, 7.2, 86, 22, 52, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (1, DATEADD('HOUR', -21, CURRENT_TIMESTAMP), 1369, 7.3, 89, 23, 53, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (1, DATEADD('HOUR', -20, CURRENT_TIMESTAMP), 1406, 7.4, 92, 24, 54, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (1, DATEADD('HOUR', -19, CURRENT_TIMESTAMP), 1419, 7.5, 95, 25, 55, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (1, DATEADD('HOUR', -18, CURRENT_TIMESTAMP), 1408, 7.6, 98, 26, 56, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (1, DATEADD('HOUR', -17, CURRENT_TIMESTAMP), 1375, 7.7, 101, 27, 57, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (1, DATEADD('HOUR', -16, CURRENT_TIMESTAMP), 1325, 7.8, 104, 20, 58, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (1, DATEADD('HOUR', -15, CURRENT_TIMESTAMP), 1263, 7.0, 107, 21, 59, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (1, DATEADD('HOUR', -14, CURRENT_TIMESTAMP), 1198, 7.1, 110, 22, 60, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (1, DATEADD('HOUR', -13, CURRENT_TIMESTAMP), 1138, 7.2, 113, 23, 61, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (1, DATEADD('HOUR', -12, CURRENT_TIMESTAMP), 1090, 7.3, 116, 24, 62, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (1, DATEADD('HOUR', -11, CURRENT_TIMESTAMP), 1060, 7.4, 119, 25, 63, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (1, DATEADD('HOUR', -10, CURRENT_TIMESTAMP), 1052, 7.5, 122, 26, 64, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (1, DATEADD('HOUR', -9, CURRENT_TIMESTAMP), 1069, 7.6, 125, 27, 65, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (1, DATEADD('HOUR', -8, CURRENT_TIMESTAMP), 1109, 7.7, 128, 20, 66, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (1, DATEADD('HOUR', -7, CURRENT_TIMESTAMP), 1169, 7.8, 131, 21, 67, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (1, DATEADD('HOUR', -6, CURRENT_TIMESTAMP), 1243, 7.0, 134, 22, 68, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (1, DATEADD('HOUR', -5, CURRENT_TIMESTAMP), 1323, 7.1, 137, 23, 69, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (1, DATEADD('HOUR', -4, CURRENT_TIMESTAMP), 1402, 7.2, 140, 24, 50, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (1, DATEADD('HOUR', -3, CURRENT_TIMESTAMP), 1473, 7.3, 143, 25, 51, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (1, DATEADD('HOUR', -2, CURRENT_TIMESTAMP), 1527, 7.4, 146, 26, 52, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (1, DATEADD('HOUR', -1, CURRENT_TIMESTAMP), 1560, 7.5, 149, 27, 53, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (2, DATEADD('HOUR', -24, CURRENT_TIMESTAMP), 1420, 8.0, 80, 20, 50, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (2, DATEADD('HOUR', -23, CURRENT_TIMESTAMP), 1500, 8.1, 83, 21, 51, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (2, DATEADD('HOUR', -22, CURRENT_TIMESTAMP), 1572, 8.2, 86, 22, 52, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (2, DATEADD('HOUR', -21, CURRENT_TIMESTAMP), 1629, 8.3, 89, 23, 53, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (2, DATEADD('HOUR', -20, CURRENT_TIMESTAMP), 1666, 8.4, 92, 24, 54, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (2, DATEADD('HOUR', -19, CURRENT_TIMESTAMP), 1679, 8.5, 95, 25, 55, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (2, DATEADD('HOUR', -18, CURRENT_TIMESTAMP), 1668, 8.6, 98, 26, 56, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (2, DATEADD('HOUR', -17, CURRENT_TIMESTAMP), 1635, 8.7, 101, 27, 57, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (2, DATEADD('HOUR', -16, CURRENT_TIMESTAMP), 1585, 8.8, 104, 20, 58, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (2, DATEADD('HOUR', -15, CURRENT_TIMESTAMP), 1523, 8.0, 107, 21, 59, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (2, DATEADD('HOUR', -14, CURRENT_TIMESTAMP), 1458, 8.1, 110, 22, 60, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (2, DATEADD('HOUR', -13, CURRENT_TIMESTAMP), 1398, 8.2, 113, 23, 61, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (2, DATEADD('HOUR', -12, CURRENT_TIMESTAMP), 1350, 8.3, 116, 24, 62, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (2, DATEADD('HOUR', -11, CURRENT_TIMESTAMP), 1320, 8.4, 119, 25, 63, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (2, DATEADD('HOUR', -10, CURRENT_TIMESTAMP), 1312, 8.5, 122, 26, 64, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (2, DATEADD('HOUR', -9, CURRENT_TIMESTAMP), 1329, 8.6, 125, 27, 65, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (2, DATEADD('HOUR', -8, CURRENT_TIMESTAMP), 1369, 8.7, 128, 20, 66, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (2, DATEADD('HOUR', -7, CURRENT_TIMESTAMP), 1429, 8.8, 131, 21, 67, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (2, DATEADD('HOUR', -6, CURRENT_TIMESTAMP), 1503, 8.0, 134, 22, 68, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (2, DATEADD('HOUR', -5, CURRENT_TIMESTAMP), 1583, 8.1, 137, 23, 69, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (2, DATEADD('HOUR', -4, CURRENT_TIMESTAMP), 1662, 8.2, 140, 24, 50, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (2, DATEADD('HOUR', -3, CURRENT_TIMESTAMP), 1733, 8.3, 143, 25, 51, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (2, DATEADD('HOUR', -2, CURRENT_TIMESTAMP), 1787, 8.4, 146, 26, 52, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (2, DATEADD('HOUR', -1, CURRENT_TIMESTAMP), 1820, 8.5, 149, 27, 53, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (3, DATEADD('HOUR', -24, CURRENT_TIMESTAMP), 1680, 9.0, 80, 20, 50, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (3, DATEADD('HOUR', -23, CURRENT_TIMESTAMP), 1760, 9.1, 83, 21, 51, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (3, DATEADD('HOUR', -22, CURRENT_TIMESTAMP), 1832, 9.2, 86, 22, 52, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (3, DATEADD('HOUR', -21, CURRENT_TIMESTAMP), 1889, 9.3, 89, 23, 53, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (3, DATEADD('HOUR', -20, CURRENT_TIMESTAMP), 1926, 9.4, 92, 24, 54, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (3, DATEADD('HOUR', -19, CURRENT_TIMESTAMP), 1939, 9.5, 95, 25, 55, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (3, DATEADD('HOUR', -18, CURRENT_TIMESTAMP), 1928, 9.6, 98, 26, 56, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (3, DATEADD('HOUR', -17, CURRENT_TIMESTAMP), 1895, 9.7, 101, 27, 57, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (3, DATEADD('HOUR', -16, CURRENT_TIMESTAMP), 1845, 9.8, 104, 20, 58, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (3, DATEADD('HOUR', -15, CURRENT_TIMESTAMP), 1783, 9.0, 107, 21, 59, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (3, DATEADD('HOUR', -14, CURRENT_TIMESTAMP), 1718, 9.1, 110, 22, 60, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (3, DATEADD('HOUR', -13, CURRENT_TIMESTAMP), 1658, 9.2, 113, 23, 61, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (3, DATEADD('HOUR', -12, CURRENT_TIMESTAMP), 1610, 9.3, 116, 24, 62, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (3, DATEADD('HOUR', -11, CURRENT_TIMESTAMP), 1580, 9.4, 119, 25, 63, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (3, DATEADD('HOUR', -10, CURRENT_TIMESTAMP), 1572, 9.5, 122, 26, 64, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (3, DATEADD('HOUR', -9, CURRENT_TIMESTAMP), 1589, 9.6, 125, 27, 65, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (3, DATEADD('HOUR', -8, CURRENT_TIMESTAMP), 1629, 9.7, 128, 20, 66, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (3, DATEADD('HOUR', -7, CURRENT_TIMESTAMP), 1689, 9.8, 131, 21, 67, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (3, DATEADD('HOUR', -6, CURRENT_TIMESTAMP), 1763, 9.0, 134, 22, 68, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (3, DATEADD('HOUR', -5, CURRENT_TIMESTAMP), 1843, 9.1, 137, 23, 69, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (3, DATEADD('HOUR', -4, CURRENT_TIMESTAMP), 1922, 9.2, 140, 24, 50, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (3, DATEADD('HOUR', -3, CURRENT_TIMESTAMP), 1993, 9.3, 143, 25, 51, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (3, DATEADD('HOUR', -2, CURRENT_TIMESTAMP), 2047, 9.4, 146, 26, 52, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (3, DATEADD('HOUR', -1, CURRENT_TIMESTAMP), 2080, 9.5, 149, 27, 53, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (4, DATEADD('HOUR', -24, CURRENT_TIMESTAMP), 1940, 10.0, 500, 20, 50, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (4, DATEADD('HOUR', -23, CURRENT_TIMESTAMP), 2020, 10.1, 518, 21, 51, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (4, DATEADD('HOUR', -22, CURRENT_TIMESTAMP), 2092, 10.2, 536, 22, 52, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (4, DATEADD('HOUR', -21, CURRENT_TIMESTAMP), 2149, 10.3, 554, 23, 53, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (4, DATEADD('HOUR', -20, CURRENT_TIMESTAMP), 2186, 10.4, 572, 24, 54, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (4, DATEADD('HOUR', -19, CURRENT_TIMESTAMP), 2199, 10.5, 590, 25, 55, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (4, DATEADD('HOUR', -18, CURRENT_TIMESTAMP), 2188, 10.6, 608, 26, 56, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (4, DATEADD('HOUR', -17, CURRENT_TIMESTAMP), 2155, 10.7, 626, 27, 57, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (4, DATEADD('HOUR', -16, CURRENT_TIMESTAMP), 2105, 10.8, 644, 20, 58, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (4, DATEADD('HOUR', -15, CURRENT_TIMESTAMP), 2043, 10.0, 662, 21, 59, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (4, DATEADD('HOUR', -14, CURRENT_TIMESTAMP), 1978, 10.1, 680, 22, 60, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (4, DATEADD('HOUR', -13, CURRENT_TIMESTAMP), 1918, 10.2, 698, 23, 61, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (4, DATEADD('HOUR', -12, CURRENT_TIMESTAMP), 1870, 10.3, 716, 24, 62, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (4, DATEADD('HOUR', -11, CURRENT_TIMESTAMP), 1840, 10.4, 734, 25, 63, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (4, DATEADD('HOUR', -10, CURRENT_TIMESTAMP), 1832, 10.5, 752, 26, 64, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (4, DATEADD('HOUR', -9, CURRENT_TIMESTAMP), 1849, 10.6, 770, 27, 65, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (4, DATEADD('HOUR', -8, CURRENT_TIMESTAMP), 1889, 10.7, 788, 20, 66, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (4, DATEADD('HOUR', -7, CURRENT_TIMESTAMP), 1949, 10.8, 806, 21, 67, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (4, DATEADD('HOUR', -6, CURRENT_TIMESTAMP), 2023, 10.0, 824, 22, 68, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (4, DATEADD('HOUR', -5, CURRENT_TIMESTAMP), 2103, 10.1, 842, 23, 69, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (4, DATEADD('HOUR', -4, CURRENT_TIMESTAMP), 2182, 10.2, 860, 24, 50, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (4, DATEADD('HOUR', -3, CURRENT_TIMESTAMP), 2253, 10.3, 878, 25, 51, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (4, DATEADD('HOUR', -2, CURRENT_TIMESTAMP), 2307, 10.4, 896, 26, 52, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (4, DATEADD('HOUR', -1, CURRENT_TIMESTAMP), 2340, 10.5, 914, 27, 53, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (5, DATEADD('HOUR', -24, CURRENT_TIMESTAMP), 2200, 11.0, 500, 20, 50, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (5, DATEADD('HOUR', -23, CURRENT_TIMESTAMP), 2280, 11.1, 518, 21, 51, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (5, DATEADD('HOUR', -22, CURRENT_TIMESTAMP), 2352, 11.2, 536, 22, 52, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (5, DATEADD('HOUR', -21, CURRENT_TIMESTAMP), 2409, 11.3, 554, 23, 53, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (5, DATEADD('HOUR', -20, CURRENT_TIMESTAMP), 2446, 11.4, 572, 24, 54, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (5, DATEADD('HOUR', -19, CURRENT_TIMESTAMP), 2459, 11.5, 590, 25, 55, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (5, DATEADD('HOUR', -18, CURRENT_TIMESTAMP), 2448, 11.6, 608, 26, 56, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (5, DATEADD('HOUR', -17, CURRENT_TIMESTAMP), 2415, 11.7, 626, 27, 57, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (5, DATEADD('HOUR', -16, CURRENT_TIMESTAMP), 2365, 11.8, 644, 20, 58, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (5, DATEADD('HOUR', -15, CURRENT_TIMESTAMP), 2303, 11.0, 662, 21, 59, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (5, DATEADD('HOUR', -14, CURRENT_TIMESTAMP), 2238, 11.1, 680, 22, 60, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (5, DATEADD('HOUR', -13, CURRENT_TIMESTAMP), 2178, 11.2, 698, 23, 61, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (5, DATEADD('HOUR', -12, CURRENT_TIMESTAMP), 2130, 11.3, 716, 24, 62, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (5, DATEADD('HOUR', -11, CURRENT_TIMESTAMP), 2100, 11.4, 734, 25, 63, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (5, DATEADD('HOUR', -10, CURRENT_TIMESTAMP), 2092, 11.5, 752, 26, 64, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (5, DATEADD('HOUR', -9, CURRENT_TIMESTAMP), 2109, 11.6, 770, 27, 65, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (5, DATEADD('HOUR', -8, CURRENT_TIMESTAMP), 2149, 11.7, 788, 20, 66, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (5, DATEADD('HOUR', -7, CURRENT_TIMESTAMP), 2209, 11.8, 806, 21, 67, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (5, DATEADD('HOUR', -6, CURRENT_TIMESTAMP), 2283, 11.0, 824, 22, 68, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (5, DATEADD('HOUR', -5, CURRENT_TIMESTAMP), 2363, 11.1, 842, 23, 69, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (5, DATEADD('HOUR', -4, CURRENT_TIMESTAMP), 2442, 11.2, 860, 24, 50, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (5, DATEADD('HOUR', -3, CURRENT_TIMESTAMP), 2513, 11.3, 878, 25, 51, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (5, DATEADD('HOUR', -2, CURRENT_TIMESTAMP), 2567, 11.4, 896, 26, 52, 'NORMAL', 'GOOD');
INSERT INTO power_readings(station_id, record_time, power_kw, wind_speed, irradiance, temperature, humidity, device_status, data_quality) VALUES (5, DATEADD('HOUR', -1, CURRENT_TIMESTAMP), 2600, 11.5, 914, 27, 53, 'NORMAL', 'GOOD');

INSERT INTO prediction_models(model_name,model_type,version,status,description,metrics) VALUES
('注意力启发式预测','Attention-Heuristic','1.1','ACTIVE','基于历史功率与气象变量生成短期预测','{"evaluation":"pending-backtest"}');
INSERT INTO prediction_tasks(id,station_id,model_type,horizon,confidence_level,start_time,end_time,status) VALUES
(1,1,'Attention-Heuristic',2,90,CURRENT_TIMESTAMP,DATEADD('HOUR',2,CURRENT_TIMESTAMP),'COMPLETED');
INSERT INTO predictions(station_id,task_id,model_type,prediction_time,horizon,metrics,result_data) VALUES
(1,1,'Attention-Heuristic',CURRENT_TIMESTAMP,2,'{"evaluation":"pending-ground-truth","model":"attention-heuristic"}','[{"time":"2026-06-08 10:15","power":1460},{"time":"2026-06-08 10:30","power":1510},{"time":"2026-06-08 10:45","power":1568},{"time":"2026-06-08 11:00","power":1612},{"time":"2026-06-08 11:30","power":1660},{"time":"2026-06-08 12:00","power":1695}]');
INSERT INTO alarm_rules(rule_name,alarm_type,threshold_value,enabled,description) VALUES
('功率连续低值','POWER_LOW',10,1,'功率连续 5 分钟接近 0 触发'),
('风速过高','WIND_HIGH',20,1,'风速超过阈值触发告警'),
('预测偏差过大','PREDICTION_DEVIATION',15,1,'预测误差百分比超过阈值');
INSERT INTO alarms(station_id,device_id,alarm_type,alarm_level,title,content,status) VALUES
(3,4,'DEVICE','WARN','风机健康状态异常','7号风机处于维护状态，请安排巡检','OPEN'),
(1,1,'PREDICTION_DEVIATION','INFO','预测偏差提醒','最近一个周期预测偏差略高','OPEN');
INSERT INTO work_orders(title,description,station_id,device_id,type,priority,status,assignee_id,creator_id) VALUES
('达坂城 7 号风机巡检','检查叶片振动和变桨系统',3,4,'故障维修','HIGH','PROCESSING',4,1),
('格尔木逆变器季度保养','清洁滤网并检查接线端子',4,5,'预防性维护','MEDIUM','PENDING',4,2);
INSERT INTO reports(report_name,report_type,station_id,content,created_by) VALUES
('全站日报','DAILY',NULL,'{"generation":32800,"accuracy":null,"alarms":2,"orders":2}',1);
INSERT INTO operation_logs(username,operation,method,request_uri,ip,status) VALUES ('admin','系统初始化','INIT','/','127.0.0.1','SUCCESS');
INSERT INTO login_logs(username,ip,status,message) VALUES ('admin','127.0.0.1','SUCCESS','示例登录日志');
