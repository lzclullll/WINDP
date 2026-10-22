package com.energy.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.energy.platform.entity.WorkOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WorkOrderMapper extends BaseMapper<WorkOrder> {}