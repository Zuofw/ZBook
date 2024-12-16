package com.zuofw.service;

import com.zuofw.model.vo.SearchNoteReqVO;
import com.zuofw.model.vo.SearchNoteRspVO;
import com.zuofw.util.PageResponse;

public interface NoteService {

    /**
     * 搜索笔记
     * @param searchNoteReqVO
     * @return
     */
    PageResponse<SearchNoteRspVO> searchNote(SearchNoteReqVO searchNoteReqVO);
}