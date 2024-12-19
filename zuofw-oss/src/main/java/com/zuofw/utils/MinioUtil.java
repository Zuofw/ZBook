package com.zuofw.utils;

import cn.hutool.core.util.IdUtil;
import com.google.common.collect.HashMultimap;
import com.zuofw.config.CustomMinioClient;
import com.zuofw.config.MinioConfigInfo;
import com.zuofw.model.FileUploadInfo;
import com.zuofw.model.UploadUrlsVO;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Part;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MinioUtil {

    private CustomMinioClient customMinioClient;

    @Resource
    private MinioConfigInfo minioConfigInfo;

    // spring自动注入会失败
    @PostConstruct
    public void init() {
        MinioAsyncClient minioClient = MinioAsyncClient.builder()
                .endpoint(minioConfigInfo.getEndpoint())
                .credentials(minioConfigInfo.getAccessKey(), minioConfigInfo.getSecretKey())
                .build();
        customMinioClient = new CustomMinioClient(minioClient);
    }

    /**
     * 获取 Minio 中已经上传的分片文件
     * @param object 文件名称
     * @param uploadId 上传的文件id（由 minio 生成）
     * @return List<Integer>
     */
    @SneakyThrows
    public List<Integer> getListParts(String object, String uploadId) {
        List<Part> parts = getParts(object, uploadId);
        return parts.stream()
                .map(Part::partNumber)
                .collect(Collectors.toList());
    }

    /**
     * 单文件签名上传
     * @param object 文件名称（uuid 格式）
     * @return UploadUrlsVO
     */
    public UploadUrlsVO getUploadObjectUrl(String contentType, String object) {
        try {
            log.info("<{}> 开始单文件上传<minio>", object);
            UploadUrlsVO urlsVO = new UploadUrlsVO();
            List<String> urlList = new ArrayList<>();
            // 主要是针对图片，若需要通过浏览器直接查看，而不是下载，需要指定对应的 content-type
            HashMultimap<String, String> headers = HashMultimap.create();
            if (contentType == null || contentType.equals("")) {
                contentType = "application/octet-stream";
            }
            headers.put("Content-Type", contentType);

            String uploadId = IdUtil.simpleUUID();
            Map<String, String> reqParams = new HashMap<>();
            reqParams.put("uploadId", uploadId);

            String url = customMinioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.PUT)
                    .bucket(minioConfigInfo.getBucket())
                    .object(object)
                    .extraHeaders(headers)
                    .extraQueryParams(reqParams)
                    .expiry(minioConfigInfo.getExpiry(), TimeUnit.DAYS)
                    .build());
            urlList.add(url);
            urlsVO.setUploadId(uploadId).setUrls(urlList);
            return urlsVO;
        } catch (Exception e) {
            log.error("单文件上传失败: {}", e.getMessage());
            throw new RuntimeException("单文件上传失败");
        }
    }

    /**
     * 初始化分片上传
     * @param fileUploadInfo 前端传入的文件信息
     * @param object object
     * @return UploadUrlsVO
     */
    public UploadUrlsVO initMultiPartUpload(FileUploadInfo fileUploadInfo, String object) {
        Integer chunkCount = fileUploadInfo.getChunkCount();
        String contentType = fileUploadInfo.getContentType();
        String uploadId = fileUploadInfo.getUploadId();

        log.info("文件<{}> - 分片<{}> 初始化分片上传数据 请求头 {}", object, chunkCount, contentType);
        UploadUrlsVO urlsVO = new UploadUrlsVO();
        try {
            HashMultimap<String, String> headers = HashMultimap.create();
            if (contentType == null || contentType.equals("")) {
                contentType = "application/octet-stream";
            }
            headers.put("Content-Type", contentType);

            // 如果初始化时有 uploadId，说明是断点续传，不能重新生成 uploadId
            if (fileUploadInfo.getUploadId() == null || fileUploadInfo.getUploadId().equals("")) {
                uploadId = customMinioClient.initMultiPartUpload(minioConfigInfo.getBucket(), null, object, headers, null);
            }
            urlsVO.setUploadId(uploadId);

            List<String> partList = new ArrayList<>();
            Map<String, String> reqParams = new HashMap<>();
            reqParams.put("uploadId", uploadId);
            for (int i = 1; i <= chunkCount; i++) {
                reqParams.put("partNumber", String.valueOf(i));
                String uploadUrl = customMinioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                        .method(Method.PUT)
                        .bucket(minioConfigInfo.getBucket())
                        .object(object)
                        .expiry(1, TimeUnit.DAYS)
                        .extraQueryParams(reqParams)
                        .build());
                partList.add(uploadUrl);
            }

            log.info("文件初始化分片成功");
            urlsVO.setUrls(partList);
            return urlsVO;
        } catch (Exception e) {
            log.error("初始化分片上传失败: {}", e.getMessage());
            // 返回 文件上传失败
            throw new RuntimeException("初始化分片上传失败");
        }
    }

    /**
     * 合并文件
     * @param object object
     * @param uploadId uploadUd
     */
    @SneakyThrows
    public boolean mergeMultipartUpload(String object, String uploadId) {
        log.info("通过 <{}-{}-{}> 合并<分片上传>数据", object, uploadId, minioConfigInfo.getBucket());
        // 获取所有分片
        List<Part> partsList = getParts(object, uploadId);
        Part[] parts = new Part[partsList.size()];
        int partNumber = 1;
        for (Part part : partsList) {
            parts[partNumber - 1] = new Part(partNumber, part.etag());
            partNumber++;
        }
        // 合并分片
        customMinioClient.mergeMultipartUpload(minioConfigInfo.getBucket(), null, object, uploadId, parts, null, null);
        return true;
    }

    /**
     * 获取文件内容和元信息，该文件不存在会抛异常
     * @param object object
     * @return StatObjectResponse
     */
    @SneakyThrows
    public StatObjectResponse statObject(String object) {
        return customMinioClient.statObject(StatObjectArgs.builder()
                .bucket(minioConfigInfo.getBucket())
                .object(object)
                .build())
                .get();
    }

    @SneakyThrows
    public GetObjectResponse getObject(String object, Long offset, Long contentLength) {
        return customMinioClient.getObject(GetObjectArgs.builder()
                .bucket(minioConfigInfo.getBucket())
                .object(object)
                .offset(offset)
                .length(contentLength)
                .build())
                .get();
    }


    @NotNull
    private  List<Part> getParts(String object, String uploadId) throws Exception {
        int partNumberMarker = 0;
        boolean isTruncated = true;
        List<Part> parts = new ArrayList<>();
        while(isTruncated){
            ListPartsResponse partResult = customMinioClient.listMultipart(minioConfigInfo.getBucket(), null, object, 1000, partNumberMarker, uploadId, null, null);
            parts.addAll(partResult.result().partList());
            // 检查是否还有更多分片
            isTruncated = partResult.result().isTruncated();
            if (isTruncated) {
                // 更新partNumberMarker以获取下一页的分片数据
                partNumberMarker = partResult.result().nextPartNumberMarker();
            }
        }
        return parts;
    }

}
