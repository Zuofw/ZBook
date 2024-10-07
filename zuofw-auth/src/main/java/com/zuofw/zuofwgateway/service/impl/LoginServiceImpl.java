package com.zuofw.zuofwgateway.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zuofw.domain.entity.User;
import com.zuofw.enums.ErrorCodeEnum;
import com.zuofw.exception.BusinessException;
import com.zuofw.zuofwgateway.domain.LoginUser;
import com.zuofw.zuofwgateway.domain.bo.AdminRegisterRequest;
import com.zuofw.zuofwgateway.service.LoginService;
import com.zuofw.zuofwgateway.service.TokenService;
import com.zuofw.zuofwgateway.service.UserService;
import com.zuofw.zuofwgateway.utils.SecurityUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author zuowei
 * @create 2024/4/9
 * @since 1.0.0
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    @Resource
    private UserService adminService;
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private TokenService tokenService;
    private final static int EXPIRE_TIME = 30;

    @Override
    public String adminLogin(String adminAccount, String adminPassword) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(adminAccount, adminPassword);
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception ignored) {
        }
        if(authentication == null) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED, "账号或密码错误");
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return tokenService.createToken(loginUser);
    }

    @Override
    public void adminRegister(AdminRegisterRequest adminRegisterRequest) {
        if(adminService.getOne(new QueryWrapper<User>().eq("user_name", adminRegisterRequest.getAdminAccount())) != null) {
            throw new BusinessException(ErrorCodeEnum.UNAUTHORIZED, "账号已存在");
        }
        User admin = User.builder().userName(adminRegisterRequest.getAdminAccount())
                .password(adminRegisterRequest.getAdminPassword()).build();
        admin.setPassword(SecurityUtils.encodePassword(adminRegisterRequest.getAdminPassword()));
//        log.info("admin: {}", admin);
        adminService.save(admin);
    }

    @Override
    public void adminLogout(HttpServletRequest request) {
        log.info("logout");
        String token = request.getHeader("Authorization");
        log.info("这是Au" + token);
        tokenService.removeToken(token);
    }


}