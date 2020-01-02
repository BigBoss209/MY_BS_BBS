package com.example.boot1907.assist;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class SessionDaoImpl extends EnterpriseCacheSessionDAO {
    private RedisTemplate<Serializable, Object> redisTemplate;
    int sessiontimeout=30;

    @Override
    protected Serializable doCreate(Session session) {
        // 生成sessionId
        Serializable sessionId = generateSessionId(session);
        // 给session赋ID值
        assignSessionId(session, sessionId);
        // 保存到redis
        redisTemplate.opsForValue().set(session.getId(), session, sessiontimeout, TimeUnit.MINUTES);
        return session.getId();
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        Session session = (Session)redisTemplate.opsForValue().get(sessionId);
        return session;
    }

    @Override
    protected void doUpdate(Session session) {
        // redis的30分钟会过期，如果用户一直在交互，要更新缓存的时间
        redisTemplate.opsForValue().set(session.getId(), session, sessiontimeout, TimeUnit.MINUTES);
    }

    @Override
    protected void doDelete(Session session) {
        redisTemplate.delete(session.getId());
    }


    public void setRedisTemplate(RedisTemplate<Serializable, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
