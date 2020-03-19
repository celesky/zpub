package com.celesky.zpub.common.constant;


import com.zuzuche.msa.base.constants.BaseEnum;

/**
 * @desc: 策略类型 1：数量， 2：百分比率
 * @author: panqiong
 * @date: 2018/8/16
 */
public enum StrategyTypeEnums implements BaseEnum<String> {
    /**
     * 数量
     */
    count("1"),

    /**
     * 百分比
     */
    percentage("2");

    public String code;

    StrategyTypeEnums(String code){
        this.code = code;
    }
    @Override
    public String code() {
        return code;
    }

    public static StrategyTypeEnums parse(String code) {
        try {
            return BaseEnum.parse(StrategyTypeEnums.class, code);
        } catch (Exception e) {
            return null;
        }
    }
}
