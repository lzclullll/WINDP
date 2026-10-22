package com.energy.platform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
@TableName("power_readings")
public class PowerReading {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long stationId;
    private LocalDateTime recordTime;
    private BigDecimal powerKw;
    private BigDecimal windSpeed;
    private BigDecimal irradiance;
    private BigDecimal temperature;
    private BigDecimal humidity;
    private String deviceStatus;
    private String dataQuality;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer deleted;
}