package com.zuofw.controller;



import com.zuofw.domain.vo.PublishNoteReqVO;
import com.zuofw.domain.vo.Result;
import com.zuofw.service.NoteService;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author: zuofw
 * @date: 2024/4/4 13:22
 * @version: v1.0.0
 * @description: 笔记
 **/
@RestController
@RequestMapping("/content")
@Slf4j
public class NoteController {

    @Resource
    private NoteService noteService;

    @PostMapping(value = "/publish")
    public Result<?> publishNote(@Validated @RequestBody PublishNoteReqVO publishNoteReqVO) {
        return noteService.publishNote(publishNoteReqVO);
    }
//    @GetMapping(value = "/list")
//    public Result<?> listNote() {
//        return noteService.listNote();
//    }
}
