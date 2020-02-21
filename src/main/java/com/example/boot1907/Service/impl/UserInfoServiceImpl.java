package com.example.boot1907.Service.impl;

import com.example.boot1907.Service.IUserInfoService;
import com.example.boot1907.dao.IUserDao;
import com.example.boot1907.pojo.User;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther 刘金明
 * @Date 2020/2/9-12:25
 * Description
 */
@Service
public class UserInfoServiceImpl implements IUserInfoService {

    @Autowired
    private IUserDao userDao;

    @Override
    public void upload(User pojo) {
        userDao.updateImg(pojo);
    }

    @Override
    public void changeInfo(User pojo) {
        userDao.changeInfo(pojo);
    }

    @Override
    public Object findOne(Object pojo) {
        return null;
    }

    @Override
    public void save(Object pojo) {

    }

    @Override
    public void deleteOne(Object pojo) {

    }

    @Override
    public void updateOne(Object pojo) {

    }

    @Override
    public PageInfo pages(Object pojo, int pageStart, int pageSize) {
        return null;
    }
}
