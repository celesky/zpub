package com.celesky.zpub.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @desc:
 * @author: panqiong
 * @date: 2018/8/29
 */
@Slf4j
public class DateUtil {
    public static String pattern_1="yyyy-MM-dd HH:mm:ss";

    public static String pattern_2="yyyyMMddHHmmss";

    public static String pattern_3="yyyy/MM/dd HH:mm";

    public static String pattern_4="yyyyMMdd_HHmmss";

    public static String getCurrentTime(String pattern){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }


    public static String formateDateStr(String dateStr){
        try{
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime ldt = LocalDateTime.parse(dateStr,df);
            return ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }catch (Exception e){
            log.error("error:",e,e.getMessage());
        }
        return "";

    }

    public static String timeStampToDateStr(long time,String pattern){
        LocalDateTime startTime = LocalDateTime.ofEpochSecond(time/1000,0, ZoneOffset.ofHours(8)) ;
        return startTime.format(DateTimeFormatter.ofPattern(pattern));
    }


    public static String formateDate(LocalDateTime date,String pattern){
        try{
            return date.format(DateTimeFormatter.ofPattern(pattern));
        }catch (Exception e){
            log.error("error:",e,e.getMessage());
        }
        return "";

    }



}
