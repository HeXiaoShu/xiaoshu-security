package com.security;

import com.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: xiaoshu-security
 * @description: 认证拦截器
 * @author: xiaoshu
 **/
@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private SecurityProperties securityProperties;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        //忽略拦截路径放行
        if (this.ignore(request)) {
            chain.doFilter(request, response);
        } else {
            String tokenHeader = this.securityProperties.getTokenHeader();
            if (!StringUtils.hasLength(tokenHeader)){
                tokenHeader="Authorization";
            }
            //请求头获取
            String header = request.getHeader(tokenHeader);
            //前缀匹配
            String tokenPrefix = this.securityProperties.getTokenPrefix();
            if (!StringUtils.hasLength(tokenPrefix)){
                tokenPrefix="Bearer";
            }
            if (header != null && header.startsWith(tokenPrefix)) {
                UsernamePasswordAuthenticationToken authenticationToken = null;
                try {
                    authenticationToken = this.getAuthentication(header);
                } catch (Exception e) {
                    log.error("getAuthentication() Exception:{}", e.getMessage());
                    //ResponseUtil.write(response,Result.error(e.getMessage()));
                }
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                chain.doFilter(request, response);
            } else {
                ResponseUtil.write(response,Result.error("无效令牌"));
            }
        }
    }

    /**
     * 这里从token中获取用户信息并新建一个token
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String header) {
        String tokenPrefix = this.securityProperties.getTokenPrefix();
        if (!StringUtils.hasLength(tokenPrefix)){
            tokenPrefix="Bearer";
        }
        String token = header.replace(tokenPrefix, "");
        String principal = this.jwtTokenUtil.getUserName(token);
        if (principal != null) {
            SecurityUser user = this.jwtTokenUtil.getSecurityUser(token);
            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        } else {
            return null;
        }
    }

    /**
     * 校检是否忽略路径,默认放行路径 /logOut
     * @param request request
     * @return boolean
     */
    private boolean ignore(HttpServletRequest request) {
        String httpIgnore = securityProperties.getHttpIgnore();
        if (!StringUtils.hasLength(httpIgnore)){
            httpIgnore="/logOut,";
        }
        String[] ignores = httpIgnore.split(",");
        for (String ignore : ignores) {
            if (new AntPathRequestMatcher(ignore).matches(request)) {
                return true;
            }
        }
        return false;
    }


}
