package com.celesky.zpub.remote.dto;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WechatMessageResult {

    /**
     * errcode : 0
     * errmsg : ok
     * invaliduser : userid1|userid2
     * invalidparty : partyid1|partyid2
     * invalidtag : tagid1|tagid2
     */

    private int errcode;
    private String errmsg;
    private String invaliduser;
    private String invalidparty;
    private String invalidtag;


}
