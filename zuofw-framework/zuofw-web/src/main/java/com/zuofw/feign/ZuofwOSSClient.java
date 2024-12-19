package com.zuofw.feign;


import com.zuofw.domain.vo.Result;
import com.zuofw.model.FileUploadInfo;
import com.zuofw.model.Files;
import com.zuofw.model.UploadUrlsVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController("/oss")
public interface ZuofwOSSClient {

    @GetMapping("/multipart/check/{md5}")
    Result<FileUploadInfo> checkFileByMd5(@PathVariable String md5);

    @PostMapping("/multipart/init")
    Result<UploadUrlsVO> initMultiPartUpload(@RequestBody FileUploadInfo fileUploadInfo);

    /**
     * 文件合并（单文件不会合并，仅信息入库）
     */
    @PostMapping("/multipart/merge/{md5}")
    Result<String> mergeMultipartUpload(@PathVariable String md5);
    @GetMapping("/download/{id}")
    ResponseEntity<byte[]> downloadMultipartFile(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws Exception;
    @GetMapping("/list")
    public Result<List<Files>> getFileList();
}
