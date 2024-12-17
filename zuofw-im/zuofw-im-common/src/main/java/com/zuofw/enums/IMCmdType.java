package com.zuofw.enums;

import lombok.AllArgsConstructor;

/*
 * @description: IM命令类型枚举
 * @author bronya
 * @date: 2024/12/17 15:00
 * @param null
 * @return null
 */
@AllArgsConstructor
public enum IMCmdType {

    /**
     * 登陆
     */
    LOGIN(0, "登陆"),
    /**
     * 心跳
     */
    HEART_BEAT(1, "心跳"),
    /**
     * 强制下线
     */
    FORCE_LOGUT(2, "强制下线"),
    /**
     * 私聊消息
     */
    PRIVATE_MESSAGE(3, "私聊消息"),
    /**
     * 群发消息
     */
    GROUP_MESSAGE(4, "群发消息"),
    /**
     * 系统消息
     */
    SYSTEM_MESSAGE(5,"系统消息");


    private final Integer code;

    private final String desc;


    public static IMCmdType fromCode(Integer code) {
        for (IMCmdType typeEnum : values()) {
            if (typeEnum.code.equals(code)) {
                return typeEnum;
            }
        }
        return null;
    }


    public Integer code() {
        return this.code;
    }


}

