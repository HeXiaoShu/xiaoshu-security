package com.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: he
 * @description: security属性配置类
 * @author: he
 * @create: 2020-01-14 11:38
 **/

@Data
@Component
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {
    /**
     * 忽略拦截路径
     */
    private String httpIgnore;
    /**
     * 请求头名称
     */
    private String tokenHeader;
    /**
     * 请求头前缀
     */
    private String tokenPrefix;
    /**
     * 令牌加密密匙
     */
    private String secret;
    /**
     * 失效时间 / 秒
     */
    private Long expiration;
}
