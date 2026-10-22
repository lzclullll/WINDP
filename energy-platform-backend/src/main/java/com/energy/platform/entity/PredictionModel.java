package com.energy.platform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("prediction_models")
public class PredictionModel {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String modelName;
    private String modelType;
    private String version;
    private String status;
    private String description;
    private String metrics;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer deleted;
}