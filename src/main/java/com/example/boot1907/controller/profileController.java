package com.example.boot1907.controller;

import com.example.boot1907.Service.impl.ArticleServiceImpl;
import com.example.boot1907.bo.ArticlePage;
import com.example.boot1907.pojo.Article;
import com.example.boot1907.pojo.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * @Auther 刘金明
 * @Date 2020/3/4-10:42
 * Description 对文章进行查询
 */
@Controller
@RequestMapping("/profile")
public class profileController {
    @Autowired
    private ArticleServiceImpl articleService;

    @RequestMapping("/{action}.do")
    public String profile(@PathVariable(name = "action") String action, Model model,
                          HttpServletRequest request,@RequestParam(defaultValue = "1") int pageNum,
                          @RequestParam(defaultValue = "2") int pageSize){
        if(!havaSession(request)) {
            return "register_login";
        }
        PageHelper.startPage(pageNum, pageSize, true);
        ArrayList<Article> Articlelist = null;
        if("questions".equals(action)){
            model.addAttribute("page","questions");
            model.addAttribute("pageName","我的问题");
            Articlelist = (ArrayList<Article>) articleService.getAllQuestions((User)request.getSession().getAttribute("userInfo"));
            PageInfo<Article> pageInfo = new PageInfo<Article>(Articlelist);
            model.addAttribute("Mylist",pageInfo);
        }
        if ("replys".equals(action)){
            model.addAttribute("page","replys");
            model.addAttribute("pageName","我的回复");
            //            articleService.getAllReplys();
        }
        return  "profile";
    }

    @RequestMapping("/AJquestions.do")
    @ResponseBody
    public ArticlePage AJAXquestion(HttpServletRequest request,
                                    @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "2") int pageSize){
        PageHelper.startPage(pageNum, pageSize, true);
        ArticlePage articlePage = new ArticlePage();
        ArrayList<Article> Articlelist = (ArrayList<Article>) articleService.getAllQuestions((User)request.getSession().getAttribute("userInfo"));
        PageInfo<Article> pageInfo = new PageInfo<Article>(Articlelist);
        articlePage.setPageInfo(pageInfo);
        articlePage.setUser((User)request.getSession().getAttribute("userInfo"));
        return articlePage;
    };
    @RequestMapping("/search.do")
    @ResponseBody
    public ArticlePage searchByTypeId(@RequestParam("typeId") int typeId,
            @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "2") int pageSize){
        PageHelper.startPage(pageNum, pageSize, true);
        ArticlePage articlePage = new ArticlePage();
        ArrayList<Article> Articlelist = (ArrayList<Article>) articleService.searchByTypeId(typeId);
        PageInfo<Article> pageInfo = new PageInfo<Article>(Articlelist);
        articlePage.setPageInfo(pageInfo);
        return articlePage;
    };



    private boolean havaSession(HttpServletRequest request){
        User userInfo = (User)request.getSession().getAttribute("userInfo");
        return userInfo == null?false:true;
    }


}
