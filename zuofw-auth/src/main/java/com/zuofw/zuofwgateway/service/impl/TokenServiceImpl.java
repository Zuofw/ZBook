package com.zuofw.zuofwgateway.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.zuofw.constant.RedisConstants;
import com.zuofw.util.RedisCache;
import com.zuofw.zuofwgateway.domain.LoginUser;
import com.zuofw.zuofwgateway.service.TokenService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 〈TokenService〉
 *
 * @author zuowei
 * @create 2024/4/9
 * @since 1.0.0
 */
@Service
@Slf4j
public class TokenServiceImpl implements TokenService {
    @Value("${jwt.secret}")
    private String secret;
    private final static String LOGIN_USER_KEY = "login_user_key";
    //过期时间
    private final static int EXPIRE_TIME = 30;// 30分钟
    @Resource
    private RedisCache redisCache;
    @Override
    public String createToken(LoginUser loginUser) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        loginUser.setUUID(uuid);
        redisCache.setExpireCache(RedisConstants.USER_LOGIN_KEY + uuid, loginUser, EXPIRE_TIME, TimeUnit.MINUTES);
        HashMap<String,Object> playLoad = new HashMap<>() {
            {
                put(LOGIN_USER_KEY,uuid);
            }
        };
        return JWTUtil.createToken(playLoad,secret.getBytes());
    }

    @Override
    public LoginUser getLoginUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        log.info("token:{}",token);
        if(StrUtil.isEmpty(token) || StrUtil.isBlank(token)){
            return null;
        }
        String uuid = getLoginKey(token);
        return redisCache.getCache(RedisConstants.USER_LOGIN_KEY +uuid);
    }

    @Override
    public void refreshToken(LoginUser loginUser) {
        String uuid = loginUser.getUUID();
        redisCache.setExpireCache(RedisConstants.USER_LOGIN_KEY + uuid,loginUser,EXPIRE_TIME,TimeUnit.MINUTES);
    }

    @Override
    public void removeToken(String token) {
        String uuid = getLoginKey(token);
        redisCache.deleteCache(RedisConstants.USER_LOGIN_KEY + uuid);
    }

    /*
     * @description:  获取登录用户的uuid
     * @author zuowei
     * @date: 2024/4/9 15:50
     * @param token
     * @return java.lang.String
     */
    private String getLoginKey(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        return (String) jwt.getPayload(LOGIN_USER_KEY);
    }
}