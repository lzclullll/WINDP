package com.energy.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.energy.platform.entity.Device;
import com.energy.platform.mapper.DeviceMapper;
import com.energy.platform.service.DeviceService;
import org.springframework.stereotype.Service;

@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements DeviceService {}