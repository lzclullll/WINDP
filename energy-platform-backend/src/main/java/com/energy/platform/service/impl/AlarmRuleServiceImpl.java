package com.energy.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.energy.platform.entity.AlarmRule;
import com.energy.platform.mapper.AlarmRuleMapper;
import com.energy.platform.service.AlarmRuleService;
import org.springframework.stereotype.Service;

@Service
public class AlarmRuleServiceImpl extends ServiceImpl<AlarmRuleMapper, AlarmRule> implements AlarmRuleService {}