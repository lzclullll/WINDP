package com.energy.platform.service;

import com.energy.platform.dto.PredictionRequest;
import com.energy.platform.entity.PredictionResult;

public interface PredictionEngineService {
    PredictionResult predict(PredictionRequest request, Long taskId);
}
