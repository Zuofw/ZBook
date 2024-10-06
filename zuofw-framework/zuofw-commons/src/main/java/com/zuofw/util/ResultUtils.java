package com.zuofw.util;


import com.zuofw.domain.vo.Result;
import com.zuofw.enums.ErrorCodeEnum;
import com.zuofw.exception.BusinessException;

public class ResultUtils {
    public static <T> Result<T> success(T data) {
        return new Result<>(ErrorCodeEnum.OK, data, "操作成功");
    }

    public static Result<?> error(BusinessException exception) {
        return new Result<>(exception.getCode(), exception.getMessage(), exception.getDescription(), null);
    }

    public static Result<?> error(Integer code, String message, String description) {
        return new Result<>(code, message, description, null);
    }

    public static Result<?> error(ErrorCodeEnum codeEnum, String description) {
        return error(codeEnum.getCode(), codeEnum.getMessage(), description);
    }
}
