package com.example.boot1907.bo;

import com.example.boot1907.Enum.LikedStatusEnum;
import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @Auther 刘金明
 * @Date 2020/4/2-11:39
 * Description
 */

@Data
public class UserLike {

    //主键id
    private Integer id;

    //被点赞的用户的id
    private String likedUserId;

    //点赞的用户的id
    private String likedPostId;

    //点赞的状态.默认未点赞
    private String status = LikedStatusEnum.UNLIKE.getCode();

    public UserLike() {
    }

    public UserLike(String likedUserId, String likedPostId, String status) {
        this.likedUserId = likedUserId;
        this.likedPostId = likedPostId;
        this.status = status;
    }
}