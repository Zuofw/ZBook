package com.zuofw.constant;

/**
 * 通道属性key,AttributeKey是netty的属性key,用于给Channel设置属性
 */
public final class ChannelAttrKey {

    private ChannelAttrKey() {
    }

    /**
     * 用户ID
     */
    public static final String USER_ID = "USER_ID";
    /**
     * 终端类型
     */
    public static final String TERMINAL_TYPE = "TERMINAL_TYPE";
    /**
     * 心跳次数
     */
    public static final String HEARTBEAT_TIMES = "HEARTBEAT_TIMES";

}
