package com.celesky.zpub.rest;

import com.celesky.zpub.rest.req.GroupRobotMsgReq;
import com.celesky.zpub.service.GroupMessageService;
import com.zuzuche.msa.base.resp.RespResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc: 群聊机器人统一封装入口接口
 * @author: panqiong
 * @date: 2019-11-15
 */
@RestController
@RequestMapping("/groupRobot")
@Slf4j
@Api(value = "企业微信群聊机器人", description = "企业微信群聊机器人", tags = {"企业微信群聊机器人"})
public class GroupRobotRest {

    @Autowired
    GroupMessageService groupRobotService;


    @RequestMapping(value = "/send" , method = RequestMethod.POST)
    @ApiOperation(value = "发送消息", notes = "发送消息")
    public RespResult send(GroupRobotMsgReq req){
        groupRobotService.sendGroupNotifyMsg(req.getContent(),req.getRobotName());
        return RespResult.success();
    }

}
