package com.celesky.zpub.common.enums;

import com.zuzuche.msa.base.constants.BaseEnum;

/**
 * @desc:
 * @author: panqiong
 * @date: 2019-09-20
 */
public enum  SentryCommand implements BaseEnum<String> {
    /**
     * 线上发布
     */
    PUBLISH("publish"),
    /**
     * 去除发布频率限制
     */
    RMLIMIT("rmlimit"),
    /**
     * 发布命令
     */
    DEPLOY("发布"),

    /**
     * 静默命令
     */
    SILENT("silent"),

    /**
     * 关闭静默
     */
    RMSILENT("rmsilent"),

    /**
     * 测试指令
     */
    TEST("test"),

    /**
     * 测试指令
     */
    HISTORY("history");

    private String code;

    SentryCommand(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return code;
    }

    public static SentryCommand parse(String code) {
        try {
            return BaseEnum.parse(SentryCommand.class, code);
        } catch (Exception e) {
            return null;
        }
    }
}