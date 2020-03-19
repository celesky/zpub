package com.celesky.zpub.service.command;

import com.celesky.zpub.cache.SilentCache;
import com.celesky.zpub.service.GroupMessageService;
import com.celesky.zpub.service.WechatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @desc: 打开静默模式
 * @author: panqiong
 * @date: 2019-09-24
 */
@Component("silent")
public class SilentCommand extends AbstractWxCommand {

    @Autowired
    WechatMessageService messageService;



    @Autowired
    SilentCache silentCache;

    @Autowired
    GroupMessageService groupRobotService;

    @Override
    public void execute(String userName, List<String> paramList) {

//        if(paramList.size()<3){
//            messageService.sendWechatMsg(userName,"缺少参数,正确格式: silent [strategyId] [minute]", WechatAppEnums.assist);
//            return;
//        }
//
//
//        String strategyId = paramList.get(1);
//        String minute = paramList.get(2);
//        Long min = Long.parseLong(minute);
//
//        String strategyName  = strategyService.getStrategyName(strategyId);
//
//        if(StringUtil.isEmpty(strategyName)){
//            messageService.sendWechatMsg(userName,"找不到该策略id,请检查", WechatAppEnums.assist);
//            return;
//        }
//
//        silentCache.setSilent(strategyId,min);
//        messageService.sendWechatMsg(userName,"安排上了!", WechatAppEnums.assist);
//
//        String content = "开启静默模式通知: %s 已经将预警策略:%s(id:%s) 设置静默 %s 分钟,该策略在%s分钟内将不会再下发预警通知!";
//        content = String.format(content,userName,strategyName,strategyId,min,min);
////        GroupRobotMsgParam.Text text = GroupRobotMsgParam.Text.builder().content(content).build();
////        GroupRobotMsgParam param = GroupRobotMsgParam.builder()
////                .msgtype("markdown")
////                .markdown(text)
////                .build();
////        sentryRobotRemote.sentGroupMsg(param);
//        groupRobotService.sendGroupNotifyMsg(content,"sentryRobot");

    }
}
