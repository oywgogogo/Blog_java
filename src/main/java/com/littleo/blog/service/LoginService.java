package com.littleo.blog.service;

import com.littleo.blog.pojo.SysUser;
import com.littleo.blog.vo.params.LoginParam;
import com.littleo.blog.vo.params.Result;

public interface LoginService {

    /**
     * 登录功能
     * @param loginParam
     * @return
     */

    //登录
    Result login(LoginParam loginParam);
    //校验
    SysUser checkToken(String token);
    //退出
    Result logout(String token);

    Result register(LoginParam loginParam);
}
