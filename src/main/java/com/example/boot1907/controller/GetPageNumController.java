package com.example.boot1907.controller;

import com.example.boot1907.Service.impl.ArticleServiceImpl;
import com.example.boot1907.Utils.PageNumUtils;
import com.example.boot1907.bo.PageNum;
import com.example.boot1907.pojo.Article;
import com.example.boot1907.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * @Auther 刘金明
 * @Date 2020/3/6-12:26
 * Description 处理分页  一页5条数据
 */
@Controller
@RequestMapping("/PageNum")
public class GetPageNumController {
    @Autowired
    private ArticleServiceImpl articleService;

    @RequestMapping("/getPageNum.do")
    @ResponseBody
    public PageNum getPageNum(@RequestParam("pageName") String pageName, HttpServletRequest request){
        PageNum pageNum = new PageNum();
        Integer Num = null;

//        if(request.getSession().getAttribute("userInfo") == null) return;

        if("questions".equals(pageName)){
            Integer count = articleService.getQuestionsNum((User)request.getSession().getAttribute("userInfo"));
            Num = count/ PageNumUtils.ARTICLE_PAGENUM ;
            if (count % PageNumUtils.ARTICLE_PAGENUM != 0){
                Num++;
            }
            pageNum.setPageName(pageName);
        }
        if(Num != null) pageNum.setPageNum(Num);
        return pageNum;
    };



    private boolean havaSession(HttpServletRequest request){
        User userInfo = (User)request.getSession().getAttribute("userInfo");
        return userInfo == null?false:true;
    }
}
