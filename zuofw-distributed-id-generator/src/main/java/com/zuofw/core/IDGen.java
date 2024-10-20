package com.zuofw.core;


import com.zuofw.core.common.Result;

public interface IDGen {
    Result get(String key);
    boolean init();
}
