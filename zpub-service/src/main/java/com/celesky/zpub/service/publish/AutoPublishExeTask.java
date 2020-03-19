package com.celesky.zpub.service.publish;

import com.celesky.zpub.service.GroupMessageService;
import com.celesky.zpub.service.publish.stage.AbstractStaging;
import com.celesky.zpub.service.publish.vo.PublishPackit;
import com.celesky.zpub.common.enums.RobotEnums;
import com.celesky.zpub.common.utils.SpringBeanFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @desc:
 * @author: panqiong
 * @date: 2019-12-06
 */
@Slf4j
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AutoPublishExeTask implements Callable<Boolean> {


    PublishPackit packit;

    GroupMessageService groupRobotService;


    List<AbstractStaging> stagingList;



//    AutoPublishExeTask(List<AbstractStaging> stagingList,String serviceName, String imageName, ServicesConfig.Replica node , ServicesConfig.DockerRunOpt dockerRunOpt){
//        this.stagingList = stagingList;
//        this.packit = new PublishPackit(logId,serviceName,imageName,node, dockerRunOpt);
//
//
//    }

    /**
     * 初始化发布任务
     */
    AutoPublishExeTask(List<AbstractStaging> stagingList,PublishPackit packit){
        this.packit = packit;
        this.stagingList = stagingList;
        this.groupRobotService = SpringBeanFactory.getBean(GroupMessageService.class);
    }


    @Override
    public Boolean call() {
        if(stagingList==null||stagingList.size()==0){
            log.error("[自动发布引擎] 流程列表为空, 发布任务终止");
            groupRobotService.sendGroupNotifyMsg("[自动发布引擎] 流程列表为空, 发布任务终止", RobotEnums.prdEnvDeployRobot.code());
            return false;
        }


        // 开始执行所有发布步骤
        for(int i = 0; i<stagingList.size();i++){
            AbstractStaging staging = stagingList.get(i);
            boolean result = staging
                    .progressStage(stagingList.size(),i+1)
                    .packit(packit)
                    .execute();
            if(!result){
                // 流程终止
                log.error("[自动发布引擎]"+staging.getStageName()+" 该阶段执行失败,流程终止");
                groupRobotService.sendGroupNotifyMsg("[自动发布引擎]"+staging.getStageName()+" 该阶段执行失败,流程终止",RobotEnums.prdEnvDeployRobot.code());
                return false;
            }
        }

        return true;
    }
}
