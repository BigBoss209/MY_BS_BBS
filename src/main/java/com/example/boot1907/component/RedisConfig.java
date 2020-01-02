package com.example.boot1907.component;

import org.apache.shiro.session.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import java.io.Serializable;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<Serializable, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory)  {
        RedisTemplate<Serializable, Object> redisTemplate = new RedisTemplate();
        redisTemplate.setKeySerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
}
