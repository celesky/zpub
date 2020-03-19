package com.celesky.zpub.common.utils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @desc:
 * @author: panqiong
 * @date: 2018/8/16
 */
public class IdGenerator {
    private static AtomicLong atomicLong = new AtomicLong(0);

//    // 本地获取id 并发量不大,id唯一性要求不严格的场景使用
//    public static String getNextLocalId(){
//        return String.valueOf(atomicLong.addAndGet(1));
//    }
}
