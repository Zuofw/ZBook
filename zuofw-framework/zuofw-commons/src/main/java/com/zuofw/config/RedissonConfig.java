package com.zuofw.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;

import javax.swing.plaf.SpinnerUI;

/**
 * Redisson配置
 *
 * @author zuowei
 * @create 2024/12/13
 * @since 1.0.0
 */
public class RedissonConfig {
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.61.190")
                .setPassword("bronya");
        return Redisson.create(config);
    }
}