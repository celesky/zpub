package com.celesky.zpub.remote;

import com.celesky.zpub.cache.WechatCache;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @desc:
 * @author: panqiong
 * @date: 2019-01-31
 */
@Component("robot")
public class AssistRobotRemote extends WechatRemote implements InitializingBean {
    @Value("${wechat.robotAppId}")
    private String robotAppKey;

    @Value("${wechat.robotAppSecret}")
    private String robotSecret;


    @Autowired
    WechatCache wechatCache;



    @Override
    protected String getCachedToken() {
        return wechatCache.getRobotToken();
    }

    @Override
    protected void setCachedToken(String token) {
        wechatCache.setRobotToken(token);
    }



    @Override
    public void afterPropertiesSet() throws Exception {
        super.setConfig(robotAppKey,robotSecret);
    }
}
