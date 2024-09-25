package com.msb.crm.interceptor;

import com.msb.crm.dao.UserMapper;
import com.msb.crm.exceptions.NoLoginException;
import com.msb.crm.utils.LoginUserUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoLoginInterceptor extends HandlerInterceptorAdapter {
    @Resource
    public UserMapper userMapper;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Integer i = LoginUserUtil.releaseUserIdFromCookie(request);
        if(null==i||userMapper.selectByPrimaryKey(i)==null){
            throw new NoLoginException();
        }


        return true;
    }
}
