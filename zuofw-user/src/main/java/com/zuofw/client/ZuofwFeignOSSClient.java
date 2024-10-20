package com.zuofw.client;

import com.zuofw.feign.ZuofwOSSClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * 〈〉
 *
 * @author zuowei
 * @create 2024/10/19
 * @since 1.0.0
 */
@FeignClient(name = "zuofw-oss")
@LoadBalancerClient(name = "zuofw-oss")
public interface ZuofwFeignOSSClient extends ZuofwOSSClient {

}