package com.zuofw.zuofwgateway.domain.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * 〈一句话功能简述〉<br>
 * 〈注册dto〉
 *
 * @author qingqiu
 * @create 2024/4/9
 * @since 1.0.0
 */
@Data
public class AdminRegisterRequest {

    @NotNull
    @NotBlank
    private String adminAccount;

    @NotNull
    @NotBlank
    private String adminPassword;

}