package com.msb.crm;

import com.alibaba.fastjson.JSON;
import com.msb.crm.base.ResultInfo;
import com.msb.crm.exceptions.AuthException;
import com.msb.crm.exceptions.NoLoginException;
import com.msb.crm.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        if(e instanceof NoLoginException){
            ModelAndView mv=new ModelAndView("redirect:/index");
            return mv;
        }
        ModelAndView modelAndView=new ModelAndView("error");
        modelAndView.addObject("code",500);
        modelAndView.addObject("msg","系统异常，请重试");
        if(o instanceof HandlerMethod){
            HandlerMethod handlerMethod=(HandlerMethod) o;
            ResponseBody responseBody=handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);
            if(responseBody==null){
                if(e instanceof ParamsException){
                    ParamsException p=(ParamsException) e;
                    modelAndView.addObject("code",p.getCode());
                    modelAndView.addObject("msg",p.getMsg());
                }else if(e instanceof AuthException){
                    AuthException p=(AuthException) e;
                    modelAndView.addObject("code",p.getCode());
                    modelAndView.addObject("msg",p.getMsg());
                }
                return modelAndView;
            }else {
                ResultInfo resultInfo=new ResultInfo();
                resultInfo.setCode(500);
                resultInfo.setMsg("异常异常");
                if(e instanceof ParamsException){
                    ParamsException p=(ParamsException) e;
                    resultInfo.setCode(p.getCode());
                    resultInfo.setMsg(p.getMsg());
                }else if(e instanceof AuthException) {
                    AuthException p = (AuthException) e;
                    resultInfo.setCode(p.getCode());
                    resultInfo.setMsg(p.getMsg());
                }
                httpServletResponse.setContentType("application/json;charset=UTF-8");
                PrintWriter writer=null;
                try {
                    writer = httpServletResponse.getWriter();
                    String s = JSON.toJSONString(resultInfo);
                    writer.write(s);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }finally {
                    if(writer!=null){
                        writer.close();
                    }
                }
                return null;
            }
        }
        return modelAndView;
    }
}
