package com.celesky.zpub.rest.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @desc:
 * @author: panqiong
 * @date: 2019-11-15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupRobotMsgReq {

    private String robotName;

    private String content;

}
