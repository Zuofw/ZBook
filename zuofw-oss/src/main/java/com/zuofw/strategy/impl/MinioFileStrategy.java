package com.zuofw.strategy.impl;


import cn.hutool.core.lang.UUID;
import com.zuofw.config.MinioProperties;
import com.zuofw.strategy.FileStrategy;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 *
 *
 * @author zuowei
 * @create 2024/10/9
 * @since 1.0.0
 */
@Slf4j
public class MinioFileStrategy implements FileStrategy {

    @Resource
    private MinioProperties minioProperties;
    @Resource
    private MinioClient minioClient;
    @Override
    public String uploadFile(MultipartFile file, String bucketName)  {
        log.info("### 上传文件到Minio ###");
        if(file == null || file.getSize() == 0){
            log.error("### 上传文件为空 ###");
            throw new RuntimeException("上传文件为空");
        }
        String fileName = file.getOriginalFilename();
        log.info("### 上传文件名：{} ###", fileName);
        String contentType = file.getContentType();
        String key = UUID.randomUUID().toString().replace("-", "");
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        String objectName = key + suffix;
        log.info("### 上传文件名：{} ###", objectName);
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(contentType)
                    .build());
        } catch (Exception e) {
            log.error("### 上传文件失败 ###", e);
            throw new RuntimeException("上传文件失败");
        }
       
        String url = String.format("%s/%s/%s", minioProperties.getEndpoint(), bucketName, objectName);
        log.info("### 上传文件成功，文件URL：{} ###", url);
        return url;
    }

}