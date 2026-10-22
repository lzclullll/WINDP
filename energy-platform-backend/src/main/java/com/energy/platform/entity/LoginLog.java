package com.energy.platform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("login_logs")
public class LoginLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String ip;
    private String status;
    private String message;
    private LocalDateTime createdAt;
}