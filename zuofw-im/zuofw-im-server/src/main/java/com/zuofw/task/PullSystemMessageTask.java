package com.zuofw.task;

import com.zuofw.contant.IMRedisKey;
import com.zuofw.enums.IMCmdType;
import com.zuofw.model.IMRecvInfo;
import com.zuofw.mq.redis.RedisMQListener;
import com.zuofw.netty.processor.AbstractMessageProcessor;
import com.zuofw.netty.processor.ProcessorFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: Blue
 * @date: 2024-07-16
 * @version: 1.0
 */
@Slf4j
@Component
@RedisMQListener(queue = IMRedisKey.IM_MESSAGE_SYSTEM_QUEUE,batchSize = 10)
public class PullSystemMessageTask extends AbstractPullMessageTask<IMRecvInfo> {

    @Override
    public void onMessage(IMRecvInfo recvInfo) {
        AbstractMessageProcessor processor = ProcessorFactory.createProcessor(IMCmdType.SYSTEM_MESSAGE);
        processor.process(recvInfo);
    }

}
