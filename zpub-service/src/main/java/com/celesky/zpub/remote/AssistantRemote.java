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
@Component("assist")
public class AssistantRemote extends WechatRemote implements InitializingBean {

    @Value("${wechat.assistAppId}")
    private String assistAppKey;

    @Value("${wechat.assistAppSecret}")
    private String assistSecret;

    @Autowired
    WechatCache wechatCache;


    @Override
    protected String getCachedToken() {
        return wechatCache.getAssistToken();
    }

    @Override
    protected void setCachedToken(String token) {
        wechatCache.setAssistToken(token);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        super.setConfig(assistAppKey,assistSecret);
    }
}
