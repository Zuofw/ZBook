package com.zuofw.service;


import com.zuofw.domain.vo.PublishNoteReqVO;
import com.zuofw.domain.vo.Result;
import com.zuofw.domain.vo.UpdateNoteReqVO;
import org.springframework.cloud.client.loadbalancer.Response;

/**
 * @author: zuofw
 * @date: 2024/4/7 15:41
 * @version: v1.0.0
 * @description: 笔记业务
 **/
public interface NoteService {

    /**
     * 笔记发布
     * @param publishNoteReqVO
     * @return
     */
    Result<?> publishNote(PublishNoteReqVO publishNoteReqVO);

    Result<?> updateNote(UpdateNoteReqVO updateNoteReqVO);
}
