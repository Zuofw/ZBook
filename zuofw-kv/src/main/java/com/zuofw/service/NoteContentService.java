package com.zuofw.service;


import com.zuofw.domain.vo.Result;
import com.zuofw.dto.AddNoteContentReqDTO;


/**
 * @author: zuofw
 * @description: 笔记内容存储业务
 **/
public interface NoteContentService {

    /**
     * 添加笔记内容
     * 
     * @param addNoteContentReqDTO
     * @return
     */
    Result<?> addNoteContent(AddNoteContentReqDTO addNoteContentReqDTO);

}
