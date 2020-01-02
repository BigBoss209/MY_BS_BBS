package com.example.boot1907.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class User implements Serializable {
    private Integer userId;
    private String userName;
    private Date brithday;
    private String gender;
    private String isadmin;
    private String filepath;
    private String passwd;
    private Integer valid;
    private String salt;
}