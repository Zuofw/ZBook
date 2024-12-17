package com.zuofw.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 〈〉
 *
 * @author qingqiu
 * @create 2024/10/6
 * @since 1.0.0
 */
@Data
@TableName("user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable {

    // todo 修改成全局的id
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "user_name")
    private String userName;

    @TableField(value = "password")
    private String password;

}