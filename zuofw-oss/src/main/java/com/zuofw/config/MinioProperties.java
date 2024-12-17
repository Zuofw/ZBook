package com.zuofw.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 〈〉
 *
 * @author qingqiu
 * @create 2024/10/19
 * @since 1.0.0
 */
@Component
@ConfigurationProperties(prefix = "minio")
@Data
public class MinioProperties {
    private String endpoint;

    private String accessKey;

    private String secretKey;
}