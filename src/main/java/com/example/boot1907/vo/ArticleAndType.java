package com.example.boot1907.vo;

import com.example.boot1907.pojo.Article;
import lombok.Data;

/**
 * @Auther 刘金明
 * @Date 2020/3/29-15:06
 * Description
 */
@Data
public class ArticleAndType {
    private String TypeName;
    private Article article;
    private Long TypeId;
}
