package com.zuofw.factory;

import com.zuofw.strategy.FileStrategy;
import com.zuofw.strategy.impl.MinioFileStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 〈〉
 *
 * @author qingqiu
 * @create 2024/10/9
 * @since 1.0.0
 */
@Configuration
@RefreshScope
public class FileStrategyFactory {
    @Value("${storage.type}")
    private String strategyType;

    @Bean
    @RefreshScope
    public FileStrategy getFileStrategy() {
        if(strategyType.equals("minio")) {
            return  new MinioFileStrategy();
        }
        throw  new RuntimeException("不可用的存储类型");

    }
}