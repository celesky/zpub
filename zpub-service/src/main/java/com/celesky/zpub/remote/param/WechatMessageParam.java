package com.celesky.zpub.remote.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @desc:
 * @author: panqiong
 * @date: 2019-01-10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WechatMessageParam {

    /**
     * touser : panqiong
     * toparty :
     * totag :
     * msgtype : text
     * agentid : 1000031
     * text : {"content":"你的快递已到，请携带工卡前往邮件中心领取。\n出发前可查看<a href=\"http://work.weixin.qq.com\">邮件中心视频实况<\/a>，聪明避开排队。"}
     * safe : 0
     */

    private String touser;
    private String toparty;
    private String totag;
    private String msgtype;
    private int agentid;
    private TextBean text;
    private int safe;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TextBean {
        private String content;
    }



}
