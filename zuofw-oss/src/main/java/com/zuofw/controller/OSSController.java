package com.zuofw.controller;

import com.zuofw.domain.vo.Result;
import com.zuofw.feign.ZuofwOSSClient;
import com.zuofw.service.FileService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 〈〉
 *
 * @author zuowei
 * @create 2024/10/19
 * @since 1.0.0
 */
@RestController
public class OSSController implements ZuofwOSSClient {

    @Resource
    private FileService fileService;
    @Override
    public Result<?> uploadFile(MultipartFile file) {
        return fileService.uploadFile(file);
    }

}