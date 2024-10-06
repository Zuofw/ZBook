package com.zuofw.zuofwgateway.security;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.zuofw.enums.ErrorCodeEnum;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈权限不足handler〉
 *
 * @author zuowei
 * @create 2024/4/9
 * @since 1.0.0
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    /*
     * @description:  处理逻辑
     * @author zuowei
     * @date: 2024/4/9 14:54
     * @param request
     * @param response
     * @param accessDeniedException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        //设置返回格式
        response.setStatus(ErrorCodeEnum.OK.getCode());
        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        Map<String,Object> responseData = new HashMap<>();
        responseData.put("code",ErrorCodeEnum.UNAUTHORIZED.getCode());
        //设置返回信息
        responseData.put("message",accessDeniedException.getMessage());
        responseData.put("description","没有权限");
        responseData.put("data",null);
        try {
            PrintWriter writer = response.getWriter();
            //不忽略null值
            writer.append(JSONUtil.toJsonStr(responseData, JSONConfig.create().setIgnoreNullValue(false)));
            writer.flush();
        } catch (Exception e) {
            response.setStatus(ErrorCodeEnum.INTERNAL_SERVER_ERROR.getCode());
        }
    }
}