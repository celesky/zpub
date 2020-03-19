package com.celesky.zpub.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @desc:
 * @author: panqiong
 * @date: 2019-01-10
 */
@Component
public class WechatCache {
    private static String ROBOT_ACCESS_TOKEN = "sentry:wechat_robot_access_token";

    private static String ASSIST_ACCESS_TOKEN = "sentry:wechat_assist_access_token";

    private static String PRODUCT_ACCESS_TOKEN = "sentry:wechat_product_access_token";

    @Autowired
    StringRedisTemplate redisTemplate;



    public void setRobotToken(String token){
        redisTemplate.opsForValue().set(ROBOT_ACCESS_TOKEN,token,2, TimeUnit.HOURS);
    }

    public String getRobotToken(){
        return redisTemplate.opsForValue().get(ROBOT_ACCESS_TOKEN);
    }


    public void setAssistToken(String token){
        redisTemplate.opsForValue().set(ASSIST_ACCESS_TOKEN,token,2, TimeUnit.HOURS);
    }

    public String getAssistToken(){
        return redisTemplate.opsForValue().get(ASSIST_ACCESS_TOKEN);
    }


    public void setProductToken(String token){
        redisTemplate.opsForValue().set(PRODUCT_ACCESS_TOKEN,token,2, TimeUnit.HOURS);
    }

    public String getProductToken(){
        return redisTemplate.opsForValue().get(PRODUCT_ACCESS_TOKEN);
    }


}
