package com.littleo.blog.utils;

import jdk.nashorn.internal.ir.RuntimeNode;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 欧阳巍
 * @Date 2022/2/18 14:38
 */
public class HttpContextUtils {
    public static HttpServletRequest getHttpServletRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }
}
