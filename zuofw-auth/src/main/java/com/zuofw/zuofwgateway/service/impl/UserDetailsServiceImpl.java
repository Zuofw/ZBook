package com.zuofw.zuofwgateway.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zuofw.constant.RoleConstants;
import com.zuofw.domain.entity.User;
import com.zuofw.zuofwgateway.domain.LoginUser;
import com.zuofw.zuofwgateway.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 〈〉
 *
 * @author qingqiu
 * @create 2024/10/6
 * @since 1.0.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getOne(new QueryWrapper<User>().eq("user_name", username));
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        Integer roleId = 0;
        return new LoginUser(
                user.getId().toString(),
                user.getPassword(),
                roleId,
                null,
                getRoleList(roleId)
        );
    }

    public List<GrantedAuthority> getRoleList(int roleId) {
        Map<Integer, String> roleMap = RoleConstants.roleMap;
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(roleMap.get(roleId)));
        return authorities;
    }
}