package com.energy.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.energy.platform.entity.Permission;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {}