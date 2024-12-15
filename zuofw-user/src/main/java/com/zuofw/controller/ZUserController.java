package com.zuofw.controller;

import com.zuofw.domain.dto.UpdateUserInfoReqVO;
import com.zuofw.domain.vo.Result;
import com.zuofw.service.ZUserService;
import com.zuofw.util.ResultUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈〉
 *
 * @author zuowei
 * @create 2024/12/13
 * @since 1.0.0
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class ZUserController {

    @Resource
    private ZUserService userService;

    /*
     * @description:  更新用户信息 consumes是指定请求的Content-Type类型，如果不指定，那么默认是application/json
     * @author bronya
     * @date: 2024/12/13 14:43
     * @param id
     */
    @PostMapping(value = "/update" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<?> update(@Validated UpdateUserInfoReqVO updateUserInfoReqVO) {
        userService.update(updateUserInfoReqVO);
        return ResultUtils.success("更新成功");
    }

    @PostMapping(value = "/add" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    private Result<?> add(@Validated UpdateUserInfoReqVO updateUserInfoReqVO) {
        userService.add(updateUserInfoReqVO);
        return ResultUtils.success("添加成功");
    }

}