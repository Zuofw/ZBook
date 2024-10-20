package com.zuofw.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.nacos.shaded.com.google.common.base.Preconditions;
import com.zuofw.client.rpc.DistributedIdGeneratorRpcService;
import com.zuofw.client.rpc.KeyValueRpcService;
import com.zuofw.domain.dataobject.NoteDO;
import com.zuofw.domain.vo.PublishNoteReqVO;
import com.zuofw.domain.vo.Result;
import com.zuofw.enums.NoteStatusEnum;
import com.zuofw.enums.NoteTypeEnum;
import com.zuofw.enums.NoteVisibleEnum;
import com.zuofw.mapper.NoteDOMapper;
import com.zuofw.mapper.TopicDOMapper;
import com.zuofw.service.NoteService;
import com.zuofw.util.ResultUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author: 犬小哈
 * @date: 2024/4/7 15:41
 * @version: v1.0.0
 * @description: 笔记业务
 **/
@Service
@Slf4j
public class NoteServiceImpl implements NoteService {

    @Resource
    private NoteDOMapper noteDOMapper;
    @Resource
    private TopicDOMapper topicDOMapper;
    @Resource
    private DistributedIdGeneratorRpcService distributedIdGeneratorRpcService;
    @Resource
    private KeyValueRpcService keyValueRpcService;


    /**
     * 笔记发布
     *
     * @param publishNoteReqVO
     * @return
     */
    @Override
    public Result<?> publishNote(PublishNoteReqVO publishNoteReqVO) {
        // 笔记类型
        Integer type = publishNoteReqVO.getType();

        // 获取对应类型的枚举
        NoteTypeEnum noteTypeEnum = NoteTypeEnum.valueOf(type);

        // 若非图文、视频，抛出业务业务异常
        if (Objects.isNull(noteTypeEnum)) {
            throw new RuntimeException("笔记类型不存在");
        }

        String imgUris = null;
		// 笔记内容是否为空，默认值为 true，即空
        Boolean isContentEmpty = true;
        String videoUri = null;
        switch (noteTypeEnum) {
            case IMAGE_TEXT: // 图文笔记
                List<String> imgUriList = publishNoteReqVO.getImgUris();
                // 校验图片是否为空
                Preconditions.checkArgument(CollUtil.isNotEmpty(imgUriList), "笔记图片不能为空");
                // 校验图片数量
                Preconditions.checkArgument(imgUriList.size() <= 8, "笔记图片不能多于 8 张");
                // 将图片链接拼接，以逗号分隔
                imgUris = String.join(",", imgUriList);

                break;
            case VIDEO: // 视频笔记
                videoUri = publishNoteReqVO.getVideoUri();
                // 校验视频链接是否为空
                Preconditions.checkArgument(!videoUri.isBlank(), "笔记视频不能为空");
                break;
            default:
                break;
        }
        
        // RPC: 调用分布式 ID 生成服务，生成笔记 ID
        String snowflakeIdId = distributedIdGeneratorRpcService.getSnowflakeId();
        // 笔记内容 UUID
        String contentUuid = null;

        // 笔记内容
        String content = publishNoteReqVO.getContent();

        // 若用户填写了笔记内容
        if (!content.isBlank()) {
            // 内容是否为空，置为 false，即不为空
            isContentEmpty = false;
            // 生成笔记内容 UUID
            contentUuid = UUID.randomUUID().toString();
            // RPC: 调用 KV 键值服务，存储短文本
            boolean isSavedSuccess = keyValueRpcService.saveNoteContent(contentUuid, content);

            // 若存储失败，抛出业务异常，提示用户发布笔记失败
            if (!isSavedSuccess) {
                throw new RuntimeException("笔记内容存储失败");
            }
        }

        // 话题
        Long topicId = publishNoteReqVO.getTopicId();
        String topicName = null;
        if (Objects.nonNull(topicId)) {
            // 获取话题名称
            topicName = topicDOMapper.selectNameByPrimaryKey(topicId);
        }

        // 发布者用户 ID
        // todo 懒得写了
        Long creatorId = 1L;

        // 构建笔记 DO 对象
        NoteDO noteDO = NoteDO.builder()
                .id(Long.valueOf(snowflakeIdId))
                .isContentEmpty(isContentEmpty)
                .creatorId(creatorId)
                .imgUris(imgUris)
                .title(publishNoteReqVO.getTitle())
                .topicId(publishNoteReqVO.getTopicId())
                .topicName(topicName)
                .type(type)
                .visible(NoteVisibleEnum.PUBLIC.getCode())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .status(NoteStatusEnum.NORMAL.getCode())
                .isTop(Boolean.FALSE)
                .videoUri(videoUri)
                .contentUuid(contentUuid)
                .build();

        try {
            // 笔记入库存储
            noteDOMapper.insert(noteDO);
        } catch (Exception e) {
            log.error("==> 笔记存储失败", e);
            
            // RPC: 笔记保存失败，则删除笔记内容
            if (!contentUuid.isBlank()) {
                keyValueRpcService.deleteNoteContent(contentUuid);
            }
        }

        return ResultUtils.success("ok");
    }


}

