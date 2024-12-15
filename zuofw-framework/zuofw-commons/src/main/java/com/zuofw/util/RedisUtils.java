package com.zuofw.util;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
@Slf4j
public class RedisUtils {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    private static final String KEY_PREFIX = "lock:";
    private static final String ID_PREFIX = UUID.randomUUID().toString(true) + "-";

    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;
    static {
        UNLOCK_SCRIPT = new DefaultRedisScript<>();
        UNLOCK_SCRIPT.setLocation(new ClassPathResource("unlock.lua"));
        UNLOCK_SCRIPT.setResultType(Long.class);
    }

    // 使用new 手动new一个线程池，选择队列不能为无界队列，否则会导致OOM
//    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);



    public <T> void setCache(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public <T> void setExpireCache(String key, T value, int timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    @SuppressWarnings("unchecked")
    public <T> T getCache(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        // 防止NPE
        return Objects.isNull(value) ? null : (T) value;
    }
    public <T> void deleteCache(String key){
        redisTemplate.delete(key);
    }
    private Boolean tryLock(String key, long timeoutSec) {
        // 获取当前线程标识
        String threadId = ID_PREFIX + Thread.currentThread().getId();
        Boolean flag = redisTemplate.opsForValue()
                .setIfAbsent(KEY_PREFIX + key, threadId, timeoutSec, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }
    private void unLock(String key) {
        redisTemplate.execute(
                UNLOCK_SCRIPT,
                Collections.singletonList(KEY_PREFIX + key),
                ID_PREFIX + Thread.currentThread().getId());
    }
}