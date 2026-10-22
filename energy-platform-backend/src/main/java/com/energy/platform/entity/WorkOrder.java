package com.energy.platform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("work_orders")
public class WorkOrder {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String description;
    private Long stationId;
    private Long deviceId;
    private String type;
    private String priority;
    private String status;
    private Long assigneeId;
    private Long creatorId;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer deleted;
}