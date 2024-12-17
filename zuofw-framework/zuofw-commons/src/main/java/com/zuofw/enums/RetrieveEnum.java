package com.zuofw.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/*
 * @description: 审核状态枚举类
 * @author qingqiu
 * @date: 2024/7/20 20:42
 */
@Getter
@AllArgsConstructor
public enum RetrieveEnum {
    //未审核
    NOT_RETRIEVE(0,"未审核"),

    //通过未被拿走
    RETRIEVE_NOT_TAKEN(1,"未领取"),

    //审核不通过
    REJECT(2,"审核不通过"),

    //通过已被拿走
    RETRIEVE_TAKEN(3,"已领取");

    //一个已审核状态，包含134三种状态，一个List结构，包含134三种状态
    private final Integer code;

    private final String name;
    public static List<Integer> getRetrieve(){
        return List.of(RETRIEVE_NOT_TAKEN.code,RETRIEVE_TAKEN.code);
    }

    public static String getName(Integer code){
        for (RetrieveEnum value : RetrieveEnum.values()) {
            if (value.getCode().equals(code)){
                return value.getName();
            }
        }
        return null;
    }
}
