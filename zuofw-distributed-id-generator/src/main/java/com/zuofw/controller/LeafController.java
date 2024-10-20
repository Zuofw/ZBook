package com.zuofw.controller;


import com.zuofw.core.common.Result;
import com.zuofw.core.common.Status;
import com.zuofw.exception.LeafServerException;
import com.zuofw.exception.NoKeyException;
import com.zuofw.feign.ZuofwDistributedIdClient;
import com.zuofw.service.SegmentService;
import com.zuofw.service.SnowflakeService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class LeafController implements ZuofwDistributedIdClient {

    @Resource
    private SegmentService segmentService;
    @Resource
    private SnowflakeService snowflakeService;

    @Override
    public String getSegmentId(@PathVariable("key") String key) {
        return get(key, segmentService.getId(key));
    }

    @Override
    public String getSnowflakeId(@PathVariable("key") String key) {
        return get(key, snowflakeService.getId(key));
    }

    private String get(@PathVariable("key") String key, Result id) {
        Result result;
        if (key == null || key.isEmpty()) {
            throw new NoKeyException();
        }
        result = id;
        if (result.getStatus().equals(Status.EXCEPTION)) {
            throw new LeafServerException(result.toString());
        }
        return String.valueOf(result.getId());
    }
}