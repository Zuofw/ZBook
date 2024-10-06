package com.zuofw.zuofwgateway.security;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.zuofw.enums.ErrorCodeEnum;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zuowei
 * @create 2024/4/9
 * @since 1.0.0
 */
@Component
public class UnAuthorizedRequestHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(ErrorCodeEnum.OK.getCode());
        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        Map<String,Object> responseData = new HashMap<>();
        responseData.put("code", ErrorCodeEnum.UNAUTHORIZED.getCode());
        responseData.put("message", authException.getMessage());
        responseData.put("description", "身份信息验证失败, 请重新登录");
        responseData.put("data", null);
        try{
            PrintWriter writer = response.getWriter();
            writer.append(JSONUtil.toJsonStr(responseData, JSONConfig.create().setIgnoreNullValue(false)));
            writer.flush();
        } catch (IOException e) {
            response.setStatus(ErrorCodeEnum.INTERNAL_SERVER_ERROR.getCode());
        }
    }
}