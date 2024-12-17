package com.zuofw.listener;


import cn.hutool.core.collection.CollUtil;

import com.alibaba.fastjson.JSONObject;
import com.zuofw.annotation.IMListener;
import com.zuofw.enums.IMListenerType;
import com.zuofw.model.IMSendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * @description:  消息监听器广播器，
 * 用于广播消息监听器处理消息结果，支
 * 持多个消息监听器处理同一消息结果
 * @author bronya
 * @date 2024/12/17 14:37
 * @version 1.0
 */
@Component
public class MessageListenerMulticaster {

    @Autowired(required = false)
    private List<MessageListener>  messageListeners  = Collections.emptyList();

    public void multicast(IMListenerType listenerType, List<IMSendResult> results){
        if(CollUtil.isEmpty(results)){
            return;
        }
        for(MessageListener listener:messageListeners){
            // 获取监听器上的注解
            IMListener annotation = listener.getClass().getAnnotation(IMListener.class);
            if(annotation!=null && (annotation.type().equals(IMListenerType.ALL) || annotation.type().equals(listenerType))){
                results.forEach(result->{
                    // 将data转回对象类型
                    if(result.getData() instanceof JSONObject){
                        Type superClass = listener.getClass().getGenericInterfaces()[0];
                        Type type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
                        JSONObject data = (JSONObject)result.getData();
                        result.setData(data.toJavaObject(type));
                    }
                });
                // 回调到调用方处理
                listener.process(results);
            }
        }
    }


}
