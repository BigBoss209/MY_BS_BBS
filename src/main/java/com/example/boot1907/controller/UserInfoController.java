package com.example.boot1907.controller;

import com.example.boot1907.Service.IUserInfoService;
import com.example.boot1907.Utils.MyFileUtils;
import com.example.boot1907.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @Auther 刘金明
 * @Date 2020/2/9-10:34
 * Description 处理个人信息
 */
@Controller
@RequestMapping("/UserInfo")
public class UserInfoController {
    @Autowired
    private IUserInfoService userInfoService;

    @ResponseBody
    @PostMapping("/UserImg.do")
    public String UserImg(@RequestParam(value = "projectImg",required = true) MultipartFile file, HttpServletRequest request){
        String UserInfo_msg = "";
        //将图片上传到服务器
        if(file.isEmpty()){
            UserInfo_msg = "项目图片不能为空";
            return UserInfo_msg;

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
                return UserInfo_msg;
            }
            //上传成功后，将可以访问的完整路径返回
            String fullImgpath = "/bs_imgs/"+fileName;
            User pojo = (User) request.getSession().getAttribute("userInfo");
            pojo.setUserImg(fullImgpath);
            userInfoService.upload(pojo);
            UserInfo_msg = "上传图片成功";
            return UserInfo_msg;
        } catch (Exception e) {
            e.printStackTrace();
            //上传图片失败
            UserInfo_msg = "上传项目图片失败";
            return UserInfo_msg ;
        }
    }

    @ResponseBody
    @PostMapping("/changeUserInfo.do")
    public void ChangeUserInfo(User user, BindingResult bindingResult, HttpServletRequest request){
        User pojo = (User)request.getSession().getAttribute("userInfo");
        user.setUserId(pojo.getUserId());
        System.out.println(user);
        userInfoService.changeInfo(user);
    }
}
