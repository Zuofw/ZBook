package com.zuofw.zuofwgateway.utils;

import com.zuofw.zuofwgateway.domain.LoginUser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public static LoginUser getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return (LoginUser)authentication.getPrincipal();
        }
        return null;
    }

    public static void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static String encodePassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    public static Integer getRoleId() {
        LoginUser loginUser = getLoginUser();
        if (loginUser != null) {
            return loginUser.getUserRoleId();
        }
        return null;
    }


    public static String getUserId() {
        LoginUser loginUser = getLoginUser();
        if (loginUser != null) {
            return loginUser.getUserId();
        }
        return null;
    }

}
