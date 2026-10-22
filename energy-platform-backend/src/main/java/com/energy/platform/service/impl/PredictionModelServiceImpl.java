package com.energy.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.energy.platform.entity.PredictionModel;
import com.energy.platform.mapper.PredictionModelMapper;
import com.energy.platform.service.PredictionModelCrudService;
import org.springframework.stereotype.Service;

@Service
public class PredictionModelServiceImpl extends ServiceImpl<PredictionModelMapper, PredictionModel> implements PredictionModelCrudService {}
