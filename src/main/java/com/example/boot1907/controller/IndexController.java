package com.example.boot1907.controller;

import com.example.boot1907.Service.impl.ArticleServiceImpl;
import com.example.boot1907.bo.ArticleInfo;
import com.example.boot1907.pojo.ArticleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther 刘金明
 * @Date 2020/3/9-11:10
 * Description
 */
@Controller
@RequestMapping("/Index")
public class IndexController {
    @Autowired
    private ArticleServiceImpl articleService;

    @PostMapping("/getArticleInfo.do")
    @ResponseBody
    public List<ArticleInfo> getArticleInfo(){
        List<ArticleInfo> articleInfoList = new ArrayList<ArticleInfo>();
        List<ArticleType> articleTypeList = articleService.getArticleType();
        for (int i = 0; i < articleTypeList.size(); i++) {
            ArticleInfo articleInfo = new ArticleInfo();
            articleInfo.setArticleType(articleTypeList.get(i));
            articleService.getArticleInfo(articleInfo);
            articleInfoList.add(articleInfo);
        }
        return articleInfoList;
    }
}
