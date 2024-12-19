package com.zuofw.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 文件上传信息，查询 redis 后的返回信息
 */
@Data
@Accessors(chain = true)
public class FileUploadInfo {
    @NotBlank(message = "md5 不能为空")
    private String md5;

    @NotBlank(message = "uploadId 不能为空")
    private String uploadId;

    @NotBlank(message = "文件名不能为空")
    private String originFileName;

    // 仅秒传会有值
    private String url;
    // 后端使用
    private String object;

    private String type;

    @NotNull(message = "文件大小不能为空")
    private Long size;

    @NotNull(message = "分片数量不能为空")
    private Integer chunkCount;

    @NotNull(message = "分片大小不能为空")
    private Long chunkSize;

    private String contentType;

    // listParts 从 1 开始，前端需要上传的分片索引+1
    private List<Integer> listParts;

}