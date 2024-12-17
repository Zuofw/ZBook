package com.zuofw.sender;

import cn.hutool.core.collection.CollUtil;
import com.zuofw.contant.IMRedisKey;
import com.zuofw.enums.IMCmdType;
import com.zuofw.enums.IMListenerType;
import com.zuofw.enums.IMSendCode;
import com.zuofw.enums.IMTerminalType;
import com.zuofw.listener.MessageListenerMulticaster;
import com.zuofw.model.*;
import com.zuofw.mq.redis.RedisMQTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * IM消息发送器
 *
 * @author qingqiu
 * @create 2024/12/17
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor // 生成一个包含常量，和标识了NotNull的字段的构造函数
public class IMSender {
    // 可替换成其他消息队列
    @Autowired
    private RedisMQTemplate redisMQTemplate;

    @Value("${spring.application.name}")
    private String applicationName;

    private final MessageListenerMulticaster listenerMulticaster;

    /*
     * @description: 发送系统消息
     * @author bronya
     * @date: 2024/12/17 14:43
     * @param message
     */
    public <T> void sendSystemMessage(IMSystemMessage<T> message) {
        // 根据群聊每个成员所连接的server进行分组
        Map<String, IMUserInfo> sendMap = new HashMap<>();
        for (Integer terminal : message.getRecvTerminals()) {
           message.getRecvIds().forEach(id -> {
               String key = String.join(":", IMRedisKey.IM_USER_SERVER_ID, id.toString(), terminal.toString());
               sendMap.put(key,new IMUserInfo(id, terminal));
           });
        }
        // 批量拉取serverId
        List<Object> serverIds = redisMQTemplate.opsForValue().multiGet(sendMap.keySet());
        // 格式 Map<serverId, List<接收方>>
        Map<Integer, List<IMUserInfo>> serverMap = new HashMap<>();
        // 离线用户 没有serverId
        List<IMUserInfo> offLinesUsers = new LinkedList<>();
        int idx = 0;
        for (Map.Entry<String,IMUserInfo> entry : sendMap.entrySet()) {
            Integer serverId = (Integer) serverIds.get(idx++);
            if (!Objects.isNull(serverId)) {
                List<IMUserInfo> list = serverMap.computeIfAbsent(serverId, k -> new LinkedList<>());
                list.add(entry.getValue());
            } else {
                offLinesUsers.add(entry.getValue());
            }
        }

        // 群发消息 == 逐个发送消息

        serverMap.entrySet().forEach(entry -> {
            IMRecvInfo recvInfo = IMRecvInfo.builder()
                    .cmd(IMCmdType.SYSTEM_MESSAGE.code())
                    .receivers(new LinkedList<>(entry.getValue()))
                    .serviceName(applicationName)
                    .sendResult(message.getSendResult())
                    .data(message.getData())
                    .build();
            String key = String.join(":", IMRedisKey.IM_MESSAGE_SYSTEM_QUEUE, entry.getKey().toString());
            // 发送消息,使用list，为了保证消息的有序性
            redisMQTemplate.opsForValue().set(key, recvInfo);
        });
        // 处理离线用户的消息
        if (message.getSendResult() && !offLinesUsers.isEmpty()) {
            List<IMSendResult> results = new LinkedList<>();
            offLinesUsers.forEach(user->{
                IMSendResult result = IMSendResult.builder()
                        .receiver(user)
                        .code(IMSendCode.NOT_ONLINE.code())
                        .data(message.getData())
                        .build();
                results.add(result);
            });
            listenerMulticaster.multicast(IMListenerType.SYSTEM_MESSAGE,results);
        }
   }
   /*
    * @description: 发送私聊消息
    * @author bronya
    * @date: 2024/12/17 15:52
    * @param message
    */
    public <T> void sendPrivateMessage(IMPrivateMessage<T> message) {
        // 发送结果
        List<IMSendResult> results = new LinkedList<>();
        if (!Objects.isNull(message.getRecvId())) {
            // 判断用户是否在线
            message.getRecvTerminals().forEach(terminal->{
                String key = String.join(":", IMRedisKey.IM_USER_SERVER_ID, message.getRecvId().toString(), terminal.toString());
                Integer serverId = (Integer) redisMQTemplate.opsForValue().get(key);
                // 如果在线就将消息发送到redis中，等待拉取
                if (!Objects.isNull(serverId)) {
                    String sendKey = String.join(":", IMRedisKey.IM_MESSAGE_PRIVATE_QUEUE, serverId.toString());
                    IMRecvInfo recvInfo = IMRecvInfo.builder()
                            .cmd(IMCmdType.PRIVATE_MESSAGE.code())
                            .receivers(Collections.singletonList(new IMUserInfo(message.getRecvId(), terminal)))
                            .serviceName(applicationName)
                            .sendResult(message.getSendResult())
                            .data(message.getData())
                            .build();
                    redisMQTemplate.opsForValue().set(sendKey, recvInfo);
                } else {
                    // 不在线也是需要给回调结果的
                    IMSendResult result = IMSendResult.builder()
                            .receiver(new IMUserInfo(message.getRecvId(), terminal))
                            .code(IMSendCode.NOT_ONLINE.code())
                            .data(message.getData())
                            .build();
                    results.add(result);
                }
            });
        }
        // 同步自己的其他终端
        if (message.getSendToSelf()) {
            IMTerminalType.codes().forEach(terminal->{
               if (!message.getSender().getTerminal().equals(terminal)) {
                   String key = String.join(":", IMRedisKey.IM_USER_SERVER_ID, message.getSender().getId().toString(), terminal.toString());
                   Integer serverId = (Integer) redisMQTemplate.opsForValue().get(key);
                   if (serverId != null) {
                       String sendKey = String.join(":", IMRedisKey.IM_MESSAGE_PRIVATE_QUEUE, serverId.toString());
                       IMRecvInfo recvInfo = IMRecvInfo.builder()
                               .sendResult(false)
                               .cmd(IMCmdType.PRIVATE_MESSAGE.code())
                               .receivers(Collections.singletonList(new IMUserInfo(message.getSender().getId(), terminal)))
                               .serviceName(applicationName)
                               .data(message.getData())
                               .build();
                       redisMQTemplate.opsForValue().set(sendKey, recvInfo);
                   }
               }

            });
        }
        // 回调发送结果
        if (message.getSendResult() && !results.isEmpty()) {
            listenerMulticaster.multicast(IMListenerType.PRIVATE_MESSAGE, results);
        }
    }

    /*
     * @description: 发送群聊消息
     * @author bronya
     * @date: 2024/12/17 16:18
     * @param message
     */
    public  <T> void sendGroupMessage(IMGroupMessage<T> message) {
        // 一个成员可能同时登录多个终端，所以需要根据终端进行分组
        Map<String,IMUserInfo> sendMap = new HashMap<>();
        message.getRecvTerminals().forEach(terminal->{
            message.getRecvIds().forEach(id->{
                String key = String.join(":", IMRedisKey.IM_USER_SERVER_ID, id.toString(), terminal.toString());
                sendMap.put(key,new IMUserInfo(id,terminal));
            });
        });
        List<Object> serverIds = redisMQTemplate.opsForValue().multiGet(sendMap.keySet());
        // 格式 Map<serverId, List<接收方>>
        Map<Integer, List<IMUserInfo>> serverMap = new HashMap<>();
        // 离线用户 没有serverId
        List<IMUserInfo> offLinesUsers = new LinkedList<>();
        int idx = 0;
        for (Map.Entry<String, IMUserInfo> entry : sendMap.entrySet()) {
            Integer serverId = (Integer) serverIds.get(idx++);
            if (!Objects.isNull(serverId)) {
                List<IMUserInfo> list = serverMap.computeIfAbsent(serverId, k -> new LinkedList<>());
                list.add(entry.getValue());
            } else {
                offLinesUsers.add(entry.getValue());
            }
        }
        // 逐个发送
        for (Map.Entry<Integer, List<IMUserInfo>> entry : serverMap.entrySet()){
            IMRecvInfo recvInfo = IMRecvInfo.builder()
                    .cmd(IMCmdType.GROUP_MESSAGE.code())
                    .receivers(new LinkedList<>(entry.getValue()))
                    .serviceName(applicationName)
                    .sender(message.getSender())
                    .sendResult(message.getSendResult())
                    .data(message.getData())
                    .build();
            String key = String.join(":", IMRedisKey.IM_MESSAGE_GROUP_QUEUE, entry.getKey().toString());
            redisMQTemplate.opsForValue().set(key,recvInfo);
        }
        // 推送给自己的其他终端
        if (message.getSendToSelf()) {
            IMTerminalType.codes().forEach(terminal->{
                if (!terminal.equals(message.getSender().getTerminal())) {
                    String key = String.join(":", IMRedisKey.IM_USER_SERVER_ID, message.getSender().getId().toString(), terminal.toString());
                    Integer serverId = (Integer) redisMQTemplate.opsForValue().get(key);
                    if (!Objects.isNull(serverId)) {
                        IMRecvInfo recvInfo = IMRecvInfo.builder()
                                .sender(message.getSender())
                                .sendResult(false)
                                .cmd(IMCmdType.GROUP_MESSAGE.code())
                                .receivers(Collections.singletonList(new IMUserInfo(message.getSender().getId(), terminal)))
                                .serviceName(applicationName)
                                .data(message.getData())
                                .sendResult(false)
                                .build();
                        String sendKey = String.join(":", IMRedisKey.IM_MESSAGE_GROUP_QUEUE, serverId.toString());
                        redisMQTemplate.opsForList().rightPush(sendKey,recvInfo);
                    }
                }
            });
        }
        // 处理离线用户的消息
        if (message.getSendResult() && !offLinesUsers.isEmpty()) {
            List<IMSendResult> results = new LinkedList<>();
            offLinesUsers.forEach(user->{
                IMSendResult result = IMSendResult.builder()
                        .receiver(user)
                        .code(IMSendCode.NOT_ONLINE.code())
                        .data(message.getData())
                        .build();
                results.add(result);
            });
            listenerMulticaster.multicast(IMListenerType.GROUP_MESSAGE,results);
        }

    }

    /*
     * @description: 获取在线用户的终端类型列表
     * @author bronya
     * @date: 2024/12/17 16:30
     * @param userIds
     * @return java.util.Map<java.lang.Long,java.util.List<com.zuofw.enums.IMTerminalType>>
     */
    public Map<Long,List<IMTerminalType>> getOnlineTerminal(List<Long> userIds){
        if(CollUtil.isEmpty(userIds)){
            return Collections.emptyMap();
        }
        // 把所有用户的key都存起来
        Map<String,IMUserInfo> userMap = new HashMap<>();
        for(Long id:userIds){
            for (Integer terminal : IMTerminalType.codes()) {
                String key = String.join(":", IMRedisKey.IM_USER_SERVER_ID, id.toString(), terminal.toString());
                userMap.put(key,new IMUserInfo(id,terminal));
            }
        }
        // 批量拉取
        List<Object> serverIds = redisMQTemplate.opsForValue().multiGet(userMap.keySet());
        int idx = 0;
        Map<Long,List<IMTerminalType>> onlineMap = new HashMap<>();
        for (Map.Entry<String,IMUserInfo> entry : userMap.entrySet()) {
            // serverid有值表示用户在线
            if(serverIds.get(idx++) != null){
                IMUserInfo userInfo = entry.getValue();
                List<IMTerminalType> terminals = onlineMap.computeIfAbsent(userInfo.getId(), o -> new LinkedList<>());
                terminals.add(IMTerminalType.fromCode(userInfo.getTerminal()));
            }
        }
        // 去重并返回
        return onlineMap;
    }

   /*
    * @description:  判断用户是否在线
    * @author bronya
    * @date: 2024/12/17 15:16
    * @param userId
    * @return java.lang.Boolean
    */
   public Boolean isOnline(Long userId) {
        String key = String.join(":", IMRedisKey.IM_USER_SERVER_ID, userId.toString());
        return redisMQTemplate.hasKey(key);
   }
   public List<Long> getOnlineUser(List<Long> userIds) {
       return new LinkedList<>(getOnlineTerminal(userIds).keySet());
   }
}