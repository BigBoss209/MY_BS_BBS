package com.example.boot1907.bo;

import com.example.boot1907.pojo.User;
import com.example.boot1907.vo.CommMulUser;
import com.example.boot1907.vo.CommUser;
import com.github.pagehelper.PageInfo;
import lombok.Data;

/**
 * @Auther 刘金明
 * @Date 2020/3/17-14:49
 * Description
 */
@Data
public class CommentPage {
    private PageInfo<CommUser> pageInfo;
    private PageInfo<CommMulUser> MultipageInfo;
    private User user;

}
