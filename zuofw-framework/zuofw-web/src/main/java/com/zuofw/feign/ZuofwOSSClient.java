package com.zuofw.feign;


import com.zuofw.domain.vo.Result;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController("/oss")
public interface ZuofwOSSClient {
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result<?> uploadFile(@RequestPart(value = "file") MultipartFile file);
}
