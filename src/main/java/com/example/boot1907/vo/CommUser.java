package com.example.boot1907.vo;

import com.example.boot1907.pojo.Comment;
import com.example.boot1907.pojo.User;
import lombok.Data;

/**
 * @Auther 刘金明
 * @Date 2020/3/17-15:34
 * Description
 */
@Data
public class CommUser {
    private Comment comment;
    private User user;
}
