package com.celesky.zpub.remote;

import com.celesky.zpub.remote.param.GroupRobotMsgParam;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * @desc: 客服预警群组机器人
 * @author: panqiong
 * @date: 2019-09-19
 */
@Component
public class SentryRobotRemote extends AbstractHttpInvoke{

    /**
     * 额外的header设置 比如编码
     *
     * @param header
     */
    @Override
    protected void setHeader(HttpHeaders header) {

    }

    /**
     * 测试环境发布群
     * @param groupRobotMsgParam
     */
    public void sentGroupMsg(GroupRobotMsgParam groupRobotMsgParam){
        // 虚鲲预警
        String url = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=0594895c-1fa1-4a9d-86c1-d62cb1b07ff7";
        // 预警机器人
        //String url = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=20508b96-a98d-453c-a6e4-401221a67264";

        //String url = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=24d13644-be1e-401b-b380-f7fd24ef9383";


        super.postJson(url,groupRobotMsgParam);
    }



}
