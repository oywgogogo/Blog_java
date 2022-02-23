package com.littleo.blog.controller;

import com.littleo.blog.service.LoginService;
import com.littleo.blog.vo.params.LoginParam;
import com.littleo.blog.vo.params.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 欧阳巍
 * @Date 2021/12/31 16:26
 */
@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    LoginService loginService;

    @PostMapping
    public Result login(@RequestBody LoginParam loginParam){
        return loginService.login(loginParam);
    }
}
