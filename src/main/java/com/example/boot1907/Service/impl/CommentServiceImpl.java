package com.example.boot1907.Service.impl;

import com.example.boot1907.Service.ICommentService;
import com.example.boot1907.bo.CommInfo;
import com.example.boot1907.bo.UserCommMulti;
import com.example.boot1907.dao.ICommentDao;
import com.example.boot1907.dao.ICommentMultiDao;
import com.example.boot1907.pojo.Article;
import com.example.boot1907.pojo.Comment;
import com.example.boot1907.pojo.CommentMulti;
import com.example.boot1907.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Auther 刘金明
 * @Date 2020/3/12-23:16
 * Description
 */
@Service
@Transactional
public class CommentServiceImpl implements ICommentService {

    @Autowired
    ICommentDao commentDao;
    @Autowired
    ICommentMultiDao commentMultiDao;

//    通过文章id查找一级评论
    @Override
    public  List<CommInfo> getCommByArtId(Integer artId) {
        return commentDao.getCommByArtId(artId);
    }

    @Override
    public User getAutherByArtId(Integer artId) {
        return commentDao.getAutherByArtId(artId);
    }

    @Override
    public Article getArticleByArtId(Integer artId) {
        return commentDao.getArticleByArtId(artId);
    }

//    获取二级评论
    @Override
    public List<UserCommMulti> getCommMultiByArtId(Integer commId) {
        return commentMultiDao.getCommMultiByArtId(commId);
    }

    @Override
    public Integer addSeComm(CommentMulti commentMulti) {
        return commentMultiDao.addSeComm(commentMulti);
    }

    @Override
    public Integer addComm(Comment comment) {
        return commentDao.addComm(comment);
    }
}
