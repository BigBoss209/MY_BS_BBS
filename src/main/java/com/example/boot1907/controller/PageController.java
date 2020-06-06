package com.example.boot1907.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

/**
 * @Auther 刘金明
 * @Date 2019/11/18-10:55
 * Description
 */
@Controller
public class PageController {
    @RequestMapping("/{pageName}.html")
    public String goPage(@PathVariable("pageName") String pageName){
        return pageName;
    }

    @RequestMapping("/article/{pageName}.html")
    public ModelAndView goArticlePage(@PathVariable("pageName") String pageName,@Param("artId") Long artId){
        ModelAndView modelAndView = new ModelAndView();
        if(artId == null){
            modelAndView.setViewName("/error.html");
            modelAndView.addObject("message","文章类型错误");
            return modelAndView;
        }
        modelAndView.setViewName("/Article/"+pageName+".html");
        return modelAndView;
    }

    @RequestMapping("/search/AllSearch.html")
    public ModelAndView goArticlePage(@Param("keyword") String keyword){
        ModelAndView modelAndView = new ModelAndView();
        if(keyword == null || "".equals(keyword)){
            modelAndView.setViewName("/error.html");
            modelAndView.addObject("message","查询错误");
            return modelAndView;
        }
        modelAndView.setViewName("/com/AllSearch.html");
        return modelAndView;
    }

    @RequestMapping("/admin/{dirName}/{pageName}.html")
    public String goAdminPage(@PathVariable("dirName") String dirName,@PathVariable("pageName") String pageName){
        String path = "/AdminPage/"+dirName+"/"+pageName+".html";
        return path;
    }

    @RequestMapping("/admin/{pageName}.html")
    public String goAdminIndexPage(@PathVariable("pageName") String pageName){
        String path = "/AdminPage/"+pageName+".html";
        return path;
    }
}
