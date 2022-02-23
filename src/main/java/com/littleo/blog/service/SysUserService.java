package com.littleo.blog.service;

import com.littleo.blog.pojo.SysUser;
import com.littleo.blog.vo.UserVo;
import com.littleo.blog.vo.params.Result;

public interface SysUserService {
    SysUser findUserById(Long id);

    SysUser findUser(String account, String password);

    Result findUserByToken(String token);

    SysUser findUserByAccount(String account);

    void save(SysUser sysUser);

    UserVo findUserVoById(Long id);
}
