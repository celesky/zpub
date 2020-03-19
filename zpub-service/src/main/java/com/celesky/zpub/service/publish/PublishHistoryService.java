package com.celesky.zpub.service.publish;

import com.celesky.zpub.entity.PublishLog;
import com.celesky.zpub.mapper.PublishLogMapper;
import com.celesky.zpub.service.WechatMessageService;
import com.celesky.zpub.common.constant.WechatAppEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @desc: 发布历史
 * @author: panqiong
 * @date: 2019-12-30
 */
@Component
public class PublishHistoryService {

    @Autowired
    PublishLogMapper logMapper;


    @Autowired
    WechatMessageService messageService;


    /**
     * 发布历史记录
     * @param userName
     * @param serviceName
     */
    public void queryHistoryList(String userName,String serviceName){
        // 拼接后缀
        if(!serviceName.endsWith("-service")){
            serviceName = serviceName+"-service";
        }

        List<PublishLog> logList = logMapper.queryByServiceName(serviceName);
        if(logList==null||logList.size()==0){
            messageService.sendWechatMsg(userName,"没有该服务的发布历史记录", WechatAppEnums.assist);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(serviceName).append("发布日志如下:\n");
        for(int i=0;i<logList.size();i++){
            PublishLog log = logList.get(i);
            sb.append(i+1)
                    .append(": ")
                    .append(log.getImageName())
                    .append(" ")
                    .append(log.getPublishResult()==null?"结果未知":log.getPublishResult())
                    .append(" ")
                    .append(log.getCreateTime())
                    .append(" ")
                    .append(log.getCreateTime().getDayOfWeek())
                    .append("\n")
            ;
        }

        messageService.sendWechatMsg(userName,sb.toString(), WechatAppEnums.assist);

    }
}
