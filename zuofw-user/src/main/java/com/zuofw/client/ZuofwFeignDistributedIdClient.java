package com.zuofw.client;

import com.zuofw.feign.ZuofwDistributedIdClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

@FeignClient(name = "zuofw-distributed-id-generator")
@LoadBalancerClient(name = "zuofw-distributed-id-generator")
public interface ZuofwFeignDistributedIdClient extends ZuofwDistributedIdClient {
}
