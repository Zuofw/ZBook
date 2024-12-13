package com.zuofw;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 〈〉
 *
 * @author zuowei
 * @create 2024/10/20
 * @since 1.0.0
 */
@SpringBootApplication
@MapperScan("com.zuofw.mapper")
@EnableAsync
@EnableFeignClients(basePackages = "com.zuofw.client")
public class ZuofwUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuofwUserApplication.class, args);
    }
}