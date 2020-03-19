package com.celesky.zpub.service;

import com.celesky.zpub.cache.WechatCache;
import com.celesky.zpub.remote.AssistRobotRemote;
import com.celesky.zpub.remote.AssistantRemote;
import com.celesky.zpub.remote.WechatRemote;
import com.celesky.zpub.remote.dto.WechatMessageResult;
import com.celesky.zpub.common.constant.WechatAppEnums;
import com.celesky.zpub.common.utils.SpringBeanFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

/**
 * @desc: 企业微信小应用
 * @author: panqiong
 * @date: 2019-01-10
 */
@Service
@Slf4j
public class WechatMessageService {

    @Autowired
    WechatCache wechatCache;

    @Autowired
    AssistRobotRemote robotRemote;

    @Autowired
    AssistantRemote assistantRemote;

    /**
     * 刷新accesstoken
     */
    public void refreshToken(){
        Map<String, WechatRemote> map = SpringBeanFactory.getBeansOfType(WechatRemote.class);
        if(map!=null){
            map.forEach((beanname,wechatRemote)->{
                try{
                    wechatRemote.refreshToken();
                }catch (Exception e){
                    log.error("[刷新微信apptoken异常]",e.getMessage(),e);
                }
            });
        }
    }

    /**
     * 发送企业微信消息
     * @param username
     * @param msg
     */
    public void sendWechatMsg(String username, String msg, WechatAppEnums appType){
        WechatRemote wechatRemote = (WechatRemote)SpringBeanFactory.getBean(appType.name());
        WechatMessageResult result = wechatRemote.sendMessageByName(Arrays.asList(username),msg);
        log.info("result:"+result.toString());
        if(result.getErrcode()!=0){
            switch (result.getErrcode()){
                case 41001:
                    refreshToken();
                case 42001:
                    // token过期 刷新一下
                    refreshToken();
                    wechatRemote.sendMessageByName(Arrays.asList(username),msg);
            }
        }
    }


}
