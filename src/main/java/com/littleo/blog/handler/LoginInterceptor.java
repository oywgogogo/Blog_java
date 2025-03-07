package com.littleo.blog.handler;

import com.alibaba.fastjson.JSON;
import com.littleo.blog.pojo.SysUser;
import com.littleo.blog.service.LoginService;
import com.littleo.blog.utils.UserThreadLocal;
import com.littleo.blog.vo.ErrorCode;
import com.littleo.blog.vo.params.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.handler.Handler;

/**
 * @author 欧阳巍
 * @Date 2022/2/9 10:13
 */

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*
         * 1.判断是否为controller
         * 2.判断是否登录（token是否为空）
         * 3.不为空则登录验证
         * 4.认证成功则放行
         * */
        if (!(handler instanceof HandlerMethod)) {
            //放行RequestRecourseHandler之类的请求
            return true;
        }

        //放行test请求
        String requestURI = request.getRequestURI();
        if (requestURI.contains("/test")) {
            log.info("=================request start===========================");
            log.info("请求URI为："+requestURI);
            log.info("=================request end===========================");
            // 放行 test 请求
            return true;
        }
        String token = request.getHeader("authorization");
        log.info("=================request start===========================");
        requestURI = request.getRequestURI();
        log.info("request uri:{}", requestURI);
        log.info("request method:{}", request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");

        if (StringUtils.isBlank(token)) {
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }

        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null) {
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        UserThreadLocal.put(sysUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocal.remove();
    }
}
