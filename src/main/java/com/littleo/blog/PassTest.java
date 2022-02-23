package com.littleo.blog;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author 欧阳巍
 * @Date 2022/2/7 11:24
 */
public class PassTest {
    public static void main(String[] args) {
        String password = "admin";
        String slat = "mszlu!@#";
        String pwd = DigestUtils.md5Hex(password + slat);
        System.out.println(pwd);
    }
}
