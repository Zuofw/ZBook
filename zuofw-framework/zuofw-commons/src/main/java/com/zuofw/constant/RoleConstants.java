package com.zuofw.constant;


import java.util.HashMap;
import java.util.Map;

/**
 * @description:  角色常量
 * @author zuowei
 * @date 2024/4/9 18:05
 * @version 1.0
 */
public interface RoleConstants {
    //管理员
    Integer ADMIN = 0;
    //普通用户
    Integer USER = 1;
    Map<Integer, String> roleMap = new HashMap<>() {
        {
            put(ADMIN, "ROLE_ADMIN");
            put(USER, "ROLE_USER");
        }
    };
}
