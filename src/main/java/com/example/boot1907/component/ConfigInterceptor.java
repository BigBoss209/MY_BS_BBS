package com.example.boot1907.component;

import com.example.boot1907.pojo.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Auther 刘金明
 * @Date 2020/2/9-12:07
 * Description
 */
@Component
public class ConfigInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        // 从session中获取用户信息
        User comUser = (User) session.getAttribute("userInfo");

        // session过期
        if(comUser == null){
            System.out.println(">>>session过期, 跳至登录页");
            response.sendRedirect("/register_login.html"); // 通过接口跳转登录页面, 注:重定向后下边的代码还会执行 ; /outToLogin是跳至登录页的后台接口
            return false;
        }else{
            return true;
        }
    }
}
