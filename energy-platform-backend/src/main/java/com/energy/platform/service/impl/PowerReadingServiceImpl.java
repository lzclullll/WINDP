package com.energy.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.energy.platform.entity.PowerReading;
import com.energy.platform.mapper.PowerReadingMapper;
import com.energy.platform.service.PowerReadingService;
import org.springframework.stereotype.Service;

@Service
public class PowerReadingServiceImpl extends ServiceImpl<PowerReadingMapper, PowerReading> implements PowerReadingService {}