package com.littleo.blog.controller;

import com.littleo.blog.service.LoginService;
import com.littleo.blog.vo.params.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 欧阳巍
 * @Date 2022/2/8 13:34
 */

@RestController
@RequestMapping("logout")
public class LogoutController {

    @Autowired
    private LoginService loginService;

    @GetMapping
    public Result logout(@RequestHeader("authorization") String token){
        return loginService.logout(token);
    }
}
