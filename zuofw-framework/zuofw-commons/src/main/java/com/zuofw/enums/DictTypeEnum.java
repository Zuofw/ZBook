package com.zuofw.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 字典类型枚举
 */
@NoArgsConstructor
@AllArgsConstructor
public enum DictTypeEnum {
    LOST_TYPE("lost_type"),          // 失物类型
    REPLY_REPORT("reply_report"),    // 举报回复类型
    TEST_TYPE("test_type");          // 测试类型

    private String value;

    @Override
    public String toString() {
        return this.value;
    }

}
