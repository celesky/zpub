package com.celesky.zpub.common.constant;


import com.zuzuche.msa.base.constants.BaseEnum;

public enum FunctionEnums implements BaseEnum<String> {
    /**
     * 全部
     */
    ALL("all"),
    /**
     * 累加
     */
    SUM("sum"),

    /**
     * 平均
     */
    AVG("avg"),

    /**
     * 最小
     */
    MIN("min"),

    /**
     * 最大
     */
    MAX("max");

    public String code;


    FunctionEnums(String code){
        this.code = code;
    }
    @Override
    public String code() {
        return null;
    }

    public static FunctionEnums parse(String code) {
        try {
            return BaseEnum.parse(FunctionEnums.class, code);
        } catch (Exception e) {
            return null;
        }
    }
}
