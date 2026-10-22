package com.energy.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.energy.platform.entity.PredictionTask;
import com.energy.platform.mapper.PredictionTaskMapper;
import com.energy.platform.service.PredictionTaskService;
import org.springframework.stereotype.Service;

@Service
public class PredictionTaskServiceImpl extends ServiceImpl<PredictionTaskMapper, PredictionTask> implements PredictionTaskService {}