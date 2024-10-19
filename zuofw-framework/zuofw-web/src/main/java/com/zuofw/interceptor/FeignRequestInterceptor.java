package com.zuofw.interceptor;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

/**
 * 〈〉
 *
 * @author zuowei
 * @create 2024/10/19
 * @since 1.0.0
 */
@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 逻辑就是拦截Authorization，获取usrId，然后设置到header中,这里懒得写了
//        RequestTemplate authorization = requestTemplate.header("Authorization");
//        if(Objects.nonNull(authorization)) {
//            requestTemplate.header("Authorization", String.valueOf(authorization));
//            log.info("FeignRequestInterceptor userId:{}", userId);
//        }
    }
}