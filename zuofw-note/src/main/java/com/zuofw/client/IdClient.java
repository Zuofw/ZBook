package com.zuofw.client;

import com.zuofw.feign.ZuofwDistributedIdClient;
import com.zuofw.feign.ZuofwKVClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "zuofw-distributed-id-generator")
@LoadBalancerClient(name = "zuofw-distributed-id-generator")
public interface IdClient extends ZuofwDistributedIdClient {
}
