package com.celesky.zpub.wechatsdk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @desc: 企业微信上行加密消息体
 * @author: panqiong
 * @date: 2019-05-04
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WechatInboundEcry {

    /**
     * 企业微信的CorpID，当为第三方套件回调事件时，CorpID的内容为suiteid
     */
    private String toUserName;

    /**
     * 接收的应用id，可在应用的设置页面获取
     */
    private String agentId;

    /**
     * 消息结构体加密后的字符串
     */
    private String Encrypt;
}
