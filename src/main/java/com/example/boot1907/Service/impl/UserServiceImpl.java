package com.example.boot1907.Service.impl;

import com.example.boot1907.Service.IUserService;
import com.example.boot1907.assist.PasswordHelper;
import com.example.boot1907.dao.IArticleDao;
import com.example.boot1907.dao.ICommentDao;
import com.example.boot1907.dao.IUserDao;
import com.example.boot1907.pojo.User;
import com.example.boot1907.vo.adminGetNum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
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
    @Autowired
    private IArticleDao articleDao;
    @Autowired
    ICommentDao commentDao;

    @Transactional(readOnly = true)
    public User findOne(User pojo) {
        return userDao.findOne(pojo.getUserName());
    }

    @Transactional
    public void save(User pojo) {
        passwordHelper.encryptPassword(pojo);
//        System.out.println(pojo);
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

    @Override
    public adminGetNum getNumInfo() {
        adminGetNum adminGetNum = new adminGetNum();
        adminGetNum.setUserNum(userDao.getNum());
        adminGetNum.setArticleNum(articleDao.getNum());
        adminGetNum.setCommNum(commentDao.getNum());
        adminGetNum.setIpAddr(getIpAdder());
        return adminGetNum;

    }

    @Override
    public void updateCon(String likedPostId) {
        userDao.updateCon(Integer.parseInt(likedPostId));
    }

    private String getIpAdder(){
        String str = "";
        Enumeration allNetInterfaces = null;
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        InetAddress ip = null;
        while (allNetInterfaces.hasMoreElements())
        {
            NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
            Enumeration addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements())
            {
                ip = (InetAddress) addresses.nextElement();
                if (ip != null && ip instanceof Inet4Address)
                {
                    if("wlan0".equals(netInterface.getName()))
                    str = ip.getHostAddress();
                }
            }
        }
        return str;
    }

}
