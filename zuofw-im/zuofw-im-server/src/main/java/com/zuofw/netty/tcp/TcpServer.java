package com.zuofw.netty.tcp;

import com.zuofw.netty.IMChannelHandler;
import com.zuofw.netty.IMServer;
import com.zuofw.netty.tcp.code.MessageProtocolDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 〈〉
 *
 * @author zuowei
 * @create 2024/12/17
 * @since 1.0.0
 */
@Slf4j
@Component
@Configuration
public class TcpServer implements IMServer {

    // 用于标识服务器是否已经启动，volatile保证了多线程之间的可见性
    private volatile boolean ready = false;
    @Value("${tcp.port}")
    private int port;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;
    @Override
    public boolean isReady() {
        return ready;
    }

    @Override
    public void start() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bossGroup = new NioEventLoopGroup();
        workGroup = new NioEventLoopGroup();
        // 设置为主从线程模型
        bootstrap.group(bossGroup, workGroup)
                // NIO
                .channel(NioServerSocketChannel.class)
                // 设置ChannelPipeline，也就是业务职责链
                .childHandler(new ChannelInitializer<>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        // 心跳机制
                        pipeline.addLast(new IdleStateHandler(120, 0, 0))
                                .addLast("encode", new MessageProtocolDecoder())
                                .addLast("decode", new MessageProtocolDecoder())
                                .addLast("handler", new IMChannelHandler());
                    }
                });
    }

    @Override
    public void stop() {

    }
}