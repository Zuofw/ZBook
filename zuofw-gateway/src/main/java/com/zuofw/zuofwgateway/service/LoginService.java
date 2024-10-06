package com.zuofw.zuofwgateway.service;

import com.zuofw.zuofwgateway.domain.bo.AdminRegisterRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface LoginService {
    String adminLogin(String adminAccount, String adminPassword);

    void adminRegister(AdminRegisterRequest adminRegisterRequest);

    void adminLogout(HttpServletRequest request);

}