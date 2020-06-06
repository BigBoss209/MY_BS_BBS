package com.example.boot1907.vo;

import com.example.boot1907.pojo.User;
import lombok.Data;
import org.hibernate.validator.constraints.EAN;

import java.util.List;

/**
 * @Auther 刘金明
 * @Date 2020/3/23-9:22
 * Description
 */
@Data
public class AdminUserInfo {
    private int code;
    private String msg;
    private List<User> data;
    private Long count;
}
