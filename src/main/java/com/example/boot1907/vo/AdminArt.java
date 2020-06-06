package com.example.boot1907.vo;

import lombok.Data;

/**
 * @Auther 刘金明
 * @Date 2020/3/26-9:44
 * Description
 */
@Data
public class AdminArt {
    private Long artId;

    private String artUserName;

    private String artTitle;

    private String url;

    private Long artTypeId;

    private Long artComNum;

    private String artContent;

    private Integer valid;
}
