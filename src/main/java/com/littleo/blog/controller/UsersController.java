package com.littleo.blog.controller;

import com.littleo.blog.service.SysUserService;
import com.littleo.blog.vo.params.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 欧阳巍
 * @Date 2022/2/7 13:33
 */
@RestController
@RequestMapping("users")
public class UsersController {
    @Autowired
    private SysUserService sysUserService;

    @GetMapping("currentUser")
    public Result currentUser(@RequestHeader("authorization") String token ){
        return sysUserService.findUserByToken(token);
    }
}
