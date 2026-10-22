package com.energy.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.energy.platform.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {}