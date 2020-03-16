package com.example.boot1907.bo;

import com.example.boot1907.pojo.Comment;
import com.example.boot1907.pojo.User;
import lombok.Data;

/**
 * @Auther 刘金明
 * @Date 2020/3/13-15:50
 * Description
 */
@Data
public class CommInfo {
    private User user;
    private Comment comment;
}
