package com.zuofw.service.impl;


import com.zuofw.domain.entity.NoteContentDO;
import com.zuofw.domain.repository.NoteContentRepository;
import com.zuofw.domain.vo.Result;
import com.zuofw.dto.AddNoteContentReqDTO;
import com.zuofw.dto.FindNoteContentRspDTO;
import com.zuofw.service.NoteContentService;
import com.zuofw.util.ResultUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * @author: zuofw
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
        String noteId = addNoteContentReqDTO.getNoteId();
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
    /**
     * 查询笔记内容
     *
     * @return
     */
    @Override
    public Result<FindNoteContentRspDTO> findNoteContent(String noteId) {
        // 根据笔记 ID 查询笔记内容
        Optional<NoteContentDO> optional = noteContentRepository.findById(UUID.fromString(noteId));

        // 若笔记内容不存在
        if (!optional.isPresent()) {
            throw new RuntimeException("笔记内容不存在");
        }

        NoteContentDO noteContentDO = optional.get();
        // 构建返参 DTO
        FindNoteContentRspDTO findNoteContentRspDTO = FindNoteContentRspDTO.builder()
                .noteId(noteContentDO.getId())
                .content(noteContentDO.getContent())
                .build();

        return ResultUtils.success(findNoteContentRspDTO);
    }

    @Override
    public Result<?> deleteNoteContent(String noteId) {
        // 根据笔记 ID 删除笔记内容
        noteContentRepository.deleteById(UUID.fromString(noteId));
        return ResultUtils.success("ok");
    }
}

