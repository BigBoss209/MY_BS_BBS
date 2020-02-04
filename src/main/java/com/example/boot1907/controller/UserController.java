package com.example.boot1907.controller;

import com.example.boot1907.Service.IUserService;
import com.example.boot1907.Utils.TokenProccessor;
import com.example.boot1907.Utils.UserCheck;
import com.example.boot1907.bo.regUser;
import com.example.boot1907.dao.IUserDao;
import com.example.boot1907.pojo.User;
import com.example.boot1907.vo.RegMsg;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Auther 刘金明
 * @Date 2019/11/18-9:49
 * Description
 */
@Controller
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
    @ResponseBody
    @PostMapping("/register_findByName.do")
    public RegMsg FindByName(HttpServletRequest request, User user) {
        String token=TokenProccessor.getInstance().makeToken();
//        System.out.println("在FormServlet中生成的token："+token);
        RegMsg regMsg = new RegMsg();
        request.getSession().setAttribute("token", token);//在服务器端保存
        String msg = "";
       User dbUser = userService.findOne(user);
        //userService.save(user);
        if(dbUser != null){
            System.out.println(dbUser);
            msg = "1";
        }else {
//            System.out.println("ok");
            msg = "0";
        }
        regMsg.setMsg(msg);
        regMsg.setToken(token);
        return regMsg;
    }

    @PostMapping("/register.do")
    public ModelAndView register(HttpServletRequest request,regUser reguser) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register_login");

        boolean b = isRepeatSubmit(request,reguser);//判断用户是否是重复提交
        if(b==true){
            return modelAndView;
        }
        request.getSession().removeAttribute("token");//移除session中的token

        if(UserCheck.checkName(reguser)&&UserCheck.checkPasswork(reguser)){
//            System.out.println(reguser);
            //将bo转化为pojo
            User user = new User();
            user.setUserName(reguser.getUserName());
            user.setUserPassword(reguser.getPassWord());
            //注册用户
            userService.save(user);
            modelAndView.addObject("msg","注册成功！");
        }else {
            modelAndView.addObject("msg","注册失败，请联系管理员");
        }

       return modelAndView;
    }

    /**
     * 判断客户端提交上来的令牌和服务器端生成的令牌是否一致
     * @param
     * @return
     *         true 用户重复提交了表单
     *         false 用户没有重复提交表单
     */
    private boolean isRepeatSubmit(HttpServletRequest request,regUser reguser) {
        String client_token = reguser.getToken();
        //1、如果用户提交的表单数据中没有token，则用户是重复提交了表单
        if(client_token==null){
            return true;
        }
        //取出存储在Session中的token
        String server_token = reguser.getToken();
        //2、如果当前用户的Session中不存在Token(令牌)，则用户是重复提交了表单
        if(server_token==null){
            return true;
        }
        //3、存储在Session中的Token(令牌)与表单提交的Token(令牌)不同，则用户是重复提交了表单
        if(!client_token.equals(server_token)){
            return true;
        }
        //4、存储在Session中的Token(令牌)已经被移除
        if(request.getSession().getAttribute("token") == null){
            return true;
        }
        return false;
    }




    @PostMapping("/login.do")
    public ModelAndView login(User user, HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("register_login");
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(), user.getUserPassword());
        String error = null;
        try {
            subject.login(token);
            // 登录成功获取上次请求的页面
            SavedRequest savedRequest = WebUtils.getSavedRequest(request);
            if(savedRequest == null){
                mv.setViewName("redirect:"+"index.html");
                HttpSession session = request.getSession();
                User pojo = userService.findOne(user);
                session.setAttribute("userInfo",pojo);
                return mv;
            }
            String url = savedRequest.getRequestUrl();
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
            error = "其他错误,请联系管理员";
            e.printStackTrace();
        }
        if(error == null) {
            HttpSession session = request.getSession();
            User pojo = userService.findOne(user);
            session.setAttribute("userInfo", pojo);
        }
        mv.addObject("msg", error);
        return mv;
    }
}

