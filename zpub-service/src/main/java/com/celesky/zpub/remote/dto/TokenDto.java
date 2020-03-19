package com.celesky.zpub.remote.dto;

import com.alibaba.fastjson.annotation.JSONField;
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
public class TokenDto {

    /**
     * errcode : 0
     * errmsg : ok
     * access_token : accesstoken000001
     * expires_in : 7200
     *
     *
     */


    private int errcode;
    private String errmsg;
    private String access_token;

    @JSONField(name = "expires_in")
    private int expiresIn;


}
