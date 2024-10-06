package com.zuofw.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 物品类型枚举类
 */
@Getter
@AllArgsConstructor
public enum GoodsTypeEnum {

    CAMPUS_CARD(1, "校园卡"),                   // 校园卡
    STUDENT_ID(2, "学生证"),                    // 学生证
    IDENTITY_CARD(3, "身份证"),                 // 身份证
    HOUSEHOLD_ITEM(4, "生活用品"),              // 生活用品
    SCHOOL_SUPPLIES(5, "学习用品"),             // 学习用品
    RECREATIONAL_GOODS(6, "娱乐用品"),          // 娱乐用品
    BANK_CARDS(7, "银行卡"),                   // 银行卡
    OTHER(8, "其他");                          // 其他

    private final Integer id;
    private final String type;

    public static String getType(Integer id){
        for (GoodsTypeEnum value : GoodsTypeEnum.values()) {
            if (value.getId().equals(id)) {
                return value.getType();
            }
        }
        return null;
    }

}
