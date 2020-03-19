package com.celesky.zpub.common.enums;

import com.zuzuche.msa.base.constants.BaseEnum;

/**
 * @desc:
 * @author: panqiong
 * @date: 2019-09-20
 */
public enum RobotEnums implements BaseEnum<String> {

    batchSmsAuthorizeRobot("batchSmsAuthorizeRobot"),

    sentryRobot("sentryRobot"),

    TestEnvDeployRobot("TestEnvDeployRobot"),

//    prdEnvDeployRobot("prdEnvDeployRobot"),
    prdEnvDeployRobot("pubProgressFeedbackRobot"),

    pubProgressFeedbackRobot("pubProgressFeedbackRobot");

    private String code;

    RobotEnums(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return code;
    }

    public static RobotEnums parse(String code) {
        try {
            return BaseEnum.parse(RobotEnums.class, code);
        } catch (Exception e) {
            return null;
        }
    }
}