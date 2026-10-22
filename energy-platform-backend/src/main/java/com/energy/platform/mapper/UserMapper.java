package com.energy.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.energy.platform.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {}