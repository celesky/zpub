package com.celesky.zpub.service.command;

import com.celesky.zpub.cache.SilentCache;
import com.celesky.zpub.remote.SentryRobotRemote;
import com.celesky.zpub.service.GroupMessageService;
import com.celesky.zpub.service.WechatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @desc: 取消静默模式
 * @author: panqiong
 * @date: 2019-09-24
 */
@Component("rmsilent")
public class RmsilentCommand extends AbstractWxCommand{
    @Autowired
    WechatMessageService messageService;



    @Autowired
    SilentCache silentCache;

    @Autowired
    SentryRobotRemote sentryRobotRemote;

    @Autowired
    GroupMessageService groupRobotService;

    @Override
    public void execute(String userName, List<String> paramList) {
//        if(paramList.size()<2){
//            messageService.sendWechatMsg(userName,"缺少参数,正确格式: rmsilent 19", WechatAppEnums.assist);
//            return;
//        }
//        String strategyId = paramList.get(1);
//
//        String strategyName  = strategyService.getStrategyName(strategyId);
//
//        if(StringUtil.isEmpty(strategyName)){
//            messageService.sendWechatMsg(userName,"找不到该策略id,请检查", WechatAppEnums.assist);
//            return;
//        }
//
//        silentCache.removeSilent(strategyId);
//
//        messageService.sendWechatMsg(userName,"安排上了!", WechatAppEnums.assist);
//
//        String content = "取消静默模式通知: %s 已经将预警策略:%s(id:%s) 取消静默模式,该策略将继续实时告警!";
//        content = String.format(content,userName,strategyName,strategyId);
////        GroupRobotMsgParam.Text text = GroupRobotMsgParam.Text.builder().content(content).build();
////        GroupRobotMsgParam param = GroupRobotMsgParam.builder()
////                .msgtype("markdown")
////                .markdown(text)
////                .build();
////        sentryRobotRemote.sentGroupMsg(param);
//        groupRobotService.sendGroupNotifyMsg(content,"sentryRobot");
    }

}
