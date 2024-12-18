package com.zuofw.netty.processor;

import com.zuofw.enums.IMCmdType;
import com.zuofw.util.SpringContextHolder;

public class ProcessorFactory {

    public static AbstractMessageProcessor createProcessor(IMCmdType cmd) {
        return switch (cmd) {
            case HEART_BEAT -> SpringContextHolder.getApplicationContext().getBean(HeartbeatProcessor.class);
            case PRIVATE_MESSAGE->SpringContextHolder.getApplicationContext().getBean(PrivateMessageProcessor.class);
            case GROUP_MESSAGE->SpringContextHolder.getApplicationContext().getBean(GroupMessageProcessor.class);
            case SYSTEM_MESSAGE->SpringContextHolder.getApplicationContext().getBean(SystemMessageProcessor.class);
            default -> null;
        };
    }

}
