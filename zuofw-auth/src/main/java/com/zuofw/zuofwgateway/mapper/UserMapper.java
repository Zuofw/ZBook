package com.zuofw.zuofwgateway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zuofw.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
