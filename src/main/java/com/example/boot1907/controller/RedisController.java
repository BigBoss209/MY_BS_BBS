package com.example.boot1907.controller;

import com.example.boot1907.Service.IUserService;
import com.example.boot1907.Service.RedisService;
import com.example.boot1907.pojo.User;
import com.example.boot1907.vo.JsonRespone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Controller
@RequestMapping("/redis")
public class RedisController {
    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private IUserService userService;

    @Autowired
    private RedisService redisService;

    private final String key = "ljm";

    /**
     * @param
     * @return
     */
    @RequestMapping("/LikePut.do")
    @ResponseBody
    public JsonRespone LikePut(@RequestParam("likedArtId") Integer ilikedArtId, HttpServletRequest request){
        JsonRespone jsonRespone = new JsonRespone();
        String likedArtId = ilikedArtId.toString();
        if(!havaSession(request)) {
            jsonRespone.setCode(500);
            jsonRespone.setMsg("请先登录！");
        }else{
            User userInfo = (User)request.getSession().getAttribute("userInfo");
            String likedPostId = userInfo.getUserId().toString();
            if(redisService.IsInRedis(likedArtId,likedPostId)){
//                取消点赞
                redisService.deleteLikedFromRedis(likedArtId,likedPostId);
                redisService.decrementLikedCount(likedArtId);
                jsonRespone.setMsg("取消点赞操作成功！");
            }else {
//                点赞
                redisService.saveLiked2Redis(likedArtId,likedPostId);
                redisService.incrementLikedCount(likedArtId);
                jsonRespone.setMsg("点赞操作成功！");
            }
            jsonRespone.setCode(200);

        }
        return jsonRespone;
    }
    @RequestMapping("/LikeGut.do")
    @ResponseBody
    public String LikeGut(@RequestParam("likedArtId") String likedArtId){
        return redisService.getLikedCount(likedArtId)== null?"0":redisService.getLikedCount(likedArtId);
    }

    @GetMapping("/put.do")
//    @ResponseBody
    public void testPut() {
//        User user = new User();
//        user.setUserName(key);
//        User one = userService.findOne(user);
        redisTemplate.opsForValue().set(key, "success");
        System.out.println("save success");
//        return "save success";
    }
    @GetMapping("/get.do")
    @ResponseBody
    public String restGet() {
        return (String) redisTemplate.opsForValue().get(key);
    }

    private boolean havaSession(HttpServletRequest request){
        User userInfo = (User)request.getSession().getAttribute("userInfo");
        return userInfo == null?false:true;
    }
}
