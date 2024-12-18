package com.zuofw.netty.tcp.code;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuofw.model.IMSendInfo;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
// ReplayingDecoder是ByteToMessageDecoder的子类，不需要判断数据是否足够读取，ReplayingDecoder会自动判断，如果存在数据不足则停止解码，等数据足够时再继续解码
public class MessageProtocolDecoder extends ReplayingDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 少于4个字节，直接返回
        if (byteBuf.readableBytes() < 4) {
            return;
        }
        // 获取到包的长度
        long length = byteBuf.readLong();
        // 转成IMSendInfo
        ByteBuf contentBuf = byteBuf.readBytes((int) length);
        String content = contentBuf.toString(CharsetUtil.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        IMSendInfo sendInfo = objectMapper.readValue(content, IMSendInfo.class);
        list.add(sendInfo);
    }
}
