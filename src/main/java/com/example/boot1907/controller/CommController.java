package com.example.boot1907.controller;

import com.example.boot1907.Service.ICommentService;
import com.example.boot1907.bo.CommInfo;
import com.example.boot1907.bo.UserComm;
import com.example.boot1907.bo.UserCommMulti;
import com.example.boot1907.pojo.Article;
import com.example.boot1907.pojo.Comment;
import com.example.boot1907.pojo.CommentMulti;
import com.example.boot1907.pojo.User;
import oracle.jdbc.proxy.annotation.Post;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @Auther 刘金明
 * @Date 2020/3/11-11:29
 * Description
 */
@Controller
@RequestMapping("/comm")
public class CommController {

    @Autowired
    ICommentService commentService;

    //    获取评论
    @PostMapping("/searchCommByArtId.do")
    @ResponseBody
    public UserComm searchCommByArtId(@Param("artId") Integer artId){
        UserComm userComm = new UserComm();
        List<CommInfo> commInfos = commentService.getCommByArtId(artId);
        User user = commentService.getAutherByArtId(artId);
        Article article = commentService.getArticleByArtId(artId);
        userComm.setCommInfos(commInfos);
        userComm.setUser(user);
        userComm.setArticle(article);
        return userComm;
    }

    @PostMapping("/searchSeCommByArtId.do")
    @ResponseBody
    public List<UserCommMulti> getCommMultiByArtId(@Param("commId") Integer commId){
        List<UserCommMulti> userCommMultis = commentService.getCommMultiByArtId(commId);
       return userCommMultis;
    }

    @PostMapping("/addSeComm.do")
    @ResponseBody
    public User addSeComm(@Param("comMultiContent") String comMultiContent
            ,@Param("comId") Long comId, HttpServletRequest request){
        CommentMulti commentMulti = new CommentMulti();
        commentMulti.setComId(comId);
        commentMulti.setComMultiContent(comMultiContent);
        commentMulti.setComMultiTime(new Date());
        User userInfo = (User)request.getSession().getAttribute("userInfo");
        commentMulti.setComMultiUserId(userInfo.getUserId());
//        受影响行数
        Integer count = commentService.addSeComm(commentMulti);
        String msg = "defult";
        if(count > 0){
            msg = "success";
        }
        userInfo.setValid(msg);
       return userInfo;
    }


    @PostMapping("/addComm.do")
    @ResponseBody
    public User addComm(@Param("comMultiContent") String comMultiContent
            ,@Param("artId") Long artId, HttpServletRequest request){
        Comment comment = new Comment();
        comment.setComArtId(artId);
        comment.setComContent(comMultiContent);
        comment.setComTime(new Date());
        User userInfo = (User)request.getSession().getAttribute("userInfo");
        comment.setComUserId(userInfo.getUserId());
//        受影响行数
        Integer count = commentService.addComm(comment);

        String msg = "defult";
        if(count > 0){
            msg = "success";
        }
        userInfo.setValid(msg);
        userInfo.setUserFans(comment.getComId());
        return userInfo;
    }

    //    判断用户session是否有效
    private boolean havaSession(HttpServletRequest request){
        User userInfo = (User)request.getSession().getAttribute("userInfo");
        return userInfo == null?false:true;
    }
}
