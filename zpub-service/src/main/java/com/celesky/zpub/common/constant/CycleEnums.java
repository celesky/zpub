package com.celesky.zpub.common.constant;


import com.zuzuche.msa.base.constants.BaseEnum;

/**
 * @desc: 周期对比类型  比如上一周同一时间  上一个月同一时间
 * @author: panqiong
 * @date: 2018/8/16
 */
public enum  CycleEnums implements BaseEnum<String> {
    /**
     * 星期
     */
    WEEK("week"),
    /**
     * 月
     */
    MONTH("month");

    public String code;


    CycleEnums(String code){
        this.code = code;
    }

    @Override
    public String code() {
        return null;
    }

    public static CycleEnums parse(String code) {
        try {
            return BaseEnum.parse(CycleEnums.class, code);
        } catch (Exception e) {
            return null;
        }
    }
}