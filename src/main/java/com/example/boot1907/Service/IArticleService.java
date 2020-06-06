package com.example.boot1907.Service;
import com.example.boot1907.bo.ArticleInfo;
import com.example.boot1907.bo.Article_User_Info;
import com.example.boot1907.pojo.Article;
import com.example.boot1907.pojo.ArticleType;
import com.example.boot1907.pojo.User;

import java.util.List;

public interface IArticleService extends IService{
     List<ArticleType> getArticleType();

    void addArticle(Article article);

    List<Article> getAllQuestions(User user);

    Integer getQuestionsNum(User user);

    Article editArticle(Article article);

    void reviseArticle(Article article);

    void deleteArticle(Article article);

    void getArticleInfo(ArticleInfo articleInfo);

    Object searchByTypeId(int typeId);

    boolean isType(long typeId);

    void getArtilcleInfo(int typeId, List<Article_User_Info> articleUserInfoList,String selectType);

    boolean isArt(Integer artId);

    void searchByKeyWord(String keyword, List<Article_User_Info> articleUserInfoList, String selectType);

    void updatelikeInfo(Article art);

    Article searchByArtId(int parseInt);
}
