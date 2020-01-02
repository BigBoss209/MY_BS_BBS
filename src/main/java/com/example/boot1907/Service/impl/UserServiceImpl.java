package com.example.boot1907.Service.impl;

import com.example.boot1907.Service.IUserService;
import com.example.boot1907.assist.PasswordHelper;
import com.example.boot1907.dao.IUserDao;
import com.example.boot1907.pojo.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Auther 刘金明
 * @Date 2019/11/18-16:03
 * Description
 */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IUserDao userDao;
    @Autowired
    private PasswordHelper passwordHelper;

    @Transactional(readOnly = true)
    public User findOne(User pojo) {
        return userDao.findOne(pojo.getUserName());
    }

    @Transactional
    public void save(User pojo) {
        passwordHelper.encryptPassword(pojo);
        userDao.save(pojo);
    }

    @Transactional
    public void deleteOne(User pojo) {

    }

    @Transactional
    public void updateOne(User pojo) {

    }

    @Transactional(readOnly = true)
    public PageInfo<User> pages(User pojo, int pageStart, int pageSize) {
        PageHelper.startPage(pageStart, pageSize);
        List<User> users = userDao.pages(pojo);
        PageInfo<User> pageInfo = new PageInfo<>(users);
        return pageInfo;
    }

}
