package com.zuofw.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/*
 * @description: IM消息接收信息
 * @author bronya
 * @date: 2024/12/17 14:59
 * @param null
 * @return null
 */
@Data
@Builder
public class IMRecvInfo {

    /**
     * 命令类型 IMCmdType
     */
    private Integer cmd;

    /**
     * 发送方
     */
    private IMUserInfo sender;

    /**
     * 接收方用户列表
     */
    List<IMUserInfo> receivers;

    /**
     * 是否需要回调发送结果
     */
    private Boolean sendResult;

    /**
     * 当前服务名（回调发送结果使用）
     */
    private String serviceName;
    /**
     * 推送消息体
     */
    private Object data;
}


