package com.zuofw.netty;

/**
 * 〈〉
 *
 * @author zuowei
 * @create 2024/12/17
 * @since 1.0.0
 */
public interface IMServer {
    boolean isReady();

    void start();

    void stop();
}