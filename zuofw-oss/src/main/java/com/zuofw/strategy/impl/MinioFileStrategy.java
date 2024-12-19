package com.zuofw.strategy.impl;


import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zuofw.domain.vo.Result;
import com.zuofw.mapper.FilesMapper;
import com.zuofw.model.FileUploadInfo;
import com.zuofw.model.Files;
import com.zuofw.model.UploadUrlsVO;
import com.zuofw.strategy.FileStrategy;
import com.zuofw.util.BeanCopyUtils;
import com.zuofw.util.RedisUtils;
import com.zuofw.util.ResultUtils;
import com.zuofw.utils.MinioUtil;
import io.minio.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 *
 * @author qingqiu
 * @create 2024/10/9
 * @since 1.0.0
 */
@Slf4j
public class MinioFileStrategy implements FileStrategy {

    private static final Integer BUFFER_SIZE = 1024 * 64; // 64KB
    @Resource
    private FilesMapper filesMapper;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private MinioUtil minioUtil;
    @Resource
    private MinioClient minioClient;
    @Resource
    private RedisUtils redisUtil;


    @Override
    public Result<FileUploadInfo> checkFileByMd5(String md5) {
        FileUploadInfo fileUploadInfo = (FileUploadInfo)redisUtils.getCache(md5);
        if (fileUploadInfo != null) {
            List<Integer> listParts = minioUtil.getListParts(fileUploadInfo.getObject(), fileUploadInfo.getUploadId());
            fileUploadInfo.setListParts(listParts);
            return ResultUtils.success(fileUploadInfo);
        }
        log.info("redis中不存在md5: <{}> 查询mysql是否存在", md5);
        Files file = filesMapper.selectOne(new LambdaQueryWrapper<Files>().eq(Files::getMd5, md5));
        if (file != null) {
            log.info("mysql中存在md5: <{}> 的文件 该文件已上传至minio 秒传直接过", md5);
            FileUploadInfo dbFileInfo = BeanCopyUtils.copyBean(file, FileUploadInfo.class);
            return ResultUtils.success(dbFileInfo);
        }
        return ResultUtils.success(null);
    }

    @Override
    public Result<UploadUrlsVO> initMultipartUpload(FileUploadInfo fileUploadInfo) {
        return null;
    }

    @Override
    public Result<String> mergeMultipartUpload(String md5) {
        return null;
    }

    @Override
    public ResponseEntity<byte[]> downloadMultipartFile(Long id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // redis 缓存当前文件信息，避免分片下载时频繁查库
        Files file = null;
        Files redisFile = (Files)redisUtil.getCache(String.valueOf(id));
        if (redisFile == null) {
            Files dbFile = filesMapper.selectById(id);
            if (dbFile == null) {
                return null;
            } else {
                file = dbFile;
                redisUtil.setExpireCache(String.valueOf(id), file, 1, TimeUnit.DAYS);
            }
        } else {
            file = redisFile;
        }

        String range = request.getHeader("Range");
        String fileName = file.getOriginFileName();
        log.info("下载文件的 object <{}>", file.getObject());
        // 获取 bucket 桶中的文件元信息，获取不到会抛出异常
        StatObjectResponse objectResponse = minioUtil.statObject(file.getObject());
        long startByte = 0; // 开始下载位置
        long fileSize = objectResponse.size();
        long endByte = fileSize - 1; // 结束下载位置
        log.info("文件总长度：{}，当前 range：{}", fileSize, range);

        BufferedOutputStream os = null; // buffer 写入流
        GetObjectResponse stream = null; // minio 文件流

        // 存在 range，需要根据前端下载长度进行下载，即分段下载
        // 例如：range=bytes=0-52428800
        if (range != null && range.contains("bytes=") && range.contains("-")) {
            range = range.substring(range.lastIndexOf("=") + 1).trim(); // 0-52428800
            String[] ranges = range.split("-");
            // 判断range的类型
            if (ranges.length == 1) {
                // 类型一：bytes=-2343 后端转换为 0-2343
                if (range.startsWith("-")) endByte = Long.parseLong(ranges[0]);
                // 类型二：bytes=2343- 后端转换为 2343-最后
                if (range.endsWith("-")) startByte = Long.parseLong(ranges[0]);
            } else if (ranges.length == 2) { // 类型三：bytes=22-2343
                startByte = Long.parseLong(ranges[0]);
                endByte = Long.parseLong(ranges[1]);
            }
        }

        // 要下载的长度
        // 确保返回的 contentLength 不会超过文件的实际剩余大小
        long contentLength = Math.min(endByte - startByte + 1, fileSize - startByte);
        // 文件类型
        String contentType = request.getServletContext().getMimeType(fileName);

        // 解决下载文件时文件名乱码问题
        byte[] fileNameBytes = fileName.getBytes(StandardCharsets.UTF_8);
        fileName = new String(fileNameBytes, 0, fileNameBytes.length, StandardCharsets.ISO_8859_1);

        // 响应头设置---------------------------------------------------------------------------------------------
        // 断点续传，获取部分字节内容：
        response.setHeader("Accept-Ranges", "bytes");
        // http状态码要为206：表示获取部分内容,SC_PARTIAL_CONTENT,若部分浏览器不支持，改成 SC_OK
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        response.setContentType(contentType);
        response.setHeader("Last-Modified", objectResponse.lastModified().toString());
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setHeader("Content-Length", String.valueOf(contentLength));
        // Content-Range，格式为：[要下载的开始位置]-[结束位置]/[文件总大小]
        response.setHeader("Content-Range", "bytes " + startByte + "-" + endByte + "/" + objectResponse.size());
        response.setHeader("ETag", "\"".concat(objectResponse.etag()).concat("\""));
        response.setContentType("application/octet-stream;charset=UTF-8");

        try {
            // 获取文件流
            stream = minioUtil.getObject(objectResponse.object(), startByte, contentLength);
            os = new BufferedOutputStream(response.getOutputStream());
            // 将读取的文件写入到 OutputStream
            byte[] bytes = new byte[BUFFER_SIZE];
            long bytesWritten = 0;
            int bytesRead = -1;
            while ((bytesRead = stream.read(bytes)) != -1) {
                if (bytesWritten + bytesRead >= contentLength) {
                    os.write(bytes, 0, (int)(contentLength - bytesWritten));
                    break;
                } else {
                    os.write(bytes, 0, bytesRead);
                    bytesWritten += bytesRead;
                }
            }
            os.flush();
            response.flushBuffer();
            // 返回对应http状态
            return new ResponseEntity<>(bytes, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) os.close();
            if (stream != null) stream.close();
        }
        return null;
    }

    @Override
    public Result<List<Files>> getFileList() {
        List<Files> filesList = filesMapper.selectList(null);
        return ResultUtils.success(filesList);
    }

}