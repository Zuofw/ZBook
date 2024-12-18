package com.zuofw.netty.tcp.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zuofw.model.IMSendInfo;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;

public class MessageProtocolEncoder extends MessageToByteEncoder<IMSendInfo> {

    // 前8个字节表示长度，后面是内容
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, IMSendInfo sendInfo, ByteBuf byteBuf) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String content = objectMapper.writeValueAsString(sendInfo);
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        // 写入长度
        byteBuf.writeLong(bytes.length);
        // 写入命令体
        byteBuf.writeBytes(bytes);
    }

}