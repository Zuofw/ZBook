package com.zuofw.controller;

import com.zuofw.domain.vo.Result;
import com.zuofw.dto.AddNoteContentReqDTO;
import com.zuofw.feign.ZuofwKVClient;
import com.zuofw.service.NoteContentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;

import java.util.prefs.NodeChangeEvent;

/**
 * 〈〉
 *
 * @author zuowei
 * @create 2024/10/19
 * @since 1.0.0
 */
@RestController
public class NoteContentController implements ZuofwKVClient {

    @Resource
    private NoteContentService noteContentService;
    @Override
    public Result<?> addNoteContent(AddNoteContentReqDTO addNoteContentReqDTO) {
        return noteContentService.addNoteContent(addNoteContentReqDTO);
    }
}