package com.zuofw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: zuofw
 * @date: 2024/4/7 15:17
 * @version: v1.0.0
 * @description: 新增笔记内容
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddNoteContentReqDTO {

    @NotNull(message = "笔记 ID 不能为空")
    private String noteId;

    @NotBlank(message = "笔记内容不能为空")
    private String content;

}

