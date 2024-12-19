package com.zuofw.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioConfigInfo {

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucket;
    private Integer expiry;
    private Integer breakpointTime;
}
