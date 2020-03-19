package com.celesky.zpub.service.publish.stage;

import com.celesky.zpub.service.OpsService;
import com.zuzuche.msa.base.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * @desc:
 * @author: panqiong
 * @date: 2019-12-06
 */
@Component("StartupStage")
@Slf4j
public class StartupStage extends AbstractStaging {

    private static String STAGE_NAME = "服务启动阶段(StartupStage)";

    /**
     * 服务启动探测超时时间
     */
    private static int detectStartupStatusTimeOut = 90;


    @Autowired
    OpsService opsService;

    @Override
    public boolean execute() {

        String information = "";
        Long starTime = Instant.now().getEpochSecond();
        try {
            // 休眠10秒
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        while (true){
            Long endTime = Instant.now().getEpochSecond();

            // 如果超时直接中断探测
            if((endTime-starTime)>detectStartupStatusTimeOut){
                information = " 启动超时,请介入检查 ("+detectStartupStatusTimeOut +"秒内没有启动完成) , 流程终止";
                log.info("[StartupStage]"+information);
                super.sendMessage(information);
                // 终止流程
                return false;
            }

            StringBuilder autoTestUrlBuilder = new StringBuilder();
            autoTestUrlBuilder
                    .append("http://")
                    .append(packit.getReplica().getHttpUri());

            if(StringUtil.isNotEmpty(packit.getAutoTestUri()) && !packit.getAutoTestUri().startsWith("/")){
                autoTestUrlBuilder.append(packit.getAutoTestUri());
            }else{
                autoTestUrlBuilder.append(packit.getAutoTestUri());
            }

            boolean status = opsService.autoTest(autoTestUrlBuilder.toString());

            if(status){
                long timeCost = endTime-starTime;
                // 启动完成
                information = " 已经启动完成(包含自动测试),现在已经可以正常提供服务; 服务启动耗时:"+timeCost +" 秒";

                log.info("[StartupStage]"+information);
                super.sendMessage(information);
                // 流程继续
                return true;
            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                // 启动完成
                information = " 服务启动过程监控被中断.";
                log.info(STAGE_NAME+information);
                super.sendMessage(information);
                return false;
            }


        }

    }

    @Override
    public String getStageName() {
        return STAGE_NAME;
    }
}