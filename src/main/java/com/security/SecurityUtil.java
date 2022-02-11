package com.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author xiaoshu
 * @description 登录用户信息获取
 * @date 2022年02月11日 23:42
 */
@Component
public class SecurityUtil {

    public SecurityUser getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getPrincipal()==null?null:(SecurityUser)authentication.getPrincipal();
    }

}
