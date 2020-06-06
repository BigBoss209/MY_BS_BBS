package com.example.boot1907.Service.impl;

/**
 * @Auther 刘金明
 * @Date 2020/4/2-11:44
 * Description
 */
import com.example.boot1907.Enum.LikedStatusEnum;
import com.example.boot1907.Service.IArticleService;
import com.example.boot1907.Service.IUserService;
import com.example.boot1907.Service.LikedService;
import com.example.boot1907.Service.RedisService;
import com.example.boot1907.bo.UserLike;
import com.example.boot1907.dto.LikedCountDTO;
import com.example.boot1907.pojo.Article;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class LikedServiceImpl implements LikedService {

//    @Autowired
//    UserLikeRepository likeRepository;

    @Autowired
    RedisService redisService;

    @Autowired
    IArticleService articleService;

    @Autowired
    IUserService userService;

    @Override
    @Transactional
    public UserLike save(UserLike userLike) {
//        return likeRepository.save(userLike);
        return null;
    }

    @Override
    @Transactional
    public List<UserLike> saveAll(List<UserLike> list) {
//        return likeRepository.saveAll(list);
        return null;
    }

    @Override
    public Page<UserLike> getLikedListByLikedUserId(String likedUserId, Pageable pageable) {
//        return likeRepository.findByLikedUserIdAndStatus(likedUserId, LikedStatusEnum.LIKE.getCode(), pageable);
        return null;
    }

    @Override
    public Page<UserLike> getLikedListByLikedPostId(String likedPostId, Pageable pageable) {
//        return likeRepository.findByLikedPostIdAndStatus(likedPostId, LikedStatusEnum.LIKE.getCode(), pageable);
    return null;
    }

    @Override
    public UserLike getByLikedUserIdAndLikedPostId(String likedUserId, String likedPostId) {
//        return likeRepository.findByLikedUserIdAndLikedPostId(likedUserId, likedPostId);
    return null;
    }

    @Override
    @Transactional
    public void transLikedFromRedis2DB() {
        List<UserLike> list = redisService.getLikedDataFromRedis();
        for (UserLike like : list) {
            userService.updateCon(like.getLikedPostId());
//            UserLike ul = getByLikedUserIdAndLikedPostId(like.getLikedUserId(), like.getLikedPostId());
//            if (ul == null){
//                //没有记录，直接存入
//                save(like);
//            }else{
//                //有记录，需要更新
//                ul.setStatus(like.getStatus());
//                save(ul);
//            }
        }
    }

    @Override
    @Transactional
    public void transLikedCountFromRedis2DB() {
        List<LikedCountDTO> list = redisService.getLikedCountFromRedis();
        for (LikedCountDTO dto : list) {
            Article art =  articleService.searchByArtId(Integer.parseInt(dto.getId()));
            //点赞数量属于无关紧要的操作，出错无需抛异常
            if (art != null){
                Long likeNum = art.getArtLikeNum() + dto.getCount();
                art.setArtLikeNum(likeNum);
                //更新点赞数量
                articleService.updatelikeInfo(art);
            }
        }
    }
}
