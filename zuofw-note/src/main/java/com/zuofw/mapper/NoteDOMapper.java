package com.zuofw.mapper;


import com.zuofw.domain.dataobject.NoteDO;

public interface NoteDOMapper {
    int deleteByPrimaryKey(Long id);

    int insert(NoteDO record);

    int insertSelective(NoteDO record);

    NoteDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(NoteDO record);

    int updateByPrimaryKey(NoteDO record);

    int updateVisibleOnlyMe(NoteDO noteDO);

    int updateIsTop(NoteDO noteDO);

}