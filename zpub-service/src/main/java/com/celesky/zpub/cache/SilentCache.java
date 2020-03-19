package com.celesky.zpub.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @desc: 静默模式设置
 * @author: panqiong
 * @date: 2019-09-20
 */
@Component
public class SilentCache {

    private static String SILENT_STRATEGY_KEY = "sentry:silent_strategy:%s";

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * 对该策略设置静默时间
     * 单位分钟
     * @param strategyId
     * @param minute
     */
    public void setSilent(String strategyId,long minute){
        String key = String.format(SILENT_STRATEGY_KEY,strategyId);
        redisTemplate.opsForValue().set(key,"0",minute, TimeUnit.MINUTES);
    }


    /**
     * 查询key是否存在,即是否仍在静默期间
     * 单位分钟
     * @param strategyId
     */
    public void removeSilent(String strategyId){
        String key = String.format(SILENT_STRATEGY_KEY,strategyId);
        redisTemplate.delete(key);
    }


    /**
     * 查询key是否存在,即是否仍在静默期间
     * 单位分钟
     * @param strategyId
     */
    public boolean hasSilentkey(String strategyId){
        String key = String.format(SILENT_STRATEGY_KEY,strategyId);
        return redisTemplate.hasKey(key);
    }



}
