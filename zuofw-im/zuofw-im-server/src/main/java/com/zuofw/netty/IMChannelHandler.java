package com.zuofw.netty;

import com.zuofw.constant.ChannelAttrKey;
import com.zuofw.contant.IMRedisKey;
import com.zuofw.enums.IMCmdType;
import com.zuofw.model.IMSendInfo;
import com.zuofw.mq.redis.RedisMQTemplate;
import com.zuofw.netty.processor.AbstractMessageProcessor;
import com.zuofw.netty.processor.ProcessorFactory;
import com.zuofw.util.SpringContextHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * 〈〉
 *
 * @author zuowei
 * @create 2024/12/17
 * @since 1.0.0
 */
@Slf4j
public class IMChannelHandler extends SimpleChannelInboundHandler<IMSendInfo> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, IMSendInfo imSendInfo) throws Exception {
        AbstractMessageProcessor processor = ProcessorFactory.createProcessor(IMCmdType.fromCode(imSendInfo.getCmd()));
        processor.process(channelHandlerContext, processor.transForm(imSendInfo.getData()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage());
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info(ctx.channel().id().asLongText() + "连接");
    }
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        AttributeKey<Long> userIdAttr = AttributeKey.valueOf(ChannelAttrKey.USER_ID);
        Long userId = ctx.channel().attr(userIdAttr).get();
        AttributeKey<Integer> terminalAttr = AttributeKey.valueOf(ChannelAttrKey.TERMINAL_TYPE);
        Integer terminal = ctx.channel().attr(terminalAttr).get();
        ChannelHandlerContext context = UserChannelCtxMap.getChannelCtx(userId, terminal);
        // 判断一下，避免异地登录导致的误删
        if (context != null && ctx.channel().id().equals(context.channel().id())) {
            // 移除channel
            UserChannelCtxMap.removeChannelCtx(userId, terminal);
            // 用户下线
            RedisMQTemplate redisTemplate = SpringContextHolder.getBean(RedisMQTemplate.class);
            String key = String.join(":", IMRedisKey.IM_USER_SERVER_ID, userId.toString(), terminal.toString());
            redisTemplate.delete(key);
            log.info("断开连接,userId:{},终端类型:{},{}", userId, terminal, ctx.channel().id().asLongText());
        }
    }
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                // 在规定时间内没有收到客户端的上行数据, 主动断开连接
                AttributeKey<Long> attr = AttributeKey.valueOf("USER_ID");
                Long userId = ctx.channel().attr(attr).get();
                AttributeKey<Integer> terminalAttr = AttributeKey.valueOf(ChannelAttrKey.TERMINAL_TYPE);
                Integer terminal = ctx.channel().attr(terminalAttr).get();
                log.info("心跳超时，即将断开连接,用户id:{},终端类型:{} ", userId, terminal);
                ctx.channel().close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }

    }
}