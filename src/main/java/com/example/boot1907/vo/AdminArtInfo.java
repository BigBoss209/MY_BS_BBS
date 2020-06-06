package com.example.boot1907.vo;

import com.example.boot1907.pojo.User;
import lombok.Data;

import java.util.List;

/**
 * @Auther 刘金明
 * @Date 2020/3/26-9:42
 * Description
 */
@Data
public class AdminArtInfo {
    private int code;
    private String msg;
    private List<AdminArt> data;
    private Long count;
}
