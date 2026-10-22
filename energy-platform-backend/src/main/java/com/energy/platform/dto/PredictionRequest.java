package com.energy.platform.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PredictionRequest {
    @NotNull private Long stationId;
    private String modelType = "Attention-Heuristic";
    @Min(1) @Max(24) private Integer horizon = 2;
    @Min(50) @Max(99) private Integer confidenceLevel = 90;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
