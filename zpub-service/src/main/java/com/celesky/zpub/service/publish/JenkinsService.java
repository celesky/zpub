package com.celesky.zpub.service.publish;

import com.celesky.zpub.cache.DeployLimitCache;
import com.celesky.zpub.cache.SilentCache;
import com.celesky.zpub.config.JenkinsConfig;
import com.celesky.zpub.config.ServicesConfigQa;
import com.celesky.zpub.jenkins.JenkinsBuildStatus;
import com.celesky.zpub.remote.JenkinsRemote;
import com.celesky.zpub.remote.SentryRobotRemote;
import com.celesky.zpub.service.GroupMessageService;
import com.celesky.zpub.service.WechatMessageService;
import com.zuzuche.msa.base.resp.StatusServiceCnException;
import com.celesky.zpub.common.constant.WechatAppEnums;
import com.celesky.zpub.common.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;
import java.util.List;

/**
 * @desc: jenkins相关
 * @author: panqiong
 * @date: 2019-05-04
 */
@Service
@Slf4j
public class JenkinsService {

//    public static String jenkinsUrl = "http://jks-repositories.zuzuche.com/view/%E5%AE%A2%E6%9C%8D%E6%8A%80%E6%9C%AF%E6%B5%8B%E8%AF%95";
//
//    private static String templateUrl = "http://jks-repositories.zuzuche.com/view/客服技术测试/job/{service_name}/buildWithParameters?token=don't_modify_this&branch={branch}&cause={cause}";
//
//    private static String templateDockerBuildUrl = "http://jks-repositories.zuzuche.com/view/客服技术测试/job/{service_name}-docker/buildWithParameters?token=don't_modify_this&branch={branch}&imageName={imageName}&cause={cause}";
//
//    //curl http://jks-repositories.zuzuche.com/job/stream-service/lastBuild/api/xml --user panqiong:289289
//    private static String buildStatusUrl = "http://jks-repositories.zuzuche.com/job/{service_name}/lastBuild/api/xml";
//
//
//    private static String jobUrl = "http://jks-repositories.zuzuche.com/view/%E5%AE%A2%E6%9C%8D%E6%8A%80%E6%9C%AF%E6%B5%8B%E8%AF%95/job";
//
//
//    private static String HARBOR_BASE_NAME ="harbor-release.zuzuche.net/zzc/{service_name}:";


    @Autowired
    JenkinsConfig jenkinsConfig;

    @Autowired
    JenkinsRemote jenkinsRemote;

    @Autowired
    WechatMessageService messageService;


    @Autowired
    DeployLimitCache deployLimitCache;


    @Autowired
    SilentCache silentCache;

    @Autowired
    SentryRobotRemote sentryRobotRemote;


    @Autowired
    GroupMessageService groupRobotService;




    @Autowired
    ServicesConfigQa servicesConfigQa;


    /**
     * 镜像名称
     * @return
     */
    private String generateImageName(String serviceName,String branch){
        String label = branch+"_"+ DateUtil.getCurrentTime(DateUtil.pattern_4);
        return jenkinsConfig.getHarborBaseName()
                .replace("{service_name}",serviceName)
                .replace("{label}",label);
    }

    /**
     * 触发远程构建
     * @param serviceName
     * @param branch
     */
    public void triggerDeploy(String userName,String serviceName,String branch){
        String instruction = serviceName+" "+branch;

        // 生成镜像名称
        String imageName = generateImageName(serviceName,branch);

        String  url = jenkinsConfig.getTemplateBuildUrl()
                        .replace("{service_name}",serviceName)
                        .replace("{branch}",branch)
                        .replace("{imageName}",imageName)
                        .replace("{cause}","由"+userName+"通过企业微信发布助手远程触发");
        boolean deployResult = false;
        try{
            jenkinsRemote.invokeBuild(url);
            deployResult = true;
        }catch (StatusServiceCnException ex){
            deployResult = true;
        }catch (HttpClientErrorException ex){
            if(HttpStatus.NOT_FOUND.equals(ex.getStatusCode())){
                //项目不存在
                messageService.sendWechatMsg(userName,"发送远程构建命令["+instruction+"]失败了! 该服务不存在,请检查命令或者jenkins配置! "+instruction, WechatAppEnums.assist);
            }else{
                messageService.sendWechatMsg(userName,"发送远程构建命令"+instruction+"失败了! 服务不存在,错误码:"+ex.getMessage(), WechatAppEnums.assist);
            }
        }catch (Exception e){
            messageService.sendWechatMsg(userName,"发送远程构建命令失败了! "+instruction, WechatAppEnums.assist);
            log.error("调用远程触发构建url失败:"+url);
            log.error("调用远程触发构建url失败",e.getMessage(),e);
        }
        // 命令发送成功
        if(deployResult){
            String jurl = jenkinsConfig.getJobUrl().replace("{service_name}",serviceName);
            messageService.sendWechatMsg(userName,
                          "[提交结果] 已经成功提交构建指令! \n" +
                                "[镜像名称]:" +imageName+" \n"+
                                "[查看进度] [点击]("+jurl+")", WechatAppEnums.assist);
            // 开启一个异步任务,监控任务结果
            new Thread(()-> {
                // 启动应用构建监控
                boolean buildingSuccess = startBuildingMonitor(serviceName, userName,imageName);
                // 启动应用启动监控
                if(buildingSuccess){
                    startAppBootingMonitor(serviceName, userName ,imageName);
                }

            }).start();
        }else{
            // 如果发布失败,移除30秒的限制
            deployLimitCache.remove(serviceName);
        }
    }

    /**
     * 获取最后一次构建信息
     */
    public JenkinsBuildStatus getJenkinLastBuildStatus(String serviceName){
        String infoUrl = jenkinsConfig.getBuildStatusUrl().replace("{service_name}",serviceName);
        JenkinsBuildStatus status = jenkinsRemote.getLastBuildInfo(infoUrl);
        return status;
    }

    /**
     * 开启一个构建结果监控器
     */
    public boolean startBuildingMonitor(String serviceName,String userName,String imageName) {
        log.info("startResultMonitor 启动一个发布结果监控器:"+serviceName+" "+userName);
        Long starTime = Instant.now().getEpochSecond();
        JenkinsBuildStatus status = null;
        boolean buildingResult = false;
        try {
            while (true) {
                Long endTime = Instant.now().getEpochSecond();
                // 如果超过5分钟,直接中断
                if(endTime-starTime>600){
                    log.info("startResultMonitor "+serviceName+" "+userName+" 监控线程超过10分钟,直接终止");
                    messageService.sendWechatMsg(userName,"服务构建阶段超时,请检查", WechatAppEnums.assist);
                    break;
                }

                // 休息10秒再拉取一次
                Thread.sleep(10000);
                status = getJenkinLastBuildStatus(serviceName);

                if(status==null){
                    messageService.sendWechatMsg(userName,"获取构建结果失败了,请于jenkins上查看结果", WechatAppEnums.assist);
                    // 中断监控
                    break;
                }
                // 是否还在构建中
                if(status.isBuiding()){
                    continue;
                }

                if(status!=null && "SUCCESS".equals(status.getResult())){
                    buildingResult = true;
                }else{
                    buildingResult = false;
                }
                String info = buildingResult?"服务已进入启动阶段...":"由于构建失败,发布终止";
                // 构建完成,发送构建结果
                String result = serviceName + " 构建完成 \n" +
                                "[镜像名称]: " + imageName + "\n" +
                                "[构建结果]: " + status.getResult() + "\n" +
                                "[详细信息]: "+ info;
                messageService.sendWechatMsg(userName,result, WechatAppEnums.assist);

                // 发送一条群聊通知
                groupRobotService.sendGroupNotifyMsg(result,"TestEnvDeployRobot",userName);

                break;
            }
        }catch(InterruptedException e){
            log.error("startResultMonitor异常"+e.getMessage(),e);
        }
        log.info("startResultMonitor_结束一个发布结果监控器:"+serviceName+" "+userName);

        return buildingResult;


    }


    /**
     * 开启应用启动结果监控器
     */
    public void startAppBootingMonitor(String serviceName,String userName,String imageName) {
        log.info("startAppBootingMonitor 启动一个启动结果监控器:"+serviceName+" "+userName);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            log.error("休眠报错:",e.getMessage(),e);
            Thread.currentThread().interrupt();
        }

        Long starTime = Instant.now().getEpochSecond();
        try {
            while (true) {
                Long endTime = Instant.now().getEpochSecond();
                // 启动时间超过60秒  直接预警
                if(endTime-starTime>60){
                    // 启动完成
                    String result =
                            serviceName+"服务未能正常启动,超时时间1分钟,请检查" ;

                    log.info("startResultMonitor "+serviceName+" "+userName+" 监控线程超过1分钟,直接终止");
                    messageService.sendWechatMsg(userName,result, WechatAppEnums.assist);
                    // 发送一条群聊通知
                    groupRobotService.sendGroupNotifyMsg(result,"TestEnvDeployRobot",userName);
                    break;
                }

                // 休息10秒再开启拉取
                Thread.sleep(2000);
                // 当前服务的ip地址 todo 暂时只考虑1个qa环境实例的情况
                List<ServicesConfigQa.Replica> nodeList = servicesConfigQa.getReplicaList(serviceName);
                String url = "http://"+ nodeList.get(0).getHttpUri();
                //boolean status = liveCheckService.evaluateInstance(url);
                // todo
                boolean status = true;
                if(status){
                    // 启动完成
                    String result =
                            serviceName+"服务已经启动完成,现在已经可以正常提供服务 \n" +
                            "[镜像名称]: "+imageName;
                    messageService.sendWechatMsg(userName,result, WechatAppEnums.assist);

                    // 发送一条群聊通知
                    groupRobotService.sendGroupNotifyMsg(result,"TestEnvDeployRobot",userName);

                    break;
                }
            }
        }catch(InterruptedException e){
            log.error("startResultMonitor异常"+e.getMessage(),e);
            Thread.currentThread().interrupt();
        }

        log.info("startResultMonitor_结束一个启动结果监控器:"+serviceName+" "+userName);
    }




}
