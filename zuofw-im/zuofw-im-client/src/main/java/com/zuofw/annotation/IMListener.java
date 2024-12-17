package com.zuofw.annotation;


import com.zuofw.enums.IMListenerType;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description:  IM监听器注解
 * @autho qingqiu
 * @date 2024/12/17 14:35
 * @version 1.0
 */
@Target({ElementType.TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface IMListener {

    IMListenerType type();

}
