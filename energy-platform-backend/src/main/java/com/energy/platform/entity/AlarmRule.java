package com.energy.platform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
@TableName("alarm_rules")
public class AlarmRule {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String ruleName;
    private String alarmType;
    private BigDecimal thresholdValue;
    private Integer enabled;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer deleted;
}