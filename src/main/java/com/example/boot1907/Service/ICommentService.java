package com.example.boot1907.Service;

import com.example.boot1907.bo.CommInfo;
import com.example.boot1907.bo.UserCommMulti;
import com.example.boot1907.pojo.Article;
import com.example.boot1907.pojo.Comment;
import com.example.boot1907.pojo.CommentMulti;
import com.example.boot1907.pojo.User;

import java.util.List;

public interface ICommentService{

    List<CommInfo> getCommByArtId(Integer artId);

    User getAutherByArtId(Integer artId);

    Article getArticleByArtId(Integer artId);

    List<UserCommMulti> getCommMultiByArtId(Integer commId);

    Integer addSeComm(CommentMulti commentMulti);

    Integer addComm(Comment comment);
}
