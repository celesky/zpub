package com.celesky.zpub.service.command;

import com.celesky.zpub.cache.DeployLimitCache;
import com.celesky.zpub.cache.PublishLimitCache;
import com.celesky.zpub.cache.SilentCache;
import com.celesky.zpub.common.constant.WechatAppEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @desc: 取消发布限制命令
 * @author: panqiong
 * @date: 2019-09-24
 */
@Component("rmlimit")
public class RmlimitCommand extends AbstractWxCommand {


    @Autowired
    SilentCache silentCache;



    @Autowired
    DeployLimitCache deployLimitCache;


    @Autowired
    PublishLimitCache publishLimitCache;

    @Override
    public void execute(String userName, List<String> paramList) {

        if(paramList.size()<2){
            messageService.sendWechatMsg(userName,"缺少参数,正确格式: rmlimit sms-service", WechatAppEnums.assist);
            return;
        }
        String serviceName = paramList.get(1);
        deployLimitCache.remove(serviceName);

        publishLimitCache.remove(serviceName);
        messageService.sendWechatMsg(userName,"已临时去除"+serviceName+"30秒发布限制", WechatAppEnums.assist);

    }
}
