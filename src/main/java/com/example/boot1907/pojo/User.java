package com.example.boot1907.pojo;

import java.util.Date;

import com.alibaba.druid.sql.visitor.functions.Char;
import lombok.Data;

@Data
public class User {
    private Long userId;

    private String userName;

    private String userPassword;

    private String salt;

    private String userEmail;

    private String userGender;

    private Long userPhone;

    private String isadmin;

    private String userStatus;

    private Integer userEx;

    private Date userTime;

    private String userShow;

    private String userBlog;

    private String userImg;

    private Long userFans;

    private Long userConcern;

    private String valid;
}