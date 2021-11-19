package com.security;

import com.common.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: he
 * @description:
 * @author: he
 * @create: 2020-01-15 11:33
 **/
public class RestAuthenticationAccessDeniedHandler implements AccessDeniedHandler {
    public RestAuthenticationAccessDeniedHandler() {}

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        ResponseUtil.write(response, Result.error("没有权限"));
    }
}
