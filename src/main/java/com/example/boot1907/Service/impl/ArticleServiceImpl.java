package com.example.boot1907.Service.impl;

import com.example.boot1907.Service.IArticleService;
import com.example.boot1907.bo.ArticleInfo;
import com.example.boot1907.dao.IArticleDao;
import com.example.boot1907.pojo.Article;
import com.example.boot1907.pojo.ArticleType;
import com.example.boot1907.pojo.User;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther 刘金明
 * @Date 2020/2/28-21:44
 * Description
 */
@Service
public class ArticleServiceImpl implements IArticleService {
    @Autowired
    private IArticleDao  articleDao;

    //获取文章类型信息
    public List<ArticleType> getArticleType(){
        ArrayList<ArticleType> articleList = (ArrayList) articleDao.getArticleType();
        return articleList;
    }

//    添加文章
    public void addArticle(Article article) {
        articleDao.addArticle(article);
    }

    @Override
    public List<Article> getAllQuestions(User user) {
        return articleDao.getAllQuestions(user);
    }
//获取所有文章数
    public Integer getQuestionsNum(User user) {
        return articleDao.getQuestionsNum(user);
    }
//查看文章内容
    @Override
    public Article editArticle(Article article) {
        return articleDao.editArticle(article);
    }
    //修改文章内容
    @Override
    public void reviseArticle(Article article) {
        articleDao.reviseArticle(article);
    }
    //删除文章内容
    @Override
    public void deleteArticle(Article article) {
        articleDao.deleteArticle(article);
    }
//获取文章信息展示
    @Override
    public void getArticleInfo(ArticleInfo articleInfo) {
        articleInfo.setTotal(articleDao.getArticleInfo_total(articleInfo.getArticleType().getTypeId()));
        articleInfo.setLastReply(articleDao.getArticleInfo_lastReply(articleInfo.getArticleType().getTypeId()));
    }
//根据typeId获取文章
    @Override
    public Object searchByTypeId(int typeId) {
       return articleDao.searchByTypeId(typeId);
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
