package com.example.boot1907.controller;

import com.example.boot1907.Service.IArticleService;
import com.example.boot1907.Service.ICommentService;
import com.example.boot1907.Service.impl.ArticleServiceImpl;
import com.example.boot1907.Service.impl.CommentServiceImpl;
import com.example.boot1907.bo.ArticlePage;
import com.example.boot1907.bo.Article_User_Info;
import com.example.boot1907.bo.CommentPage;
import com.example.boot1907.pojo.Article;
import com.example.boot1907.pojo.Comment;
import com.example.boot1907.pojo.CommentMulti;
import com.example.boot1907.pojo.User;
import com.example.boot1907.vo.CommMulUser;
import com.example.boot1907.vo.CommUser;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther 刘金明
 * @Date 2020/3/4-10:42
 * Description 对文章进行查询
 */
@Controller
@RequestMapping("/profile")
public class profileController {
    @Autowired
    private IArticleService articleService;
    
    @Autowired
    private ICommentService commentService;

    @RequestMapping("/{action}.do")
    public String profile(@PathVariable(name = "action") String action, Model model,
                          HttpServletRequest request,@RequestParam(defaultValue = "1") int pageNum,
                          @RequestParam(defaultValue = "5") int pageSize){
        if(!havaSession(request)) {
            return "register_login";
        }
        PageHelper.startPage(pageNum, pageSize, true);
        ArrayList<Article> Articlelist = null;
        if("questions".equals(action)){
            model.addAttribute("page","questions");
            model.addAttribute("pageName","我的问题");
            Articlelist = (ArrayList<Article>) articleService.getAllQuestions((User)request.getSession().getAttribute("userInfo"));
            PageInfo<Article> pageInfo = new PageInfo<Article>(Articlelist);
            model.addAttribute("Mylist",pageInfo);
        }
        if ("replys".equals(action)){
            model.addAttribute("page","replys");
            model.addAttribute("pageName","我的回复");
            //            articleService.getAllReplys();
        }
        return  "profile";
    }
//通过用户id查找 
    @RequestMapping("/AJreplys.do")
    @ResponseBody
    public CommentPage AJAXreply(HttpServletRequest request,
                                    @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "5") int pageSize){
        PageHelper.startPage(pageNum, pageSize, true);
        CommentPage commentPage = new CommentPage();
        ArrayList<CommUser> CommList = (ArrayList<CommUser>) commentService.getAllComments((User)request.getSession().getAttribute("userInfo"));
        ArrayList<CommMulUser> CommMultiList = (ArrayList<CommMulUser>) commentService.getAllCommentMultis((User)request.getSession().getAttribute("userInfo"));
        PageInfo<CommUser> pageInfo = new PageInfo<CommUser>(CommList);
        PageInfo<CommMulUser> MulpageInfo = new PageInfo<CommMulUser>(CommMultiList);
        commentPage.setPageInfo(pageInfo);
        commentPage.setMultipageInfo(MulpageInfo);
        commentPage.setUser((User)request.getSession().getAttribute("userInfo"));
        return commentPage;
    };

    @RequestMapping("/REquestions.do")
    @ResponseBody
    public ArticlePage AJAXquestion(HttpServletRequest request,
                                    @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "5") int pageSize){
        PageHelper.startPage(pageNum, pageSize, true);
        ArticlePage articlePage = new ArticlePage();
        ArrayList<Article> Articlelist = (ArrayList<Article>) articleService.getAllQuestions((User)request.getSession().getAttribute("userInfo"));
        PageInfo<Article> pageInfo = new PageInfo<Article>(Articlelist);
        articlePage.setPageInfo(pageInfo);
        articlePage.setUser((User)request.getSession().getAttribute("userInfo"));
        return articlePage;
    };
    
//通过文章类型查找
    @RequestMapping("/search.do")
    @ResponseBody
    public ArticlePage search(@RequestParam("typeId") int typeId,
            @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "5") int pageSize){
        PageHelper.startPage(pageNum, pageSize, true);
        ArticlePage articlePage = new ArticlePage();
        ArrayList<Article> Articlelist = (ArrayList<Article>) articleService.searchByTypeId(typeId);
        PageInfo<Article> pageInfo = new PageInfo<Article>(Articlelist);
        articlePage.setPageInfo(pageInfo);
        return articlePage;
    };

//通过文章类型查找 并返回文章及作者信息 watchReplys
    @RequestMapping("/searchByTypeId.do")
    @ResponseBody
    public PageInfo searchByTypeId(@RequestParam("typeId") int typeId,@RequestParam("selectType") String selectType,
                                            @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "5") int pageSize){
        PageHelper.startPage(pageNum, pageSize, true);
        List<Article_User_Info> ArticleUserInfoList = new ArrayList<Article_User_Info>();
        articleService.getArtilcleInfo(typeId,ArticleUserInfoList,selectType);
        PageInfo<Article_User_Info> pageInfo = new PageInfo<>(ArticleUserInfoList);
        return pageInfo;
    };

    //通过关键字查找 并返回文章及作者信息 watchReplys
    @RequestMapping("/searchByKeyWord.do")
    @ResponseBody
    public PageInfo searchByKeyWord(@RequestParam("keyword") String keyword,@RequestParam("selectType") String selectType,
                                   @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "5") int pageSize){
        PageHelper.startPage(pageNum, pageSize, true);
        List<Article_User_Info> ArticleUserInfoList = new ArrayList<Article_User_Info>();
        articleService.searchByKeyWord(keyword,ArticleUserInfoList,selectType);
        PageInfo<Article_User_Info> pageInfo = new PageInfo<>(ArticleUserInfoList);
        return pageInfo;
    };


    @RequestMapping("/watchReplys.do")
    @ResponseBody
    public ModelAndView watchReplys(@RequestParam("comId") Long comId,@RequestParam("comMultiId") Long comMultiId){
        ModelAndView modelAndView = new ModelAndView();
        Long count = null;
        Long artId = null;
        if(comMultiId == -1){
            count = commentService.setWacthed(comId);
            artId = commentService.getArtId(comId);
        }else {
            count = commentService.setWacthed(comId,comMultiId);
            artId = commentService.getArtId(comId,comMultiId);
        }
        if(count == 0 || artId == null){
            modelAndView.setViewName("/error.html");
            modelAndView.addObject("message","帖子查看错误，要不换个试试！！");
            return modelAndView;
        }
        modelAndView.setViewName("redirect:/article/detailArticle.html?artId="+artId);
        return modelAndView;
    };


    private boolean havaSession(HttpServletRequest request){
        User userInfo = (User)request.getSession().getAttribute("userInfo");
        return userInfo == null?false:true;
    }


}
