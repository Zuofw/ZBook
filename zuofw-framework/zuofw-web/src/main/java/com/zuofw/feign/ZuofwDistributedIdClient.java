package com.zuofw.feign;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈〉
 *
 * @author qingqiu
 * @create 2024/10/20
 * @since 1.0.0
 */
@RestController("/id")
public interface ZuofwDistributedIdClient {
    @RequestMapping(value = "/segment/get/{key}")
    String getSegmentId(@PathVariable("key") String key);

    @RequestMapping(value = "/snowflake/get/{key}")
    String getSnowflakeId(@PathVariable("key") String key);
}