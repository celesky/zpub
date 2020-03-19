package com.celesky.zpub.service.publish;

import com.celesky.zpub.config.ServicesConfig;
import com.celesky.zpub.entity.PublishLog;
import com.celesky.zpub.mapper.PublishLogMapper;
import com.celesky.zpub.service.GroupMessageService;
import com.celesky.zpub.service.publish.stage.AbstractStaging;
import com.celesky.zpub.service.publish.stage.StagingManager;
import com.celesky.zpub.service.publish.vo.PublishPackit;
import com.zuzuche.msa.threadpool.ThreadPoolExecutorFactory;
import com.celesky.zpub.common.enums.RobotEnums;
import com.celesky.zpub.common.utils.SpringBeanFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;

/**
 * @desc: 自动发布服务
 * @author: panqiong
 * @date: 2019-12-06
 */
@Service
@Slf4j
public class AutoPublishService {
    /**
     * 暂时只允许一次发布一个服务
     */
    ExecutorService bossGroup = ThreadPoolExecutorFactory.create(ThreadPoolExecutorFactory.Config.builder()
            .corePoolSize(1)
            .maximumPoolSize(1)
            .keepAliveTime(5)
            .workQueue(new ArrayBlockingQueue<>(1))
            .unit(TimeUnit.MINUTES)
            .handler(new ThreadPoolExecutor.AbortPolicy())
            .threadPoolName("auto-creator-pool")
            .build());

    /**
     * 执行器线程池
     */
    ExecutorService exeGroup = ThreadPoolExecutorFactory.create(ThreadPoolExecutorFactory.Config.builder()
                 .corePoolSize(1)
                 .maximumPoolSize(1)
                 .keepAliveTime(5)
                 .workQueue(new ArrayBlockingQueue<>(10))
                 .unit(TimeUnit.MINUTES)
                 .handler(new ThreadPoolExecutor.CallerRunsPolicy())
                 .threadPoolName("auto-publish-pool")
                 .build());

    @Autowired
    ServicesConfig servicesConfig;

    @Autowired
    GroupMessageService groupRobotService;


    @Autowired
    PublishLogMapper publishLogMapper;

    /**
     * 结果反馈
     * pubProgressFeedbackRobot
     * prdEnvDeployRobot
     */
    private static String GROUP_ROBOT_ID= RobotEnums.prdEnvDeployRobot.code();



    private static int NEXT_WAITING_TIME = 60;

    /**
     *
     * @param serviceName
     * @param imageName
     * @param quikPub 是否启用快速发布,true是启用,快速发布的情况下,将不会有流量摘除步骤
     */
    public void createPublishTask(String pubMan,String serviceName,String imageName,boolean quikPub){

        // 服务配置
        ServicesConfig.Service service = servicesConfig.getServiceInfo(serviceName);
        // 副本集合
        List<ServicesConfig.Replica> replicas = service.getReplicaList();
        // dockerrun的主要参数
        ServicesConfig.DockerRunOpt dockerRunOpt = service.getDockerRunOpt();
        // 发布步骤
        List<AbstractStaging> stagingList = StagingManager.getStagingList(quikPub);

        groupRobotService.sendGroupNotifyMsg(
                "[发布任务引擎]\n" +
                        "[服务]"+serviceName + "\n"+
                        "[镜像]"+imageName + "\n"+
                        "[节点]"+replicas.toString()+"\n" +
                        "[信息] 开始启动创建发布任务,发布模式:"+(quikPub?"快速发布流程":"常规发布流程")
                ,GROUP_ROBOT_ID);

        // 记录日志
        int logId = savePubLog(pubMan, serviceName, imageName);

        // 任务主线程池
        bossGroup.submit(()->{
            // 滚动重启 创建自动化任务
            for(int i = 0;i < replicas.size(); i++){
                ServicesConfig.Replica replica = replicas.get(i);
                try{
                    // 循环创建发布执行器
                    PublishPackit packit = PublishPackit.builder()
                            .logId(logId)
                            .dockerRunOpt(dockerRunOpt)
                            .serviceName(serviceName)
                            .imageName(imageName)
                            .unregisterUri(service.getUnregisterUri())
                            .autoTestUri(service.getAutoTestUri())
                            .replica(replica)
                            .build();

                    AutoPublishExeTask task = SpringBeanFactory.getBean(AutoPublishExeTask.class,stagingList,packit);
                    // 交给执行器线程池处理
                    Future<Boolean> future = exeGroup.submit(task);
                    Boolean result;
                    String errInfo="";
                    try {
                        result = future.get();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        result = false;
                        errInfo = e.getMessage();
                        log.error("[发布任务引擎] 任务执行中断",e.getMessage(),e);
                    } catch (ExecutionException e) {
                        result = false;
                        errInfo = e.getMessage();
                        log.error("[发布任务引擎] 任务执行异常",e.getMessage(),e);
                    } catch (Exception e) {
                        result = false;
                        errInfo = e.getMessage();
                        log.error("[发布任务引擎] 任务执行异常",e.getMessage(),e);
                    }

                    if(!result){
                        String info="[发布任务引擎] 节点发布失败,发布流程终止:"+replica.toString()+" result:"+result+" |errInfo:"+errInfo;
                        log.error(info);
                        updateLogResult(logId,"fail" ,info);
                        return;
                    }

                    if(i<replicas.size()-1){
                        int waitingTime = NEXT_WAITING_TIME;
                        // 快速发布只等10秒
                        if(quikPub){
                            waitingTime = 10;
                        }
                        groupRobotService.sendGroupNotifyMsg(
                                "[发布任务引擎]\n" +
                                        "[服务名称] "+serviceName+ "\n " +
                                        "[任务结果] 节点发布: SUCCESS , 自动测试: SUCCESS \n " +
                                        "[节点信息] "+replica.toString()+" \n"  +
                                        "[整体进度] "+ (i+1) + "/" +replicas.size()+"\n" +
                                        "[反馈信息] 除非人为终止,否则"+waitingTime+"秒后,将开始发布下一个节点"
                                ,GROUP_ROBOT_ID);

                        try {
                            Thread.sleep(waitingTime*1000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            String info = "[发布任务引擎] 任务执行中断";
                            log.error(info,e.getMessage(),e);
                            updateLogResult(logId,"fail" ,info);
                            return;
                        }

                    }else{
                        // 已经是最后一个发布节点
                        String info = "[发布任务引擎]\n" +
                                "[服务名称] "+serviceName+ "\n " +
                                "[任务结果] 节点发布: SUCCESS , 自动测试: SUCCESS \n " +
                                "[节点信息] "+replica.toString()+" \n"  +
                                "[整体进度] "+ (i+1) + "/" +replicas.size()+"\n" +
                                "[反馈信息] 所有节点("+replicas.size()+")都已经成功发布完成!  本次发布结束!";

                        groupRobotService.sendGroupNotifyMsg(info,GROUP_ROBOT_ID);

                        updateLogResult(logId,"success" ,info);
                    }
                }catch (Exception e){
                    log.error("[发布任务引擎] 出现异常,发布流程终止:"+replica.toString(),e.getMessage(),e);

                    String info = "[发布任务引擎] 出现异常,发布流程终止:"+replica.toString()+" |异常信息:"+e.getMessage();
                    groupRobotService.sendGroupNotifyMsg(info,GROUP_ROBOT_ID);
                    updateLogResult(logId,"success" ,info);
                    return;
                }
            }
        });


    }

    /**
     * 保存发布日志
     * @param pubMan
     * @param serviceName
     * @param imageName
     * @return
     */
    public int savePubLog(String pubMan, String serviceName, String imageName) {
        try{
            PublishLog publishLog = PublishLog.builder()
                    .publishMan(pubMan)
                    .command("publish")
                    .service(serviceName)
                    .imageName(imageName)
                    .createTime(LocalDateTime.now())
                    .build();
            publishLogMapper.insert(publishLog);
            return publishLog.getId();
        }catch (Exception e){
            log.error("日志保存出错:",e.getMessage(),e);
        }
        return 0;

    }

    /**
     * 更新发布日志结果
     * @param logId
     * @param result
     * @param info
     */
    public void updateLogResult(int logId, String result , String info) {
        try {
            PublishLog log = PublishLog.builder()
                    .id(logId)
                    .publishResult(result)
                    .publishInfo(info)
                    .build();
            publishLogMapper.updateByPrimaryKeySelective(log);
        }catch (Exception e ){
            log.error("日志保存出错:",e.getMessage(),e);
        }

    }


}
