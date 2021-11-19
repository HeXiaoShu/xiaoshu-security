package com.security;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: he
 * @description: jwt配置
 * @author: he
 * @create: 2020-01-14 15:26
 **/
@Component
public class JwtTokenUtil {

    @Resource
    private SecurityProperties securityProperties;

    public JwtTokenUtil() {}

    /**
     * token生成
     * @param user
     * @return
     */
    public String createToken(SecurityUser user) {
        String secret = this.securityProperties.getSecret();
        if (secret==null){
            secret="secret";
        }
        Long expiration = this.securityProperties.getExpiration();
        if (expiration==null){
            expiration=12L;
        }
        long time = expiration * 60L * 60L;
        HashMap<String, Object> map = new HashMap<>(1);
        map.put("user", user);
        return Jwts.builder()
                .setClaims(map)
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + time * 1000L))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public String getUserName(String token) {
        return this.generateToken(token).getSubject();
    }

    private Claims generateToken(String token) {
        String secret = this.securityProperties.getSecret();
        if (secret==null){
            secret="secret";
        }
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public SecurityUser getSecurityUser(String token){
        Claims claims = this.generateToken(token);
        Map map = claims.get("user", Map.class);
        return JSON.parseObject(JSON.toJSONString(map), SecurityUser.class);
    }

}
