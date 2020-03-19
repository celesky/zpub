package com.celesky.zpub.service;

import com.celesky.zpub.remote.OpsRemote;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @desc: 负责服务的下线\或者
 * @author: panqiong
 * @date: 2020-01-06
 */
@Slf4j
@Service
public class OpsService {

    @Autowired
    OpsRemote opsRemote;

    public String unregister(String url){
        try{
            String result = opsRemote.httpGetJson(url);
            log.info("[反注册下线流量]{}::::{}",url,result);
            return result;
        }catch (Exception e){
            log.error("反注册出错:",e.getMessage(),e);
            return e.getMessage();
        }
    }


    public boolean autoTest(String url){
        try{
            String result = opsRemote.httpGetJson(url);
            log.info("[自动测试完成]{}::::{}",url,result);
            return true;
        }catch (Exception e){
            log.error("自动测试出错:",e.getMessage(),e);
            return false;
        }
    }




}
