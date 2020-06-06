package com.example.boot1907.controller;

import com.example.boot1907.Service.IAdminService;
import com.example.boot1907.Service.IUserService;
import com.example.boot1907.Utils.UserCheck;
import com.example.boot1907.pojo.Article;
import com.example.boot1907.pojo.User;
import com.example.boot1907.vo.AdminArt;
import com.example.boot1907.vo.AdminArtInfo;
import com.example.boot1907.vo.AdminUserInfo;
import com.example.boot1907.vo.JsonRespone;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther 刘金明
 * @Date 2020/3/20-10:22
 * Description
 */
@Controller
@RequestMapping("/Admin")
public class AdminController {
    @Autowired
    IAdminService adminService;

    @Autowired
    private IUserService userService;


    @RequestMapping("/getUserInfo.do")
    @ResponseBody
    public AdminUserInfo getUserListInfo(HttpServletRequest request,
                                         @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize){
        AdminUserInfo adminUserInfo = new AdminUserInfo();
        if(!havaSession(request)) {
            adminUserInfo.setCode(500);
            adminUserInfo.setMsg("请先登录！");
        }else {
            adminUserInfo.setCode(200);
            adminUserInfo.setMsg("查询成功");
            Page page = PageHelper.startPage(pageNum, pageSize, true);
            adminUserInfo.setData(adminService.getUserListInfo());
            adminUserInfo.setCount(page.getTotal());
        }
        return adminUserInfo;
    }

    @RequestMapping("/selectUser.do")
    @ResponseBody
    public AdminUserInfo selectUser(HttpServletRequest request, @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime,
                                    User user ,@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize){
        AdminUserInfo adminUserInfo = new AdminUserInfo();
        if(!havaSession(request)) {
            adminUserInfo.setCode(500);
            adminUserInfo.setMsg("请先登录！");
        }else {
            adminUserInfo.setCode(200);
            adminUserInfo.setMsg("查询成功");
            Page page = PageHelper.startPage(pageNum, pageSize, true);
            adminUserInfo.setData(adminService.selectUser(startTime,endTime,user));
            adminUserInfo.setCount(page.getTotal());
        }
        return adminUserInfo;
    }

    @RequestMapping("/batchEnabled.do")
    @ResponseBody
    public JsonRespone batchEnabled(HttpServletRequest request, @RequestParam("idsStr") String idsStr) {
        JsonRespone jsonRespone = new JsonRespone();
        List<Integer> arr = new ArrayList<>();
        if(idsStr != null && !"".equals(idsStr)){
            String[] strs = idsStr.split(",");
            for (int i = 0; i < strs.length; i++) {
                if(strs[i] != null && !"".equals(strs[i])) arr.add(Integer.parseInt(strs[i]));
            }
        }
        if(!havaSession(request)) {
            jsonRespone.setCode(501);
            jsonRespone.setMsg("请先登录！");
        }else {
            Integer count = adminService.batchEnabled(arr);
            jsonRespone.setCode(200);
            jsonRespone.setMsg("批量启用成功");
        }
        return jsonRespone;
    }

    @RequestMapping("/batchDisabled.do")
    @ResponseBody
    public JsonRespone batchDisabled(HttpServletRequest request, @RequestParam("idsStr") String idsStr) {
        JsonRespone jsonRespone = new JsonRespone();
        List<Integer> arr = new ArrayList<>();
        if(idsStr != null && !"".equals(idsStr)){
            String[] strs = idsStr.split(",");
            for (int i = 0; i < strs.length; i++) {
                if(strs[i] != null && !"".equals(strs[i])) arr.add(Integer.parseInt(strs[i]));
            }
        }
        if(!havaSession(request)) {
            jsonRespone.setCode(501);
            jsonRespone.setMsg("请先登录！");
        }else {
            Integer count = adminService.batchDisabled(arr);
            jsonRespone.setCode(200);
            jsonRespone.setMsg("批量停止成功");
        }
        return jsonRespone;
    }

    @RequestMapping("/batchDel.do")
    @ResponseBody
    public JsonRespone batchDel(HttpServletRequest request, @RequestParam("idsStr") String idsStr) {
        JsonRespone jsonRespone = new JsonRespone();
        List<Integer> arr = new ArrayList<>();
        if(idsStr != null && !"".equals(idsStr)){
            String[] strs = idsStr.split(",");
            for (int i = 0; i < strs.length; i++) {
                if(strs[i] != null && !"".equals(strs[i])) arr.add(Integer.parseInt(strs[i]));
            }
        }
        if(!havaSession(request)) {
            jsonRespone.setCode(501);
            jsonRespone.setMsg("请先登录！");
        }else {
            Integer count = adminService.batchDel(arr);
            jsonRespone.setCode(200);
            jsonRespone.setMsg("批量删除成功");
        }
        return jsonRespone;
    }

    @PostMapping("/AdminRegister.do")
    @ResponseBody
    public JsonRespone AdminRegister(User user) {
        JsonRespone jsonRespone = new JsonRespone();
        User dbUser = userService.findOne(user);
        if(dbUser != null){
            System.out.println(dbUser);
            jsonRespone.setCode(302);
            jsonRespone.setMsg("用户名重复");
        }else {
            if(UserCheck.checkName(user.getUserName())&&UserCheck.checkPasswork(user.getUserPassword())){
                //注册用户
                user.setUserTime(new Date());
                Integer count = adminService.save(user);
                if(count != null && count != 0){
                    jsonRespone.setCode(200);
                    jsonRespone.setMsg("注册成功！");
                }else {
                    jsonRespone.setCode(500);
                    jsonRespone.setMsg("注册失败，请联系管理员");
                }
            }else {
                jsonRespone.setCode(500);
                jsonRespone.setMsg("注册失败，请联系管理员");
            }
        }
        return jsonRespone;
    }

    @PostMapping("/AdminEdit.do")
    @ResponseBody
    public JsonRespone AdminEdit(User user) {
        JsonRespone jsonRespone = new JsonRespone();
        User dbUser = adminService.findOne(user);
        if(dbUser == null){
            jsonRespone.setCode(202);
            jsonRespone.setMsg("用户不存在");
        }else {
            if(UserCheck.checkName(user.getUserName())){
                //注册用户
                Integer count = adminService.edit(user);
                if(count != null && count != 0){
                    jsonRespone.setCode(200);
                    jsonRespone.setMsg("编辑成功！");
                }else {
                    jsonRespone.setCode(500);
                    jsonRespone.setMsg("编辑失败");
                }

            }else {
                jsonRespone.setCode(500);
                jsonRespone.setMsg("编辑失败");
            }
        }
        return jsonRespone;
    }

    @RequestMapping("/getArtInfo.do")
    @ResponseBody
    public AdminArtInfo getArtInfo(HttpServletRequest request,
                                  @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {
        AdminArtInfo adminArtInfo = new AdminArtInfo();
        if(!havaSession(request)) {
            adminArtInfo.setCode(500);
            adminArtInfo.setMsg("请先登录！");
        }else {
            Page page = PageHelper.startPage(pageNum, pageSize, true);
            List<AdminArt> artListInfo = adminService.getArtListInfo();
            for (int i = 0; i < artListInfo.size(); i++) {
                artListInfo.get(i).setUrl("/article/detailArticle.html?artId="+artListInfo.get(i).getArtId());
            }
            adminArtInfo.setData(artListInfo);
            adminArtInfo.setCount(page.getTotal());
            adminArtInfo.setCode(200);
            adminArtInfo.setMsg("查询成功");
        }
        return adminArtInfo;
    }

    @RequestMapping("/selectArt.do")
    @ResponseBody
    public AdminArtInfo selectArt(HttpServletRequest request, @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime,
                                   Article article , @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize){
        AdminArtInfo adminArtInfo = new AdminArtInfo();
        if(!havaSession(request)) {
            adminArtInfo.setCode(500);
            adminArtInfo.setMsg("请先登录！");
        }else {
            Page page = PageHelper.startPage(pageNum, pageSize, true);
            List<AdminArt> artListInfo = adminService.selectArt(startTime,endTime,article);
            for (int i = 0; i < artListInfo.size(); i++) {
                artListInfo.get(i).setUrl("/article/detailArticle.html?artId="+artListInfo.get(i).getArtId());
            }
            adminArtInfo.setData(artListInfo);
            adminArtInfo.setCount(page.getTotal());
            adminArtInfo.setCode(200);
            adminArtInfo.setMsg("查询成功");
        }
        return adminArtInfo;
    }


    @RequestMapping("/batchNormal.do")
    @ResponseBody
    public JsonRespone batchNormal(HttpServletRequest request, @RequestParam("idsStr") String idsStr) {
        JsonRespone jsonRespone = new JsonRespone();
        List<Integer> arr = new ArrayList<>();
        if(idsStr != null && !"".equals(idsStr)){
            String[] strs = idsStr.split(",");
            for (int i = 0; i < strs.length; i++) {
                if(strs[i] != null && !"".equals(strs[i])) arr.add(Integer.parseInt(strs[i]));
            }
        }
        if(!havaSession(request)) {
            jsonRespone.setCode(501);
            jsonRespone.setMsg("请先登录！");
        }else {
            Integer count = adminService.batchNormal(arr);
            jsonRespone.setCode(200);
            jsonRespone.setMsg("批量启用成功");
        }
        return jsonRespone;
    }

    @RequestMapping("/batchStop.do")
    @ResponseBody
    public JsonRespone batchStop(HttpServletRequest request, @RequestParam("idsStr") String idsStr) {
        JsonRespone jsonRespone = new JsonRespone();
        List<Integer> arr = new ArrayList<>();
        if(idsStr != null && !"".equals(idsStr)){
            String[] strs = idsStr.split(",");
            for (int i = 0; i < strs.length; i++) {
                if(strs[i] != null && !"".equals(strs[i])) arr.add(Integer.parseInt(strs[i]));
            }
        }
        if(!havaSession(request)) {
            jsonRespone.setCode(501);
            jsonRespone.setMsg("请先登录！");
        }else {
            Integer count = adminService.batchStop(arr);
            jsonRespone.setCode(200);
            jsonRespone.setMsg("批量停止成功");
        }
        return jsonRespone;
    }

    @RequestMapping("/batchArtDel.do")
    @ResponseBody
    public JsonRespone batchArtDel(HttpServletRequest request, @RequestParam("idsStr") String idsStr) {
        JsonRespone jsonRespone = new JsonRespone();
        List<Integer> arr = new ArrayList<>();
        if(idsStr != null && !"".equals(idsStr)){
            String[] strs = idsStr.split(",");
            for (int i = 0; i < strs.length; i++) {
                if(strs[i] != null && !"".equals(strs[i])) arr.add(Integer.parseInt(strs[i]));
            }
        }
        if(!havaSession(request)) {
            jsonRespone.setCode(501);
            jsonRespone.setMsg("请先登录！");
        }else {
            Integer count = adminService.batchArtDel(arr);
            jsonRespone.setCode(200);
            jsonRespone.setMsg("批量删除成功");
        }
        return jsonRespone;
    }

    @PostMapping("/editArt.do")
    @ResponseBody
    public JsonRespone editArt(Article article) {
        JsonRespone jsonRespone = new JsonRespone();
        Article dbArt = adminService.findArtOne(article);
        if(dbArt == null){
            jsonRespone.setCode(202);
            jsonRespone.setMsg("文章不存在");
        }else {
                article.setArtCreTime(new Date());
                Integer count = adminService.editArt(article);
                if(count != null && count != 0){
                    jsonRespone.setCode(200);
                    jsonRespone.setMsg("编辑成功！");
                }else {
                    jsonRespone.setCode(500);
                    jsonRespone.setMsg("编辑失败");
                }

        }
        return jsonRespone;
    }

    @PostMapping("/addArt.do")
    @ResponseBody
    public JsonRespone addArt(HttpServletRequest request,Article article) {
        JsonRespone jsonRespone = new JsonRespone();
        if (havaSession(request)){
            article.setArtCreTime(new Date());
            User userInfo = (User)request.getSession().getAttribute("userInfo");
            article.setArtUserId(userInfo.getUserId());
            Integer count = adminService.addArt(article);
            if(count != null && count != 0){
                jsonRespone.setCode(200);
                jsonRespone.setMsg("添加成功！");
            }else {
                jsonRespone.setCode(500);
                jsonRespone.setMsg("添加失败");
            }
        }else {
            jsonRespone.setCode(501);
            jsonRespone.setMsg("请先登录！");
        }

        return jsonRespone;
    }

    private boolean havaSession(HttpServletRequest request){
        User userInfo = (User)request.getSession().getAttribute("userInfo");
        return userInfo == null?false:true;
    }
}
