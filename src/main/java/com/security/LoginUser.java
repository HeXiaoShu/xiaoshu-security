package com.security;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description 当前认证登录用户
 * @Author Hexiaoshu
 * @Date 2021/11/15
 * @modify
 */
@Data
@Accessors(chain = true)
public class LoginUser {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户密码
     */
    private String passWord;
    /**
     * 是否锁定
     */
    private String status;

}
