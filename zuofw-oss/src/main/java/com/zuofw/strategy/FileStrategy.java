package com.zuofw.strategy;

import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 文件策略工厂
 *
 * @author qingqiu
 * @create 2024/10/9
 * @since 1.0.0
 */
public interface FileStrategy{
    /*
     * @description:  上传文件
     * @autho qingqiu
     * @date: 2024/10/9 10:03
     * @param file
     * @param bucketName
     * @return java.lang.String
     */
    String uploadFile(MultipartFile file, String bucketName);
}