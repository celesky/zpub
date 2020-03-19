package com.celesky.zpub.remote;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * @desc:
 * @author: panqiong
 * @date: 2018/11/19
 */
@Component
public class OpsRemote extends AbstractHttpInvoke{
    /**
     * 额外的header设置 比如编码
     *
     * @param header
     */
    @Override
    protected void setHeader(HttpHeaders header) {

    }

    public  String httpGetJson(String url){
        String result = super.getJson(url);
        return result;
    }







}
