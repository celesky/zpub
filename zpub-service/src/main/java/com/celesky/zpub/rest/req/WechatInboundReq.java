package com.celesky.zpub.rest.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @desc:
 * @author: panqiong
 * @date: 2019-05-04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WechatInboundReq {

    /**
     * 企业微信加密签名，msg_signature结合了企业填写的token、请求中的timestamp、nonce参数、加密的消息体
     */
    private String msg_signature;

    /**
     * 时间戳
     */
    private String timestamp;

    /**
     * 随机数
     */
    private String nonce;



}
