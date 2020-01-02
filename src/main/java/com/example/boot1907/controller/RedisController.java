package com.example.boot1907.controller;

import com.example.boot1907.Service.IUserService;
import com.example.boot1907.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@RestController
public class RedisController {
    @Autowired
    private RedisTemplate<Serializable, Object> redisTemplate;
    @Autowired
    private IUserService userService;

    private final String key = "ljm";

    @GetMapping("/redis/put")
    public String testPut() {
        User user = new User();
        user.setUserName(key);
        User one = userService.findOne(user);
        redisTemplate.opsForValue().set(key, one);
        return "save success";
    }
    @GetMapping("/redis/get")
    public User restGet() {
        return (User) redisTemplate.opsForValue().get(key);
    }
}
