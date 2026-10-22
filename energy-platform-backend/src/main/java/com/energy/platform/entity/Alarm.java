package com.energy.platform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("alarms")
public class Alarm {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long stationId;
    private Long deviceId;
    private String alarmType;
    private String alarmLevel;
    private String title;
    private String content;
    private String status;
    private Long handledBy;
    private LocalDateTime handledAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer deleted;
}