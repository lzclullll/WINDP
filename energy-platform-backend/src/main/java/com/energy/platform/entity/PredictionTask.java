package com.energy.platform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("prediction_tasks")
public class PredictionTask {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long stationId;
    private String modelType;
    private Integer horizon;
    private Integer confidenceLevel;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer deleted;
}