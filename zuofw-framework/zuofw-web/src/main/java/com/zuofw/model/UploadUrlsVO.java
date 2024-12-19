package com.zuofw.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 返回文件生成的分片上传地址
 */
@Data
// 这是 Lombok 的注解，用来自动生成 getter/setter 方法
@Accessors(chain = true) // 这是 Lombok 的注解，用来自动生成链式调用方法
public class UploadUrlsVO {
    private String uploadId;
    private List<String> urls;
}