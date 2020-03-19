package com.celesky.zpub.common.constant;

import com.zuzuche.msa.base.constants.BaseEnum;

public enum LoopTypeEnums implements BaseEnum<Integer> {
    /**
     * 全部
     */
    NOLOOP(0),
    /**
     * 累加
     */
    DAY(1),

    /**
     * 平均
     */
    WEEK(2),

    /**
     * 最小
     */
    MONTH(3);


    public Integer code;


    LoopTypeEnums(Integer code) {
        this.code = code;
    }

    @Override
    public Integer code() {
        return null;
    }

    public static FunctionEnums parse(Integer code) {
        try {
            return BaseEnum.parse(FunctionEnums.class, code);
        } catch (Exception e) {
            return null;
        }
    }
}