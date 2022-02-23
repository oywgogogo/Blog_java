package com.littleo.blog.utils;

import com.littleo.blog.pojo.SysUser;

/**
 * @author 欧阳巍
 * @Date 2022/2/9 11:32
 */
public class UserThreadLocal {

    private UserThreadLocal(){}

    private final static ThreadLocal<SysUser>  LOCAL = new ThreadLocal<>();

    public static void put(SysUser sysUser){
        LOCAL.set(sysUser);
    }

    public static  SysUser get(){
        return LOCAL.get();
    }

    public static void remove(){
        LOCAL.remove();
    }

}
