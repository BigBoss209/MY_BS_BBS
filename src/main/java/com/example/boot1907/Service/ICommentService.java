package com.example.boot1907.Service;

import com.example.boot1907.bo.CommInfo;
import com.example.boot1907.bo.UserCommMulti;
import com.example.boot1907.pojo.Article;
import com.example.boot1907.pojo.Comment;
import com.example.boot1907.pojo.CommentMulti;
import com.example.boot1907.pojo.User;
import com.example.boot1907.vo.CommMulUser;
import com.example.boot1907.vo.CommUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

public interface ICommentService{

    List<CommInfo> getCommByArtId(Integer artId);

    User getAutherByArtId(Integer artId);

    Article getArticleByArtId(Integer artId);

    List<UserCommMulti> getCommMultiByArtId(Integer commId);

    Integer addSeComm(CommentMulti commentMulti);

    Integer addComm(Comment comment);

    ArrayList<CommUser> getAllComments(User userInfo);

    ArrayList<CommMulUser> getAllCommentMultis(User userInfo);

    Long setWacthed(Long comId);

    Long setWacthed(Long comId,Long comMultiId);

    Long getArtId(Long comId);

    Long getArtId(Long comId,Long comMultiId);
}
