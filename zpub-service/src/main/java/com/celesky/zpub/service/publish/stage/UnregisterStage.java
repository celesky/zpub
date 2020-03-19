package com.celesky.zpub.service.publish.stage;

import com.celesky.zpub.config.ServerNodeConfig;
import com.celesky.zpub.service.OpsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @desc:
 * @author: panqiong
 * @date: 2019-12-06
 */
@Slf4j
@Component("UnregisterStage")
public class UnregisterStage extends AbstractStaging {

    private static String STAGE_NAME = "反注册阶段(UnregisterStage)";

    private static final int WAITING_TIME = 60;
    @Autowired
    ServerNodeConfig serverNodeConfig;

    @Autowired
    OpsService opsService;

    @Override
    public boolean execute() {
        String information = "";
        String url = "http://" + packit.getReplica().getHttpUri();
        try {
            information = opsService.unregister(url);
        } catch (Exception e) {
            information = "调用unregister接口出现异常:" + e.getMessage();
            log.error("UnregisterStage异常:", e.getMessage(), e);
        }
        information = information+" ;等待"+WAITING_TIME+"秒,流量将自动摘除";
        log.info(STAGE_NAME+information);
        super.sendMessage(information);

        try {
            Thread.sleep(WAITING_TIME*1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return true;
    }

    @Override
    public String getStageName() {
        return STAGE_NAME;
    }
}
