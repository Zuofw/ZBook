package com.zuofw.zuofwgateway.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zuofw.domain.entity.User;
import com.zuofw.zuofwgateway.mapper.UserMapper;
import com.zuofw.zuofwgateway.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 〈〉
 *
 * @author zuowei
 * @create 2024/10/6
 * @since 1.0.0
 */
@Service
public class UserServiceImpl extends  ServiceImpl<UserMapper, User> implements UserService {

}