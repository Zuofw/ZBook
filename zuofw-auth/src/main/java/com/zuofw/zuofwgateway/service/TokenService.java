package com.zuofw.zuofwgateway.service;

import cn.hutool.jwt.Claims;
import com.zuofw.zuofwgateway.domain.LoginUser;
import jakarta.servlet.http.HttpServletRequest;

public interface TokenService {
    String createToken(LoginUser loginUser);

    LoginUser getLoginUser(HttpServletRequest request);

    void refreshToken(LoginUser loginUser);
    void removeToken(String token);

}