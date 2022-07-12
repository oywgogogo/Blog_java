package com.littleo.blog.handler;

import com.littleo.blog.vo.params.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 欧阳巍
 * @Date 2021/12/29 17:39
 */
@ControllerAdvice
public class AllExceptionHandler {


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result doException(Exception ex){
        ex.printStackTrace();
        return Result.fail(-999,"系统异常");
    }

    @ExceptionHandler(TestException.class)
    @ResponseBody
    public Result catchException(){
        return Result.fail(-999,"catch");
    }
}
