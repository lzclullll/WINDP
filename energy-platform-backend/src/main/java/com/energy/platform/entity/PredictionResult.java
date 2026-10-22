package com.energy.platform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("predictions")
public class PredictionResult {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long stationId;
    private Long taskId;
    private String modelType;
    private LocalDateTime predictionTime;
    private Integer horizon;
    private String metrics;
    private String resultData;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer deleted;
}