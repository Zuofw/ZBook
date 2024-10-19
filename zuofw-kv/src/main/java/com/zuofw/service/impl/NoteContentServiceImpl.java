package com.zuofw.service.impl;


import com.zuofw.domain.entity.NoteContentDO;
import com.zuofw.domain.repository.NoteContentRepository;
import com.zuofw.domain.vo.Result;
import com.zuofw.dto.AddNoteContentReqDTO;
import com.zuofw.service.NoteContentService;
import com.zuofw.util.ResultUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author: 犬小哈
 * @date: 2024/4/7 15:41
 * @version: v1.0.0
 * @description: Key-Value 业务
 **/
@Service
@Slf4j
public class NoteContentServiceImpl implements NoteContentService {

    @Resource
    private NoteContentRepository noteContentRepository;


    @Override
    public Result<?> addNoteContent(AddNoteContentReqDTO addNoteContentReqDTO) {
        // 笔记 ID
        Long noteId = addNoteContentReqDTO.getNoteId();
        // 笔记内容
        String content = addNoteContentReqDTO.getContent();

        // 构建数据库 DO 实体类
        NoteContentDO nodeContent = NoteContentDO.builder()
                .id(UUID.randomUUID()) // TODO: 暂时用 UUID, 目的是为了下一章讲解压测，不用动态传笔记 ID。后续改为笔记服务传过来的笔记 ID
                .content(content)
                .build();

        // 插入数据
        noteContentRepository.save(nodeContent);

        return ResultUtils.success("ok");
    }
}
