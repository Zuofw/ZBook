package com.zuofw.config;

import com.zuofw.interceptor.FeignRequestInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 〈〉
 *
 * @author zuowei
 * @create 2024/10/19
 * @since 1.0.0
 */
@AutoConfiguration // 为什么要加这个注解？ 为了让SpringBoot能够自动扫描到这个类
public class FeignContextAutoConfiguration {
    @Bean
    public FeignRequestInterceptor feignRequestInterceptor() {
        return new FeignRequestInterceptor();
    }
}