package com.zuofw.netty.processor;

import cn.hutool.core.util.StrUtil;
import com.zuofw.contant.IMRedisKey;
import com.zuofw.enums.IMCmdType;
import com.zuofw.enums.IMSendCode;
import com.zuofw.model.IMRecvInfo;
import com.zuofw.model.IMSendInfo;
import com.zuofw.model.IMSendResult;
import com.zuofw.model.IMUserInfo;
import com.zuofw.mq.redis.RedisMQTemplate;
import com.zuofw.netty.UserChannelCtxMap;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;


@Slf4j
@Component
@RequiredArgsConstructor
public class GroupMessageProcessor extends AbstractMessageProcessor<IMRecvInfo> {

    private final RedisMQTemplate redisMQTemplate;

    @Override
    public void process(IMRecvInfo recvInfo) {
        IMUserInfo sender = recvInfo.getSender();
        List<IMUserInfo> receivers = recvInfo.getReceivers();
        log.info("接收到群消息，发送者:{},接收用户数量:{}，内容:{}", sender.getId(), receivers.size(), recvInfo.getData());
        for (IMUserInfo receiver : receivers) {
            try {
                ChannelHandlerContext channelCtx = UserChannelCtxMap.getChannelCtx(receiver.getId(), receiver.getTerminal());
                if (!Objects.isNull(channelCtx)) {
                    // 推送消息到用户
                    IMSendInfo<Object> sendInfo = new IMSendInfo<>();
                    sendInfo.setCmd(IMCmdType.GROUP_MESSAGE.code());
                    sendInfo.setData(recvInfo.getData());
                    channelCtx.channel().writeAndFlush(sendInfo);
                    // 消息发送成功确认
                    sendResult(recvInfo, receiver, IMSendCode.SUCCESS);

                } else {
                    // 消息发送成功确认
                    sendResult(recvInfo, receiver, IMSendCode.NOT_FIND_CHANNEL);
                    log.error("未找到channel,发送者:{},接收id:{}，内容:{}", sender.getId(), receiver.getId(), recvInfo.getData());
                }
            } catch (Exception e) {
                // 消息发送失败确认
                sendResult(recvInfo, receiver, IMSendCode.UNKONW_ERROR);
                log.error("发送消息异常,发送者:{},接收id:{}，内容:{}", sender.getId(), receiver.getId(), recvInfo.getData());
            }
        }
    }


    private void sendResult(IMRecvInfo recvInfo, IMUserInfo receiver, IMSendCode sendCode) {
        if (recvInfo.getSendResult()) {
            IMSendResult<Object> result = new IMSendResult<>();
            result.setSender(recvInfo.getSender());
            result.setReceiver(receiver);
            result.setCode(sendCode.code());
            result.setData(recvInfo.getData());
            // 推送到结果队列
            String key = StrUtil.join(":", IMRedisKey.IM_RESULT_GROUP_QUEUE,recvInfo.getServiceName());
            redisMQTemplate.opsForList().rightPush(key, result);
        }
    }
}