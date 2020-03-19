package com.celesky.zpub.common.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * @desc:
 * @author: panqiong
 * @date: 2018/11/6
 */
@Slf4j
public class PerformUtil {

    public static void logTime(String api,long start,long end){
        long diff = end-start;
        log.info(api+" 接口耗时:"+String.valueOf(diff));
//        if(diff>200){
//            log.info(api+" 接口耗时200+:"+String.valueOf(diff));
//        }
//        if(diff>500){
//            log.info(api+" 接口耗时500+:"+String.valueOf(diff));
//        }
//        if(diff>1000){
//            log.info(api+" 接口耗时1000+:"+String.valueOf(diff));
//        }
//        if(diff>10000){
//            log.info(api+" 接口耗时10000+:"+String.valueOf(diff));
//        }
    }
}
