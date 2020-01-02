package com.example.boot1907.pojo;

import lombok.Data;


@Data
public class Menu {
    private Integer menuId;
    private String menuName;
    private String menuUrl;
    private Integer menuLevel;
    private Integer menuView;
    private Integer pid;
    private String permission;
}