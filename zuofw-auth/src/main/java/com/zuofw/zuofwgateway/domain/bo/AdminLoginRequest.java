package com.zuofw.zuofwgateway.domain.bo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author qingqiu
 * @create 2024/4/9
 * @since 1.0.0
 */
@Data
public class AdminLoginRequest {

   @NotNull
   private String adminAccount;
   @NotNull
   private String adminPassword;
}