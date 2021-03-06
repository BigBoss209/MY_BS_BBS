package com.example.boot1907.controller;

import com.example.boot1907.Service.impl.ArticleServiceImpl;
import com.example.boot1907.pojo.Article;
import com.example.boot1907.pojo.ArticleType;
import com.example.boot1907.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @Auther 刘金明
 * @Date 2020/2/28-21:32
 * Description 处理帖子信息
 */
@Controller
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleServiceImpl articleService;
//Ajax获取所有帖子类型
    @PostMapping("/getArticleType.do")
    @ResponseBody
    public List<ArticleType> getArticleType(){
        List<ArticleType> articleTypeList = articleService.getArticleType();
        return articleTypeList;
    }

    @PostMapping("/getAdminArtType.do")
    @ResponseBody
    public List<ArticleType> getAdminArtType(){
        List<ArticleType> articleTypeList = articleService.getAdminArtType();
        return articleTypeList;
    }

//发帖
    @PostMapping("/addArticle.do")
    @ResponseBody
    public Article addArticle(Article article, HttpServletRequest request){
        if(!havaSession(request)) {
            article.setArtContent("请登陆后重试");
            return article;
        }
        User userInfo = (User)request.getSession().getAttribute("userInfo");
        article.setArtUserId(userInfo.getUserId());
        article.setArtCreTime(new Date());
        articleService.addArticle(article);
        article.setArtContent("发表成功");
        return article;
    }

    @RequestMapping("/changeArticle.do")
    public ModelAndView editArticle(@Param("artId") String artId,HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/Article/editArticle.html");
        if(!havaSession(request)) {
            modelAndView.setViewName("redirect:"+"/register_login.html");
            return modelAndView;
        }
        Article article = new Article();
        User userInfo = (User)request.getSession().getAttribute("userInfo");
        article.setArtUserId(userInfo.getUserId());
        article.setArtId(Long.parseLong(artId));
        article = articleService.editArticle(article);
        modelAndView.addObject("article",article);
        return modelAndView;
    }

    @PostMapping("/reviseArticle.do")
    @ResponseBody
    public Article reviseArticle(Article article, HttpServletRequest request){
        System.out.println(article);

        if(!havaSession(request)) {
            article.setArtContent("请登陆后重试");
            return article;
        }
        User userInfo = (User)request.getSession().getAttribute("userInfo");
        article.setArtUserId(userInfo.getUserId());
        article.setArtCreTime(new Date());
        articleService.reviseArticle(article);
        article.setArtContent("修改成功");
        return article;
    }

    @PostMapping("/deleteArticle.do")
    @ResponseBody
    public String deleteArticle(@Param("artId") String artId, HttpServletRequest request){
        User userInfo = (User)request.getSession().getAttribute("userInfo");
        Article article = new Article();
        article.setArtUserId(userInfo.getUserId());
        article.setArtId(Long.parseLong(artId));
        articleService.deleteArticle(article);
        String msg="删除成功";
        return msg;
    }

    @RequestMapping("/watchArticle.do")
    public ModelAndView watchArticle(@Param("artId") Integer artId){
        ModelAndView modelAndView = new ModelAndView();
        //        还可以添加错误页面
        if(!articleService.isArt(artId)){
            modelAndView.setViewName("/error.html");
            modelAndView.addObject("message","帖子查询错误，要不换个试试！！");
            return modelAndView;
        }
        modelAndView.setViewName("/Article/detailArticle.html");
        return modelAndView;
    }


//    判断用户session是否有效
    private boolean havaSession(HttpServletRequest request){
        User userInfo = (User)request.getSession().getAttribute("userInfo");
        return userInfo == null?false:true;
    }
}
