package com.zuofw.controller;

import com.zuofw.domain.vo.Result;
import com.zuofw.feign.ZuofwOSSClient;
import com.zuofw.holder.LoginUserContextHolder;
import com.zuofw.model.FileUploadInfo;
import com.zuofw.model.Files;
import com.zuofw.model.UploadUrlsVO;
import com.zuofw.service.FileService;
import com.zuofw.strategy.FileStrategy;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 〈〉
 *
 * @author qingqiu
 * @create 2024/10/19
 * @since 1.0.0
 */
@RestController
@Slf4j
public class OSSController implements ZuofwOSSClient {

    @Resource
    private FileService fileService;
    // 因为这里只使用MinIO来进行展示，所以这个地方就不再使用策略模式了
    @Resource
    private FileStrategy fileStrategy;

    @Override
    public Result<FileUploadInfo> checkFileByMd5(String md5) {
        return fileStrategy.checkFileByMd5(md5);
    }

    @Override
    public Result<UploadUrlsVO> initMultiPartUpload(FileUploadInfo fileUploadInfo) {
        return fileStrategy.initMultipartUpload(fileUploadInfo);
    }

    @Override
    public Result<String> mergeMultipartUpload(String md5) {
        return fileStrategy.mergeMultipartUpload(md5);
    }

    @Override
    public ResponseEntity<byte[]> downloadMultipartFile(Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return fileStrategy.downloadMultipartFile(id, request, response);
    }

    @Override
    public Result<List<Files>> getFileList() {
        return fileStrategy.getFileList();
    }

}