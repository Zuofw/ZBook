package com.zuofw.model;


import com.zuofw.enums.IMTerminalType;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * @description:  IM系统消息
 * @author bronya
 * @date 2024/12/17 14:45
 * @version 1.0
 */
@Data
public class IMSystemMessage<T> {


    /**
     * 接收者id列表，为空表示向所有在线用户广播
     */
    private List<Long> recvIds = new LinkedList<>();

    /**
     * 接收者终端类型,默认全部
     */
    private List<Integer> recvTerminals = IMTerminalType.codes();

    /**
     * 是否需要回推发送结果,默认true
     */
    private Boolean sendResult = true;

    /**
     * 消息内容
     */
    private T data;


}
