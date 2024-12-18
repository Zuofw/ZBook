package com.zuofw.netty.processor;


import cn.hutool.core.bean.BeanUtil;
import com.zuofw.constant.ChannelAttrKey;
import com.zuofw.contant.IMConstant;
import com.zuofw.contant.IMRedisKey;
import com.zuofw.enums.IMCmdType;
import com.zuofw.model.IMHeartbeatInfo;
import com.zuofw.model.IMSendInfo;
import com.zuofw.mq.redis.RedisMQTemplate;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class HeartbeatProcessor extends AbstractMessageProcessor<IMHeartbeatInfo> {

    private final RedisMQTemplate redisMQTemplate;

    @Override
    public void process(ChannelHandlerContext ctx, IMHeartbeatInfo beatInfo) {
        // 响应ws
        IMSendInfo<Object> sendInfo = new IMSendInfo<>();
        sendInfo.setCmd(IMCmdType.HEART_BEAT.code());
        ctx.channel().writeAndFlush(sendInfo);
        // 设置属性
        AttributeKey<Long> heartBeatAttr = AttributeKey.valueOf(ChannelAttrKey.HEARTBEAT_TIMES);
        Long heartbeatTimes = ctx.channel().attr(heartBeatAttr).get();
        ctx.channel().attr(heartBeatAttr).set(++heartbeatTimes);
        if (heartbeatTimes % 10 == 0) {
            // 每心跳10次，用户在线状态续一次命
            AttributeKey<Long> userIdAttr = AttributeKey.valueOf(ChannelAttrKey.USER_ID);
            Long userId = ctx.channel().attr(userIdAttr).get();
            AttributeKey<Integer> terminalAttr = AttributeKey.valueOf(ChannelAttrKey.TERMINAL_TYPE);
            Integer terminal = ctx.channel().attr(terminalAttr).get();
            String key = String.join(":", IMRedisKey.IM_USER_SERVER_ID, userId.toString(), terminal.toString());
            redisMQTemplate.expire(key, IMConstant.ONLINE_TIMEOUT_SECOND, TimeUnit.SECONDS);
        }
        AttributeKey<Long> userIdAttr = AttributeKey.valueOf(ChannelAttrKey.USER_ID);
        Long userId = ctx.channel().attr(userIdAttr).get();
        log.info("心跳,userId:{},{}",userId,ctx.channel().id().asLongText());
    }

    @Override
    public IMHeartbeatInfo transForm(Object o) {
        HashMap map = (HashMap) o;
        return BeanUtil.fillBeanWithMap(map, new IMHeartbeatInfo(), false);
    }
}
