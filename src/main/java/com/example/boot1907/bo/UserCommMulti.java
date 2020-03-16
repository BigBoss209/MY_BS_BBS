package com.example.boot1907.bo;

import com.example.boot1907.pojo.CommentMulti;
import com.example.boot1907.pojo.User;
import lombok.Data;

/**
 * @Auther 刘金明
 * @Date 2020/3/13-17:47
 * Description
 */
@Data
public class UserCommMulti {
    private User user;
    private CommentMulti commentMulti;
}
