package com.energy.platform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("operation_logs")
public class OperationLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String operation;
    private String method;
    private String requestUri;
    private String ip;
    private String status;
    private String errorMessage;
    private LocalDateTime createdAt;
}