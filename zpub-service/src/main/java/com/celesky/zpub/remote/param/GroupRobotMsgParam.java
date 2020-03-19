package com.celesky.zpub.remote.param;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @desc: 群聊机器人消息体
 * @author: panqiong
 * @date: 2019-07-30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupRobotMsgParam {

    private String msgtype;

    private Text text;

    private Text markdown;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Text{
        private String content;

        @JSONField(name = "mentioned_list")
        private List<String> mentionedList;

        @JSONField(name = "mentioned_mobile_list")
        private List<String> mentionedMobileList;
    }

}
