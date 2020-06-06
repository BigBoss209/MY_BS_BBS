package com.example.boot1907.Service.impl;

/**
 * @Auther 刘金明
 * @Date 2020/4/2-11:34
 * Description
 */
import com.example.boot1907.Enum.LikedStatusEnum;
import com.example.boot1907.Service.IArticleService;
import com.example.boot1907.Service.LikedService;
import com.example.boot1907.Service.RedisService;
import com.example.boot1907.Utils.RedisKeyUtils;
import com.example.boot1907.bo.UserLike;
import com.example.boot1907.dto.LikedCountDTO;
import com.example.boot1907.pojo.Article;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class RedisServiceImpl implements RedisService {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    LikedService likedService;

    @Autowired
    IArticleService iArticleService;

    @Override
    public void saveLiked2Redis(String likedUserId, String likedPostId) {
        String key = RedisKeyUtils.getLikedKey(likedUserId, likedPostId);
        redisTemplate.opsForHash().put(RedisKeyUtils.MAP_KEY_USER_LIKED, key, LikedStatusEnum.LIKE.getCode());

    }

    @Override
    public void unlikeFromRedis(String likedUserId, String likedPostId) {
        String key = RedisKeyUtils.getLikedKey(likedUserId, likedPostId);
        redisTemplate.opsForHash().put(RedisKeyUtils.MAP_KEY_USER_LIKED, key, LikedStatusEnum.UNLIKE.getCode());
    }

    @Override
    public void deleteLikedFromRedis(String likedUserId, String likedPostId) {
        String key = RedisKeyUtils.getLikedKey(likedUserId, likedPostId);
        redisTemplate.opsForHash().delete(RedisKeyUtils.MAP_KEY_USER_LIKED, key);
    }

    @Override
    public Boolean IsInRedis(String likedUserId, String likedPostId) {
        String key = RedisKeyUtils.getLikedKey(likedUserId, likedPostId);
        return redisTemplate.opsForHash().hasKey(RedisKeyUtils.MAP_KEY_USER_LIKED, key);
    }

    @Override
    public void incrementLikedCount(String likedUserId) {
        redisTemplate.opsForHash().increment(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, likedUserId, 1);
    }

    @Override
    public void decrementLikedCount(String likedUserId) {
        redisTemplate.opsForHash().increment(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, likedUserId, -1);
    }

    @Override
    public String getLikedCount(String likedUserId) {
        //初始化
        if(redisTemplate.opsForHash().get(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, likedUserId) == null){
            Article article = iArticleService.searchByArtId(Integer.parseInt(likedUserId));
            String value = article.getArtLikeNum().toString();
            redisTemplate.opsForHash().put(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, likedUserId,value);
        }

        return (String) redisTemplate.opsForHash().get(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, likedUserId);
    }

    @Override
    public List<UserLike> getLikedDataFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtils.MAP_KEY_USER_LIKED, ScanOptions.NONE);
        List<UserLike> list = new ArrayList<>();
        while (cursor.hasNext()){
            Map.Entry<Object, Object> entry = cursor.next();
            String key = (String) entry.getKey();
            //分离出 likedUserId，likedPostId
            String[] split = key.split("::");
            String likedUserId = split[0];
            String likedPostId = split[1];
            String value = (String) entry.getValue();

            //组装成 UserLike 对象
            UserLike userLike = new UserLike(likedUserId, likedPostId, value);
            list.add(userLike);

            //存到 list 后从 Redis 中删除
            redisTemplate.opsForHash().delete(RedisKeyUtils.MAP_KEY_USER_LIKED, key);
        }

        return list;
    }

    @Override
    public List<LikedCountDTO> getLikedCountFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, ScanOptions.NONE);
        List<LikedCountDTO> list = new ArrayList<>();
        while (cursor.hasNext()){
            Map.Entry<Object, Object> map = cursor.next();
            //将点赞数量存储在 LikedCountDT
            String key = (String)map.getKey();
            LikedCountDTO dto = new LikedCountDTO(key, Integer.parseInt((String) map.getValue()));
            list.add(dto);
            //从Redis中删除这条记录
            redisTemplate.opsForHash().delete(RedisKeyUtils.MAP_KEY_USER_LIKED_COUNT, key);
        }
        return list;
    }
}



