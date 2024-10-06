package com.zuofw.exception;


import com.zuofw.enums.ErrorCodeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;


@Data
@EqualsAndHashCode(callSuper = false)
public class BusinessException extends RuntimeException {

    private final Integer code;

    private final String message;

    private final String description;

    public BusinessException(ErrorCodeEnum errorCodeEnum, String description) {
        this.code = errorCodeEnum.getCode();
        this.message = errorCodeEnum.getMessage();
        this.description = description;
    }

    public BusinessException(HttpStatus httpStatus, String description) {
        this.code = httpStatus.value();
        this.message = httpStatus.getReasonPhrase();
        this.description = description;
    }
}
