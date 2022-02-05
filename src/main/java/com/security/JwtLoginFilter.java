package com.security;

import com.alibaba.fastjson.JSON;
import com.common.Result;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @program: he
 * @description: 登录入口
 *  -- UsernamePasswordAuthenticationFilter security 登陆用户密码验证过滤器
 * @author: he
 * @create: 2020-01-14 15:47
 **/
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    private static final String REQUEST_METHOD="POST";
    private static final String DATA_FORMAT="json";

    @Resource
    private JwtTokenUtil jwtTokenUtil;
    @Resource
    private SecurityProperties securityProperties;

    public JwtLoginFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!REQUEST_METHOD.equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            String contentType = request.getHeader("Content-Type");
            SecurityUser user = null;
            //json提交
            if (contentType.contains(DATA_FORMAT)) {
                user = this.getSecurityUser(request);
                if (user == null || user.getUsername() == null || user.getPassword() == null) {
                    throw new AuthenticationServiceException("Authentication failure: username or password can't be null.");
                }
            } else {
                //表单提交
                String username = this.obtainUsername(request);
                String password = this.obtainPassword(request);
                if (username == null || password == null) {
                    throw new AuthenticationServiceException("Authentication failure: username or password can't be null.");
                }
                user = (new SecurityUser()).setUsername(username.trim()).setPassword(password);
            }
            request.setAttribute("account",user.getUsername());
            return this.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), new ArrayList()));
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityUser user = (SecurityUser)authResult.getPrincipal();
        user.setPassword("");
        String token = this.jwtTokenUtil.createToken(user);
        String tokenPrefix = this.securityProperties.getTokenPrefix();
        if (!StringUtils.hasLength(tokenPrefix)){
            tokenPrefix="Bearer";
        }
        String tokenHeader = this.securityProperties.getTokenHeader();
        if (!StringUtils.hasLength(tokenHeader)){
            tokenHeader="Authorization";
        }
        response.addHeader(tokenHeader, tokenPrefix + token);
        ResponseUtil.write(response, Result.ok(tokenPrefix + token));
    }

    //对应 ->JwtUserDetailsServiceImpl loadUserByUsername()
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        String msg;
        String account = (String)request.getAttribute("account");
        if (failed instanceof UsernameNotFoundException) {
            msg="账号不存在";
        }else if ( failed instanceof BadCredentialsException){
            msg="密码输入错误";
        }else if (failed instanceof DisabledException) {
            msg="用户账号已被禁用";
        } else if (failed instanceof LockedException) {
            msg="抱歉您的账户已被锁定";
        } else if (failed instanceof AccountExpiredException) {
            msg="账户过期";
        }  else {
            msg="登录失败";
        }
        ResponseUtil.write(response, Result.error(msg));
    }


    private SecurityUser getSecurityUser(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        try {
            InputStream is = request.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String s = "";
            while((s = br.readLine()) != null) {
                sb.append(s);
            }
            return JSON.parseObject(sb.toString(), SecurityUser.class);
        } catch (IOException var7) {
            return null;
        }
    }




}
