package com.energy.platform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;

@Data
@TableName("devices")
public class Device {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long stationId;
    private String deviceName;
    private String deviceCode;
    private String deviceType;
    private String model;
    private String manufacturer;
    private BigDecimal ratedPower;
    private LocalDate installDate;
    private String healthStatus;
    private String runningStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer deleted;
}