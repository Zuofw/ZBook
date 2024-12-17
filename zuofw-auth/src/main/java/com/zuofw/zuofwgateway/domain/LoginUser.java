package com.zuofw.zuofwgateway.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * 〈〉
 *
 * @author qingqiu
 * @create 2024/10/6
 * @since 1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginUser implements UserDetails {
    // 用户id
    private String userId;
    // 密码
    private String userPassword;

    //用户角色
    private Integer userRoleId;
    // 登录标识
    private String UUID;
    // 用户权限
    private List<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.userPassword;
    }

    @Override
    public String getUsername() {
        return this.userId;
    }


    @Override
    public boolean isAccountNonExpired() { // 账户是否未过期
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {// 账户是否未锁定
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {// 密码是否未过期
        return true;
    }

    @Override
    public boolean isEnabled() {// 账户是否可用
        return true;
    }
}