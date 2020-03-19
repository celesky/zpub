package com.celesky.zpub.service.publish.stage;

import com.celesky.zpub.common.utils.SpringBeanFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * @desc: 在这里拼接组合各种步骤
 * @author: panqiong
 * @date: 2019-12-30
 */
@Slf4j
@Component
public class StagingManager implements ApplicationListener<ApplicationReadyEvent> {


    private static boolean loadingStaginOk = false;

    private static List<AbstractStaging> publishList = new LinkedList();

    private static List<AbstractStaging> quikpubList = new LinkedList();

    public static boolean isReady(){
        return loadingStaginOk;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        initPublishStages();
        initQuikpubStages();
        loadingStaginOk = true;
    }


    private synchronized void initPublishStages(){
        publishList.clear();
        register(publishList,"PreCheckStage");
        register(publishList,"UnregisterStage");
        register(publishList,"PublishStage");
        register(publishList,"StartupStage");
        register(publishList,"EndStage");

    }

    private synchronized void initQuikpubStages(){
        quikpubList.clear();

        register(quikpubList,"PreCheckStage");
        // 快速发布 不做下流量的过程了 直接重启
        //register(quikpubList,"UnregisterStage");
        register(quikpubList,"PublishStage");
        register(quikpubList,"StartupStage");
        register(quikpubList,"EndStage");
    }

    /**
     * 注册步骤
     * @param stagings
     * @param stageName
     */
    public void register(List<AbstractStaging> stagings,String stageName){
        try{
            AbstractStaging staging = (AbstractStaging) SpringBeanFactory.getBean(stageName);
            if(staging!=null){
                stagings.add(staging);
            }
        }catch (Exception e){
            log.error("[注册stage失败]stageName:"+stageName,e.getMessage(),e);
            throw  e;
        }
    }

    /**
     * 常规发布步骤
     * @return
     */
    public static List<AbstractStaging> getStagingList(boolean quik){
        if(quik){
            return quikpubList;
        }else{
            return publishList;
        }
    }

    /**
     * 常规发布步骤
     * @return
     */
    public static List<AbstractStaging> getPublishStagingList(){
        return publishList;
    }


    /**
     * 快速发布步骤
     * @return
     */
    public static List<AbstractStaging> getQuikpubStagingList(){
        return quikpubList;
    }




}
