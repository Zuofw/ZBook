package com.zuofw.listener;

import com.zuofw.model.IMSendResult;

import java.util.List;

/**
 * @description:  消息监听器接口
 * @author bronya
 * @date 2024/12/17 14:36
 * @version 1.0
 */
public interface MessageListener<T> {

     void process(List<IMSendResult<T>> result);

}
