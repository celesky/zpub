package com.celesky.zpub.service;

import com.celesky.zpub.config.RobotListConfig;
import com.celesky.zpub.remote.GroupRobotRemote;
import com.zuzuche.msa.base.resp.Status;
import com.zuzuche.msa.base.resp.StatusServiceException;
import com.zuzuche.msa.base.util.StringUtil;
import com.celesky.zpub.common.utils.DateUtil;
import com.celesky.zpub.remote.param.GroupRobotMsgParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @desc: 企业微信群消息
 * @author: panqiong
 * @date: 2019-11-15
 */
@Service
@Slf4j
public class GroupMessageService {

    @Autowired
    RobotListConfig config;

    @Autowired
    GroupRobotRemote groupRobotRemote;


    /**
     * 通过群聊机器人发送消息
     * @param content 内容
     * @param robotName 机器人名称配置
     */
    public void sendGroupNotifyMsg(String content,String robotName){
        String url = config.getRobotUrl(robotName);
        if(StringUtil.isEmpty(url)){
            throw new StatusServiceException(Status.OPERATION_NOT_SUPPORT,"找不到对应的robotName");
        }

        content = content+"\n"+"[当前时间] "+ DateUtil.formateDate(LocalDateTime.now(),DateUtil.pattern_1);

        GroupRobotMsgParam.Text text = GroupRobotMsgParam.Text.builder().content(content).build();
        GroupRobotMsgParam param = GroupRobotMsgParam.builder()
                .msgtype("markdown")
                .markdown(text)
                .build();
        groupRobotRemote.send(param,url);
    }

    /**
     * 通过群聊机器人发送消息
     * @param content 内容
     * @param robotName 机器人名称配置
     */
    public void sendGroupNotifyMsg(String content,String robotName,String mentionName){
        String url = config.getRobotUrl(robotName);
        if(StringUtil.isEmpty(url)){
            throw new StatusServiceException(Status.OPERATION_NOT_SUPPORT,"找不到对应的robotName");
        }
        content = content+"\n"+"[当前时间] "+ DateUtil.formateDate(LocalDateTime.now(),DateUtil.pattern_1);

        GroupRobotMsgParam.Text text = GroupRobotMsgParam.Text.builder()
                .content(content)
                .mentionedList(Arrays.asList(mentionName))
                .build();
        GroupRobotMsgParam param = GroupRobotMsgParam.builder()
                .msgtype("text")
                .text(text)
                .build();
        groupRobotRemote.send(param,url);
    }
}
