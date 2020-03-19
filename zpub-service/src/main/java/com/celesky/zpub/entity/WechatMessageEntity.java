package com.celesky.zpub.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @desc: 消息上行解密消息体
 * @author: panqiong
 * @date: 2019-05-04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "wechat_message")
public class WechatMessageEntity {
    @Id
    String id;
    String touserName ;
    String fromUserName ;
    String createTime ;
    String msgType ;
    String content ;
    String msgId ;
    String agentId ;
    LocalDateTime ctime;
    String picUrl ;
}
