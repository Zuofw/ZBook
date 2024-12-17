package com.zuofw;

import com.zuofw.enums.IMTerminalType;
import com.zuofw.model.IMGroupMessage;
import com.zuofw.model.IMPrivateMessage;
import com.zuofw.model.IMSystemMessage;
import com.zuofw.sender.IMSender;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * IM客户端
 *
 * @author zuowei
 * @create 2024/12/17
 * @since 1.0.0
 */
@Configuration
@AllArgsConstructor
public class IMClient {
    private final IMSender imSender;

    /**
     * 判断用户是否在线
     *
     * @param userId 用户id
     */
    public Boolean isOnline(Long userId){
        return imSender.isOnline(userId);
    }

    /**
     * 判断多个用户是否在线
     *
     * @param userIds 用户id列表
     * @return 在线的用户列表
     */
    public List<Long> getOnlineUser(List<Long> userIds){
        return imSender.getOnlineUser(userIds);
    }


    /**
     * 判断多个用户是否在线
     *
     * @param userIds 用户id列表
     * @return 在线的用户终端
     */
    public Map<Long,List<IMTerminalType>> getOnlineTerminal(List<Long> userIds){
        return imSender.getOnlineTerminal(userIds);
    }

    /**
     * 发送系统消息（发送结果通过MessageListener接收）
     *
     * @param message 私有消息
     */
    public<T> void sendSystemMessage(IMSystemMessage<T> message){
        imSender.sendSystemMessage(message);
    }


    /**
     * 发送私聊消息（发送结果通过MessageListener接收）
     *
     * @param message 私有消息
     */
    public<T> void sendPrivateMessage(IMPrivateMessage<T> message){
        imSender.sendPrivateMessage(message);
    }

    /**
     * 发送群聊消息（发送结果通过MessageListener接收）
     *
     * @param message 群聊消息
     */
    public<T> void sendGroupMessage(IMGroupMessage<T> message){
        imSender.sendGroupMessage(message);
    }

}