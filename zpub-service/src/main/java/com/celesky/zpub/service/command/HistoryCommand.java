package com.celesky.zpub.service.command;

import com.celesky.zpub.service.publish.PublishHistoryService;
import com.celesky.zpub.common.constant.WechatAppEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @desc: 查询服务的发布历史
 * @author: panqiong
 * @date: 2019-12-30
 */
@Component("history")
public class HistoryCommand extends AbstractWxCommand{
    @Autowired
    PublishHistoryService publishHistoryService;

    @Override
    public void execute(String userName, List<String> paramList) {
        if(paramList.size()<2){
            messageService.sendWechatMsg(userName,"缺少参数,正确格式: 发布 history sms", WechatAppEnums.assist);
            return;
        }
        String serviceName = paramList.get(1);
        // 拼接后缀
        if(!serviceName.endsWith("-service")){
            serviceName = serviceName+"-service";
        }

        publishHistoryService.queryHistoryList(userName,serviceName);

    }
}
