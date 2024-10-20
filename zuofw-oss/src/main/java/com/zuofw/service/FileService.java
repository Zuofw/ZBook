package com.zuofw.service;


import com.zuofw.domain.vo.Result;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @description:  TODO
 * @author zuofw
 * @date 2024/10/19 16:54
 * @version 1.0
 */
public interface FileService {

    /**
     * 上传文件
     * 
     * @param file
     * @return
     */
    Result<?> uploadFile(MultipartFile file);
}
