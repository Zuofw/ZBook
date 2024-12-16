package com.zuofw.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * 自动创建表
 */
@Mapper
public interface CreateTableMapper {

    /**
     * 创建日增量表：关注数计数变更
     * @param tableNameSuffix
     */
    void createDataAlignFollowingCountTempTable(String tableNameSuffix);
}