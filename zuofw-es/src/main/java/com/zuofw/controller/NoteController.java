package com.zuofw.controller;

import com.zuofw.model.vo.SearchNoteReqVO;
import com.zuofw.model.vo.SearchNoteRspVO;
import com.zuofw.service.NoteService;
import com.zuofw.util.PageResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
@Slf4j
public class NoteController {

    @Resource
    private NoteService noteService;

    @PostMapping("/note")
    public PageResponse<SearchNoteRspVO> searchNote(@RequestBody @Validated SearchNoteReqVO searchNoteReqVO) {
        return noteService.searchNote(searchNoteReqVO);
    }

}