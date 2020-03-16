package com.example.boot1907.bo;

import com.example.boot1907.pojo.Article;
import com.example.boot1907.pojo.User;
import lombok.Data;

/**
 * @Auther 刘金明
 * @Date 2020/3/10-11:09
 * Description 返回文章及作者信息
 */
@Data
public class Article_User_Info {
    private User user;
    private Article article;
}
