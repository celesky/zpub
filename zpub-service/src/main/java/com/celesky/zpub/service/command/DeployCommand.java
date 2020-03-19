package com.celesky.zpub.service.command;

import com.celesky.zpub.cache.DeployLimitCache;
import com.celesky.zpub.remote.GroupRobotRemote;
import com.celesky.zpub.service.GroupMessageService;
import com.celesky.zpub.service.publish.JenkinsService;
import com.celesky.zpub.common.constant.WechatAppEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @desc: 发布命令
 * @author: panqiong
 * @date: 2019-09-24
 */
@Component("deploy")
public class DeployCommand extends AbstractWxCommand {

    @Autowired
    JenkinsService jenkinsService;


    @Autowired
    GroupRobotRemote groupRobotRemote;


    @Autowired
    DeployLimitCache deployLimitCache;


    @Autowired
    GroupMessageService groupRobotService;


    @Override
    public void execute(String userName, List<String> paramList) {
        if(paramList.size()<3){
            messageService.sendWechatMsg(userName,"缺少参数,正确格式: 发布 sms master", WechatAppEnums.assist);
            return;
        }
        // 回送一个确认信息给发布人
        messageService.sendWechatMsg(userName,"收到!马上安排发布!", WechatAppEnums.assist);

        String serviceName = paramList.get(1);
        String branch = paramList.get(2);

        // 拼接后缀
        if(!serviceName.endsWith("-service")){
            serviceName = serviceName+"-service";
        }

        // 是否30秒内重复触发
        boolean limit = deployLimitCache.setIfAbsent(serviceName);
        if(!limit){
            messageService.sendWechatMsg(userName,serviceName+" 发布频率过快,听一首坤坤的<鸡你太美>放松一下!", WechatAppEnums.assist);
            return ;
        }

        String notifyContent = assembleDeployNotifyMsg(userName,serviceName,branch);
        this.sendGroupNotifyMsg(notifyContent);
        // 触发部署
        jenkinsService.triggerDeploy(userName,serviceName,branch);
    }

    /**
     * 发送一条群聊通知
     */
    public void sendGroupNotifyMsg(String content){
//        GroupRobotMsgParam.Text text = GroupRobotMsgParam.Text.builder().content(content).build();
//        GroupRobotMsgParam param = GroupRobotMsgParam.builder()
//                .msgtype("markdown")
//                .markdown(text)
//                .build();
        groupRobotService.sendGroupNotifyMsg(content,"TestEnvDeployRobot");
    }

    private String assembleDeployNotifyMsg(String userName,String serviceName,String branch){
        String content = "测试环境<font color=\"red\">{serviceName}</font>服务发布,请相关同事注意\n"+
                ">服务:<font color=\"info\">{serviceName}</font> \n"+
                ">分支:<font color=\"info\">{branch}</font> \n"+
                ">发布人:<font color=\"comment\">{userName}</font>";

        content = content.replace("{serviceName}",serviceName)
                .replace("{userName}",userName)
                .replace("{branch}",branch);
        return content;
    }
}
