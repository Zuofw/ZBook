package com.zuofw.service.impl;

import com.zuofw.domain.vo.Result;
import com.zuofw.service.FileService;
import com.zuofw.strategy.FileStrategy;
import com.zuofw.util.ResultUtils;
import io.minio.errors.*;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 〈〉
 *
 * @author qingqiu
 * @create 2024/10/19
 * @since 1.0.0
 */
@Service
public class FileServiceImpl implements FileService {

    private static final String BUSINESS_NAME = "zuofw";
    @Resource
    private FileStrategy fileStrategy;
    @Override
    public Result<?> uploadFile(MultipartFile file) {
        String url = fileStrategy.uploadFile(file, BUSINESS_NAME);
        return ResultUtils.success(url);
    }
}