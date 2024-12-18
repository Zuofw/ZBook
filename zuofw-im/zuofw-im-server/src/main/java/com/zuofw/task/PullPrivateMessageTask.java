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
@RedisMQListener(queue = IMRedisKey.IM_MESSAGE_PRIVATE_QUEUE, batchSize = 10)
public class PullPrivateMessageTask extends AbstractPullMessageTask<IMRecvInfo> {

    @Override
    public void onMessage(IMRecvInfo recvInfo) {
        AbstractMessageProcessor processor = ProcessorFactory.createProcessor(IMCmdType.PRIVATE_MESSAGE);
        processor.process(recvInfo);
    }

}
