package com.security;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Description security 加密方式替换
 * @Author Hexiaoshu
 * @Date 2021/11/18
 * @modify
 */
public class MD5PasswordEncoder implements PasswordEncoder {
    //TODO 加盐
    private static String salt="xiaoshu@730!@#$/";
    @Override
    public String encode(CharSequence charSequence) {
        return DigestUtils.md5Hex(String.valueOf(charSequence)+salt);
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        String s1 = DigestUtils.md5Hex(String.valueOf(charSequence)+salt);
        return s1.equals(s);
    }

}
