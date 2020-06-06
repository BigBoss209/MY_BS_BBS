package com.example.boot1907.Service;

import com.example.boot1907.pojo.User;
import com.example.boot1907.vo.adminGetNum;


/**
 * @Auther 刘金明
 * @Date 2019/11/18-16:02
 * Description
 */
public interface IUserService extends IService<User>{

    adminGetNum getNumInfo();

    void updateCon(String likedPostId);
}
