package com.example.boot1907.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
