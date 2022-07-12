package com.littleo.blog.controller;

import com.littleo.blog.handler.TestException;
import com.littleo.blog.pojo.SysUser;
import com.littleo.blog.utils.UserThreadLocal;
import com.littleo.blog.vo.params.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @RequestMapping
    public Result test() throws TestException {
        SysUser sysUser = UserThreadLocal.get();
        System.out.println(sysUser);
        throw new TestException();

//        return Result.success(null);
    }
}