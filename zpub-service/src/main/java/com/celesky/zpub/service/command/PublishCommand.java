package com.celesky.zpub.service.command;

import com.celesky.zpub.cache.PublishLimitCache;
import com.celesky.zpub.mapper.PublishLogMapper;
import com.celesky.zpub.remote.GroupRobotRemote;
import com.celesky.zpub.service.GroupMessageService;
import com.google.common.base.Splitter;
import com.zuzuche.msa.base.util.StringUtil;
import com.celesky.zpub.common.constant.WechatAppEnums;
import com.celesky.zpub.service.publish.AutoPublishService;
import com.celesky.zpub.service.publish.JenkinsService;
import com.celesky.zpub.service.publish.stage.StagingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @desc:
 * @author: panqiong
 * @date: 2019-12-09
 */
@Component("publish")
public class PublishCommand  extends AbstractWxCommand {
    @Autowired
    JenkinsService jenkinsService;


    @Autowired
    GroupRobotRemote groupRobotRemote;


    @Autowired
    PublishLimitCache publishLimitCache;


    @Autowired
    GroupMessageService groupRobotService;

    @Autowired
    AutoPublishService autoPublishService;

    @Autowired
    PublishLogMapper publishLogMapper;


    @Value("${publish.admins}")
    private String publishAdmins;

    @Override
    public void execute(String userName, List<String> paramList) {
        // 上线发布管理员
        if(StringUtil.isEmpty(publishAdmins)){
            messageService.sendWechatMsg(userName,"没有配置发布管理员", WechatAppEnums.assist);
            return;
        }
        List<String> admins = Splitter.on(",").splitToList(publishAdmins);
        if(!admins.contains(userName)){
            messageService.sendWechatMsg(userName,"你没这个权限,再见!", WechatAppEnums.assist);
            return;
        }

        if(paramList.size()<3){
            messageService.sendWechatMsg(userName,"缺少参数,正确格式: publish xxx-service 镜像名称;", WechatAppEnums.assist);
            return;
        }
        // 回送一个确认信息给发布人
        messageService.sendWechatMsg(userName,"收到!马上安排线上发布!", WechatAppEnums.assist);

        String serviceName = paramList.get(1);
        String imageName = paramList.get(2);

        if(paramList.size()<3){
            messageService.sendWechatMsg(userName,"缺少参数,正确格式: publish xxx-service 镜像名称  [fast]", WechatAppEnums.assist);
            return;
        }
        // 拼接后缀
        if(!serviceName.endsWith("-service")){
            serviceName = serviceName+"-service";
        }

        // 是否有快速发布标识
        boolean quikpub = false;
        if(paramList.size()==4){
            String quikFlag = paramList.get(3);
            quikpub = "fast".equals(quikFlag);
        }

        // 是否30秒内重复触发
        boolean limit = publishLimitCache.setIfAbsent(serviceName);
        if(!limit){
            messageService.sendWechatMsg(userName,serviceName+" 发布频率过快,听一首坤坤的<鸡你太美>放松一下!", WechatAppEnums.assist);
            return ;
        }

        boolean ready = StagingManager.isReady();
        if(!ready){
            messageService.sendWechatMsg(userName,"系统正在初始化中,稍后...", WechatAppEnums.assist);
        }

        autoPublishService.createPublishTask(userName,serviceName,imageName,quikpub);

    }
}
