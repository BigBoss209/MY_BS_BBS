package com.example.boot1907.bo;

import com.example.boot1907.pojo.Article;
import com.example.boot1907.pojo.User;
import com.github.pagehelper.PageInfo;
import lombok.Data;

/**
 * @Auther 刘金明
 * @Date 2020/3/6-17:06
 * Description
 */
@Data
public class ArticlePage {
    private PageInfo<Article> pageInfo;
    private User user;
}
