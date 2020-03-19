package com.celesky.zpub.service.command;

import com.celesky.zpub.remote.SentryRobotRemote;
import com.celesky.zpub.service.WechatMessageService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @desc: 命令
 * @author: panqiong
 * @date: 2019-09-24
 */
public abstract class AbstractWxCommand {
    @Autowired
    WechatMessageService messageService;


    @Autowired
    SentryRobotRemote sentryRobotRemote;


    public abstract void execute(String userName, List<String> paramList);
}
