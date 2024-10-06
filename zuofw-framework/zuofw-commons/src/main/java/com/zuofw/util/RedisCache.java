package com.zuofw.util;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 〈Redis缓存工具类〉<br>
 * 〈〉
 *
 * @author zuowei
 * @create 2024/4/9
 * @since 1.0.0
 */
@Component
public class RedisCache {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public <T> void setCache(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }
    public <T> void setExpireCache (String key, T value, int timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }
    @SuppressWarnings("unchecked")
    public <T> T getCache(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        return Objects.isNull(value) ? null : (T) value;
    }
    public <T>  void deleteCache(String key) {
        redisTemplate.delete(key);
    }
}