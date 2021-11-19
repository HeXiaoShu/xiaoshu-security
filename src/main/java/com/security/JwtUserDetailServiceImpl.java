package com.security;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @program: he
 * @description: 登录入口，具体登录实现
 * @author: he
 * @create: 2020-01-14 15:59
 **/
@Component
public class JwtUserDetailServiceImpl implements UserDetailsService {

    /**
     * 用户登录
     * @param account 账号
     * securityUser.setLocked(true); 锁定用户
     * @return token
     */
    @Override
    public UserDetails loadUserByUsername(String account) {
        String ok="1";
        SecurityUser securityUser= new SecurityUser();
        return securityUser.setUsername("xiaoshu").setPassword("c7815b6d0e7585dfa10431e450e71db9").setUserId(1L);
    }


}
