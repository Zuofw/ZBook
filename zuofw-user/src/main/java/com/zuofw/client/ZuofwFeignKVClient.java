package com.zuofw.client;

import com.zuofw.feign.ZuofwKVClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "zuofw-distributed-id")
@LoadBalancerClient(name = "zuofw-distributed-id")
public interface ZuofwFeignKVClient extends ZuofwKVClient {
}
