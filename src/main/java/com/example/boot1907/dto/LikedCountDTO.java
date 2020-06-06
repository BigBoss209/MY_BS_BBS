package com.example.boot1907.dto;

import lombok.Data;

/**
 * @Auther 刘金明
 * @Date 2020/4/2-11:57
 * Description
 */
@Data
public class LikedCountDTO {
    private String id;
    private Integer count;

    public LikedCountDTO(String id,Integer count){
        this.id = id;
        this.count = count;
    }
}
