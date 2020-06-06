package com.example.boot1907.controller;

import com.example.boot1907.Service.impl.ArticleServiceImpl;
import com.example.boot1907.Utils.MyFileUtils;
import com.example.boot1907.bo.ArticleInfo;
import com.example.boot1907.pojo.Article;
import com.example.boot1907.pojo.ArticleType;
import com.example.boot1907.vo.AdminArt;
import com.example.boot1907.vo.AdminArtInfo;
import com.example.boot1907.vo.ArticleAndType;
import com.example.boot1907.vo.MarkdownMsg;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Auther 刘金明
 * @Date 2020/3/9-11:10
 * Description
 */
@Controller
@RequestMapping("/Index")
public class IndexController {
    @Autowired
    private ArticleServiceImpl articleService;

    @PostMapping("/getArticleInfo.do")
    @ResponseBody
    public List<ArticleInfo> getArticleInfo(){
        List<ArticleInfo> articleInfoList = new ArrayList<ArticleInfo>();
        List<ArticleType> articleTypeList = articleService.getArticleType();
        for (int i = 0; i < articleTypeList.size(); i++) {
            ArticleInfo articleInfo = new ArticleInfo();
            articleInfo.setArticleType(articleTypeList.get(i));
            articleService.getArticleInfo(articleInfo);
            articleInfoList.add(articleInfo);
        }
        return articleInfoList;
    }

    @RequestMapping("/{pageName}.html")
    public ModelAndView goPage(@PathVariable("pageName") String pageName, @Param("typeId") Long typeId){
        ModelAndView modelAndView = new ModelAndView();
//        还可以添加错误页面
        if(typeId == null){
            modelAndView.setViewName("error.html");
            modelAndView.addObject("message","文章类型错误");
            return modelAndView;
        }
        if(articleService.isType(typeId)){
            modelAndView.setViewName("error.html");
            modelAndView.addObject("message","文章类型错误");
            return modelAndView;
        }
        modelAndView.setViewName("/category/"+pageName+".html");
        return modelAndView;
    }


    @RequestMapping("/uploadFile.do")
    @ResponseBody
    public MarkdownMsg uploadFile(@RequestParam(value = "editormd-image-file", required = true) MultipartFile file){
        MarkdownMsg markdownMsg = new MarkdownMsg();
        String UserInfo_msg = "";
        //将图片上传到服务器
        if(file.isEmpty()){
            UserInfo_msg = "项目图片不能为空";
            markdownMsg.setMessage(UserInfo_msg);
            markdownMsg.setSuccess(0);
            return markdownMsg;

        }
        //原始文件名
        String originalFilename = file.getOriginalFilename();
        //文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        //图片名称为uuid+图片后缀防止冲突
        String fileName = UUID.randomUUID().toString()+"."+suffix;
        String os = System.getProperty("os.name");
        //文件保存路径
        String filePath="";
        if(os.toLowerCase().startsWith("win")){
            //windows下的路径
            filePath ="d:/bs_imgs/";
        }else {
            //linux下的路径
            filePath="/root/bs_imgs/";
        }
        try {
            //写入图片
            Boolean writePictureflag = MyFileUtils.uploadFile(file.getBytes(),filePath,fileName);
            if(writePictureflag == false){
                //上传图片失败
                UserInfo_msg = "上传图片失败";
                markdownMsg.setMessage(UserInfo_msg);
                markdownMsg.setSuccess(0);
                return markdownMsg;
            }
            //上传成功后，将可以访问的完整路径返回
            String fullImgpath = "/bs_imgs/"+fileName;

            markdownMsg.setUrl(fullImgpath);

            UserInfo_msg = "上传图片成功";
            markdownMsg.setSuccess(1);
            markdownMsg.setMessage(UserInfo_msg);
            return markdownMsg;
        } catch (Exception e) {
            e.printStackTrace();
            //上传图片失败
            UserInfo_msg = "上传项目图片失败";
            markdownMsg.setSuccess(0);
            markdownMsg.setMessage(UserInfo_msg);
            return markdownMsg;
        }
    }

    @RequestMapping("/getNewContent.do")
    @ResponseBody
    public List<ArticleAndType> getNewContent(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "6") int pageSize){
        List<ArticleAndType> articleList = new ArrayList<>();
        Page page = PageHelper.startPage(pageNum, pageSize, true);
        articleList = articleService.getNewContent();
        return articleList;
    }
    @RequestMapping("/getNewReply.do")
    @ResponseBody
    public List<ArticleAndType> getNewReply(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "6") int pageSize){
        List<ArticleAndType> articleList = new ArrayList<>();
        Page page = PageHelper.startPage(pageNum, pageSize, true);
        articleList = articleService.getNewReply();
        return articleList;
    }
    @RequestMapping("/getMostLike.do")
    @ResponseBody
    public List<ArticleAndType> getMostLike(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "6") int pageSize){
        List<ArticleAndType> articleList = new ArrayList<>();
        Page page = PageHelper.startPage(pageNum, pageSize, true);
        articleList = articleService.getMostLike();
        return articleList;
    }
    @RequestMapping("/getMostReply.do")
    @ResponseBody
    public List<ArticleAndType> getMostReply(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "6") int pageSize){
        List<ArticleAndType> articleList = new ArrayList<>();
        Page page = PageHelper.startPage(pageNum, pageSize, true);
        articleList = articleService.getMostReply();
        return articleList;
    }
}
