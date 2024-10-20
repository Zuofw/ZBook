package com.zuofw.domain.vo;


import com.zuofw.enums.ErrorCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回格式统一封装
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 描述信息
     */
    private String description;

    /**git
     * 返回的数据
     */
    private T data;

    public Result(ErrorCodeEnum codeEnum, T data, String description) {
        this.code = codeEnum.getCode();
        this.message = codeEnum.getMessage();
        this.data = data;
        this.description = description;
    }
}
