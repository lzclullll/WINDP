package com.energy.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.energy.platform.entity.Alarm;
import com.energy.platform.mapper.AlarmMapper;
import com.energy.platform.service.AlarmService;
import org.springframework.stereotype.Service;

@Service
public class AlarmServiceImpl extends ServiceImpl<AlarmMapper, Alarm> implements AlarmService {}