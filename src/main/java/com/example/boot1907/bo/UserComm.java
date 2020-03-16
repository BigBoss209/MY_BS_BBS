package com.example.boot1907.bo;

import com.example.boot1907.pojo.Article;
import com.example.boot1907.pojo.Comment;
import com.example.boot1907.pojo.User;
import lombok.Data;

import java.util.List;

/**
 * @Auther 刘金明
 * @Date 2020/3/12-23:27
 * Description
 */
@Data
public class UserComm{
//    文章作者信息
    private User user;
//    评论和用户信息
    private List<CommInfo> CommInfos;
//    文章信息
    private Article article;
}
