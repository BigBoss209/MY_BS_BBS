package com.example.boot1907.Enum;

import lombok.Getter;

/**
 * @Auther 刘金明
 * @Date 2020/4/2-11:38
 * Description 用户点赞的状态
 */
@Getter
public enum LikedStatusEnum {
    LIKE("1", "点赞"),
    UNLIKE("0", "取消点赞/未点赞"),
    ;

    private String code;

    private String msg;

    LikedStatusEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

