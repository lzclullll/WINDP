package com.energy.platform.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;

@Data
@TableName("stations")
public class Station {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String code;
    private String type;
    private BigDecimal capacity;
    private String location;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private LocalDate gridConnectionDate;
    private String status;
    private Long managerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer deleted;
}