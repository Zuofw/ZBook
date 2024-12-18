package com.zuofw.task;


import com.zuofw.contant.IMRedisKey;
import com.zuofw.enums.IMCmdType;
import com.zuofw.model.IMRecvInfo;
import com.zuofw.mq.redis.RedisMQListener;
import com.zuofw.netty.processor.AbstractMessageProcessor;
import com.zuofw.netty.processor.ProcessorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@RedisMQListener(queue = IMRedisKey.IM_MESSAGE_GROUP_QUEUE, batchSize = 10)
public class PullGroupMessageTask extends AbstractPullMessageTask<IMRecvInfo> {

    @Override
    public void onMessage(IMRecvInfo recvInfo) {
        AbstractMessageProcessor processor = ProcessorFactory.createProcessor(IMCmdType.GROUP_MESSAGE);
        processor.process(recvInfo);
    }

}
