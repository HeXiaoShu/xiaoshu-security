package com.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @program: heqq
 * @description: security用户
 * @author: he
 * @create: 2020-01-14 15:41
 **/
public class SecurityUser implements UserDetails {
    private static final long serialVersionUID = -2211380247224432737L;

    private Long userId;
    private String username;
    private String password;
    private String name;
    private List<String> roles;
    /**
     * 是否可用
     */
    private boolean enabled = true;
    /**
     * 是否冻结
     */
    private boolean locked = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList();
        List<String> userRoles = this.getRoles();
        if (userRoles != null) {
            Iterator var3 = userRoles.iterator();

            while(var3.hasNext()) {
                String role = (String)var3.next();
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
                authorities.add(authority);
            }
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }
    @Override
    public String getUsername() {
        return this.username;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return locked;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public SecurityUser() {
    }

    public Long getUserId() {
        return this.userId;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getRoles() {
        return this.roles;
    }

    public SecurityUser setUserId(final Long userId) {
        this.userId = userId;
        return this;
    }

    public SecurityUser setUsername(final String username) {
        this.username = username;
        return this;
    }

    public SecurityUser setPassword(final String password) {
        this.password = password;
        return this;
    }

    public SecurityUser setName(final String name) {
        this.name = name;
        return this;
    }

    public SecurityUser setRoles(final List<String> roles) {
        this.roles = roles;
        return this;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
