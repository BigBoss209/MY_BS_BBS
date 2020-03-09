package com.example.boot1907.bo;

import com.example.boot1907.pojo.ArticleType;
import lombok.Data;

import java.util.Date;

/**
 * @Auther 刘金明
 * @Date 2020/3/9-11:17
 * Description
 */
@Data
public class ArticleInfo {
    private ArticleType articleType;
    private int total;
    private Date lastReply;
}
