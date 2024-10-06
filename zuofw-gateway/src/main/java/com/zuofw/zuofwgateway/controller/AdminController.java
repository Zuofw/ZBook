package com.zuofw.zuofwgateway.controller;


import com.zuofw.domain.vo.Result;
import com.zuofw.enums.ErrorCodeEnum;
import com.zuofw.util.ResultUtils;
import com.zuofw.zuofwgateway.domain.bo.AdminLoginRequest;
import com.zuofw.zuofwgateway.domain.bo.AdminRegisterRequest;
import com.zuofw.zuofwgateway.service.LoginService;
import com.zuofw.zuofwgateway.service.UserService;
import com.zuofw.zuofwgateway.utils.SecurityUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {


    @Resource
    private LoginService loginService;

    @PostMapping("/login")
    public Result<String> login(@RequestBody @Validated AdminLoginRequest loginRequest) {
        return ResultUtils.success(loginService.adminLogin(loginRequest.getAdminAccount(), loginRequest.getAdminPassword()));
    }
    @PostMapping("/register")
    public Result<?> register(@RequestBody @Validated AdminRegisterRequest registerRequest) {
        loginService.adminRegister(registerRequest);
        return ResultUtils.success(null);
    }
    @PostMapping("/logout")
    public Result<?> logout(HttpServletRequest request){
        log.info("logout");
        loginService.adminLogout(request);
        return ResultUtils.success("bye");
    }
}
