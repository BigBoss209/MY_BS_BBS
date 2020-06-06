package com.example.boot1907.Service;

import com.example.boot1907.pojo.Article;
import com.example.boot1907.pojo.User;
import com.example.boot1907.vo.AdminArt;

import java.util.Date;
import java.util.List;

public interface IAdminService {

    List<User> getUserListInfo();

    List<User> selectUser(String startTime, String endTime, User user);

    Integer batchEnabled(List<Integer> arr);

    Integer batchDisabled(List<Integer> arr);

    Integer batchDel(List<Integer> arr);

    Integer save(User user);

    Integer edit(User user);

    User findOne(User user);

    List<AdminArt> getArtListInfo();

    List<AdminArt> selectArt(String startTime, String endTime, Article article);

    Integer batchArtDel(List<Integer> arr);

    Integer batchStop(List<Integer> arr);

    Integer batchNormal(List<Integer> arr);

    Article findArtOne(Article article);

    Integer editArt(Article article);

    Integer addArt(Article article);
}
