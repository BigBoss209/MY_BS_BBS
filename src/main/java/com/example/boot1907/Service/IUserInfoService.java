package com.example.boot1907.Service;

import com.example.boot1907.pojo.User;

public interface IUserInfoService extends IService{
    //上传更改头像图片
    void upload(User pojo);

    //更改信息
    void changeInfo(User pojo);

    User getUserInfo(User pojo);
}
