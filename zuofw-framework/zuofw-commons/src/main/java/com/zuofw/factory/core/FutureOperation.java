package com.zuofw.factory.core;

/**
 * Future线程接口
 *
 * @author tangchao
 */
@FunctionalInterface
public interface FutureOperation<T> {

    T accept();
    
}
