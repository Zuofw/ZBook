package com.zuofw.service;


import com.zuofw.domain.vo.Result;
import com.zuofw.dto.AddNoteContentReqDTO;
import com.zuofw.dto.FindNoteContentRspDTO;
import org.springframework.cloud.client.loadbalancer.Response;


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
    /**
     * 查询笔记内容
     *
     *
     * @return
     */
    Result<FindNoteContentRspDTO> findNoteContent(String noteId);

    /**
     * 删除笔记内容
     *
     * @param noteId
     * @return
     */
    Result<?> deleteNoteContent(String noteId);

}
