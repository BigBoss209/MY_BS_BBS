package com.example.boot1907.component;

import com.example.boot1907.Service.LikedService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther 刘金明
 * @Date 2020/4/6-16:17
 * Description 将redis数据同步到oracle数据库
 */
@Slf4j
public class LikeTask extends QuartzJobBean {
    @Autowired
    LikedService likedService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        log.info("LikeTask-------- {}", sdf.format(new Date()));

        //将 Redis 里的点赞信息同步到数据库里
        likedService.transLikedFromRedis2DB();
        likedService.transLikedCountFromRedis2DB();
    }

}
