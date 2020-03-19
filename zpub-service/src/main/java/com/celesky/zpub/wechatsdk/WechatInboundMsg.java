package com.celesky.zpub.wechatsdk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @desc: 消息上行解密消息体
 * @author: panqiong
 * @date: 2019-05-04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WechatInboundMsg {
    String touserName ;
    String fromUserName ;
    String createTime ;
    String msgType ;
    String content ;
    String msgId ;
    String agentId ;
    String picUrl;
}
