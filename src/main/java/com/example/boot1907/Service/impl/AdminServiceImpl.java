package com.example.boot1907.Service.impl;

import com.example.boot1907.Service.IAdminService;
import com.example.boot1907.assist.PasswordHelper;
import com.example.boot1907.dao.IAdminDao;
import com.example.boot1907.pojo.Article;
import com.example.boot1907.pojo.User;
import com.example.boot1907.vo.AdminArt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Auther 刘金明
 * @Date 2020/3/23-8:47
 * Description
 */
@Service
public class AdminServiceImpl implements IAdminService{
    @Autowired
    IAdminDao adminDao;

    @Autowired
    private PasswordHelper passwordHelper;

    @Override
    public List<User> getUserListInfo() {
        return adminDao.getUserListInfo();
    }

    @Override
    public List<User> selectUser(String startTime, String endTime, User user) {
        return adminDao.selectUser(startTime,endTime,user);
    }

    @Override
    public Integer batchEnabled(List<Integer> arr) {
        return adminDao.batchEnabled(arr);
    }

    @Override
    public Integer batchDisabled(List<Integer> arr) {
        return adminDao.batchDisabled(arr);
    }

    @Override
    public Integer batchDel(List<Integer> arr) {
        return adminDao.batchDel(arr);
    }

    @Transactional
    public Integer save(User user) {
        passwordHelper.encryptPassword(user);

        return adminDao.save(user);
    }

    @Override
    public Integer edit(User user) {
        return adminDao.edit(user);
    }

    @Override
    public User findOne(User user) {
        return adminDao.findOne(user);
    }

    @Override
    public List<AdminArt> getArtListInfo() {
        return adminDao.getArtListInfo();
    }

    @Override
    public List<AdminArt> selectArt(String startTime, String endTime, Article article) {
        return adminDao.selectArt(startTime,endTime,article);
    }

    @Override
    public Integer batchArtDel(List<Integer> arr) {
        return adminDao.batchArtDel(arr);
    }

    @Override
    public Integer batchStop(List<Integer> arr) {
        return adminDao.batchStop(arr);
    }

    @Override
    public Integer batchNormal(List<Integer> arr) {
        return adminDao.batchNormal(arr);
    }

    @Override
    public Article findArtOne(Article article) {
        return adminDao.findArtOne(article);
    }

    @Override
    public Integer editArt(Article article) {
        return adminDao.editArt(article);
    }

    @Override
    public Integer addArt(Article article) {
        return adminDao.addArt(article);
    }
}
