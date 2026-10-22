package com.energy.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.energy.platform.entity.PredictionResult;
import com.energy.platform.mapper.PredictionResultMapper;
import com.energy.platform.service.PredictionResultService;
import org.springframework.stereotype.Service;

@Service
public class PredictionResultServiceImpl extends ServiceImpl<PredictionResultMapper, PredictionResult> implements PredictionResultService {}