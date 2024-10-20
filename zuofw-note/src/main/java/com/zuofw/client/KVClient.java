package com.zuofw.client;

import com.zuofw.feign.ZuofwKVClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "zuofw-kv")
@LoadBalancerClient(name = "zuofw-kv")
public interface KVClient extends ZuofwKVClient {
}
