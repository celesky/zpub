package com.celesky.zpub.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @desc:
 * @author: panqiong
 * @date: 2019-05-04
 */
@Component
public class PublishLimitCache {
    private static final String PUBLISH_LIMIT_KEY = "sentry:publish_limit_key_%s";

    @Autowired
    RedisTemplate redisTemplate;


    public boolean setIfAbsent(String serviceName){
        String key = String.format(PUBLISH_LIMIT_KEY,serviceName);
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key,1);
        if(result){
            // 30秒
            redisTemplate.expire(key,30,TimeUnit.SECONDS);
        }
        return result;
    }

    public void remove(String serviceName){
        String key = String.format(PUBLISH_LIMIT_KEY,serviceName);
        redisTemplate.delete(key);
    }
}
