package com.zuofw.strategy;

import com.zuofw.domain.vo.Result;
import com.zuofw.model.FileUploadInfo;
import com.zuofw.model.Files;
import com.zuofw.model.UploadUrlsVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件策略工厂
 *
 * @author qingqiu
 * @create 2024/10/9
 * @since 1.0.0
 */
public interface FileStrategy{


    /*
     * @description: 检查Md5是否存在
     * @author bronya
     * @date: 2024/12/19 11:19
 * @param md5
 * @return com.zuofw.domain.vo.Result<com.zuofw.model.FileUploadInfo>
     */
    Result<FileUploadInfo> checkFileByMd5(String md5);

    /*
     * @description: 初始化分片上传
     * @autho qingqiu
     * @date: 2024/10/9 10:03
     * @param fileUploadInfo
     * @return com.zuofw.domain.vo.Result<com.zuofw.model.UploadUrlsVO>
     */
    Result<UploadUrlsVO> initMultipartUpload(FileUploadInfo fileUploadInfo);

    /*
     * @description: 合并分片上传
     * @autho qingqiu
     * @date: 2024/10/9 10:03
     * @param md5
     * @return com.zuofw.domain.vo.Result<java.lang.String>
     */
    Result<String> mergeMultipartUpload(String md5);
    /*
     * @description: 下载文件
     * @autho qingqiu
     * @date: 2024/10/9 10:03
     * @param id
     * @param request
     * @param response
     * @return org.springframework.http.ResponseEntity<byte[]>
     */
    ResponseEntity<byte[]> downloadMultipartFile(Long id, HttpServletRequest request, HttpServletResponse response) throws Exception;

    /*
     * @description: 获取文件列表
     * @autho qingqiu
     * @date: 2024/10/9 10:03
     * @return com.zuofw.domain.vo.Result<java.util.List<com.zuofw.model.Files>>
     */
    Result<List<Files>> getFileList();
}