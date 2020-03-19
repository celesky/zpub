package com.celesky.zpub.remote;

import com.celesky.zpub.remote.param.GroupRobotMsgParam;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * @desc: 微信群聊机器人
 * @author: panqiong
 * @date: 2019-07-30
 */
@Component("groupRobotRemote")
public class GroupRobotRemote extends AbstractHttpInvoke{



    /**
     * 额外的header设置 比如编码
     *
     * @param header
     */
    @Override
    protected void setHeader(HttpHeaders header) {

    }


    public void send(GroupRobotMsgParam groupRobotMsgParam, String url){
        super.postJson(url,groupRobotMsgParam);
    }



}
