package com.example.boot1907.controller;

import com.example.boot1907.Service.IUserService;
import com.example.boot1907.dao.IUserDao;
import com.example.boot1907.pojo.User;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Auther 刘金明
 * @Date 2019/11/18-9:49
 * Description
 */
@RestController
public class UserController {
    @Autowired
    private IUserService userService;

//    @RequestMapping("/list.html")
//    public List<User> list() {
//        return userDao.pageList();
//    }
//    @RequestMapping("/pageUser.html")
//    public PageInfo<User> findUserWithPage(){
//        List<User> users = userService.findUserWithPage(1, 3);
//        PageInfo<User> pageInfo = new PageInfo<>(users);
//        return pageInfo;
//    }

    @PostMapping("/register.do")
    public String register(User user) {
        userService.save(user);
        return "login";
    }

    @PostMapping("/login.do")
    public ModelAndView login(User user, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("login");
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(), user.getUserPassword());
        String error = null;
        try {
            subject.login(token);
            // 登录成功获取上次请求的页面
            String url = WebUtils.getSavedRequest(request).getRequestUrl();
            System.out.println(url);
            mv.setViewName("redirect:" + url);
        } catch (UnknownAccountException |
                IncorrectCredentialsException e) {
            error = "用户名或密码错误!";
        } catch (ExcessiveAttemptsException e) {
            error = "密码错误次数过多，账户锁定1分钟,请稍后再试!";
        } catch (LockedAccountException e) {
            error = "该账户异常,已被锁定, 禁止登录, 请联系管理解锁!";
        } catch (Exception e) {
            error = "其他错误,请联系管理员" + e.getMessage();
        }
        mv.addObject("msg", error);
        return mv;
    }
}

