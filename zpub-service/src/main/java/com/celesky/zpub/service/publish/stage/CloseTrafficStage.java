package com.celesky.zpub.service.publish.stage;

import lombok.extern.slf4j.Slf4j;

/**
 * @desc:
 * @author: panqiong
 * @date: 2019-12-06
 */
//@Component
@Slf4j
public class CloseTrafficStage extends AbstractStaging{

    private static String STAGE_NAME = "摘除流量阶段(CloseTrafficStage)";


    @Override
    public boolean execute() {

        String information = "(等待30秒)流量已自动摘除";
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        log.info(STAGE_NAME+information);
        super.sendMessage(information);

        return true;

    }

    @Override
    public String getStageName() {
        return STAGE_NAME;
    }


}