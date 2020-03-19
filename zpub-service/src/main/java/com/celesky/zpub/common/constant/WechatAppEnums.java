package com.celesky.zpub.common.constant;


import com.zuzuche.msa.base.constants.BaseEnum;

/**
 * @desc: 周期对比类型  比如上一周同一时间  上一个月同一时间
 * @author: panqiong
 * @date: 2018/8/16
 */
public enum WechatAppEnums implements BaseEnum<Integer> {
    /**
     * 机器人哨兵
     */
    robot(1),
    /**
     * 发布助手
     */
    assist(2),

    /**
     * 产品反馈助手
     */
    product_robot(3),

    /**
     * 产品反馈助手
     */
    group_alarm_robot(4);

    public int code;


    WechatAppEnums(int code){
        this.code = code;
    }

    @Override
    public Integer code() {
        return this.code;
    }

    public static WechatAppEnums parse(Integer code) {
        try {
            return BaseEnum.parse(WechatAppEnums.class, code);
        } catch (Exception e) {
            return null;
        }
    }


}