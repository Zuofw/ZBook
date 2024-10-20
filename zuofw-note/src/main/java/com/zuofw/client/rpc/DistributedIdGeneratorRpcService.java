package com.zuofw.client.rpc;


import com.zuofw.client.IdClient;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @author: 犬小哈
 * @date: 2024/4/13 23:29
 * @version: v1.0.0
 * @description: 用户服务
 **/
@Component
public class DistributedIdGeneratorRpcService {

    @Resource
    private IdClient idClient;

    /**
     * 生成雪花算法 ID
     *
     * @return
     */
    public String getSnowflakeId() {
        return idClient.getSnowflakeId("test");
    }

}
