package com.celesky.zpub.service.publish.stage;

import ch.ethz.ssh2.Connection;
import com.celesky.zpub.common.utils.Pair;
import com.celesky.zpub.config.ServerNodeConfig;
import com.celesky.zpub.config.ServicesConfig;
import com.celesky.zpub.service.publish.vo.CmdResult;
import com.celesky.zpub.common.utils.SShUtil;
import com.celesky.zpub.service.publish.PubCommandRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * @desc: 发布阶段
 * @author: panqiong
 * @date: 2019-12-06
 */
@Component("PublishStage")
@Slf4j
public class PublishStage extends AbstractStaging {

    private static String STAGE_NAME = "部署执行阶段(PublishStage)";

    private static int detectStartupStatusTimeOut = 10;


    /**
     * 所有的服务配置
     */
    @Autowired
    ServicesConfig servicesConfig;

    /**
     * 所有的server节点配置
     */
    @Autowired
    ServerNodeConfig serverNodeConfig;


    @Override
    public boolean execute() {

        String information = "";
        // 登录目标服务器
        ServerNodeConfig.Node node = serverNodeConfig.getServerNode(super.packit.getReplica().getIp());

        Connection connection = SShUtil.login(node);
//        List<String> commandList = PubCommandRepo.getCommandList();
//        for(String command : commandList){
//            String cmd = super.formatVairableCmd(command);
//            CmdResult cmdRet = SShUtil.exec(connection,cmd);
//            if(cmdRet.fail()){
//                // 命令执行出错
//                information = " 发布过程中指令执行失败,流程终止.\n"+cmdRet.toString();
//
//                log.info(STAGE_NAME+information);
//                super.sendMessage(information);
//                // 流程终止
//                return false;
//            }
//        }



        CmdResult cmdRet = SShUtil.exec(connection,formatVairableCmd(PubCommandRepo.DOCKER_PULL));
        if(cmdRet.fail()){
            // 命令执行出错
            information = "镜像拉取执行失败,流程终止,请检查.\n"+cmdRet.toString();
            log.info(STAGE_NAME+information);
            super.sendMessage(information);
            // 流程终止
            return false;
        }

        Pair<Boolean,CmdResult> scf = stopContainerForce(connection);
        // 容器停止失败
        if(!scf.getKey()){
            CmdResult stopRet = scf.getValue();
            // 命令执行出错
            information = "容器停止失败,流程终止,请检查.\n"+stopRet.toString();
            log.info(STAGE_NAME+information);
            super.sendMessage(information);
            // 流程终止
            return false;
        }

        // 删除旧容器
        Pair<Boolean,CmdResult> rcf = rmContainerForce(connection);
        // 容器停止失败
        if(!rcf.getKey()){
            CmdResult rmRet = rcf.getValue();
            // 命令执行出错
            information = "删除旧失败,流程终止,请检查.\n"+rmRet.toString();
            log.info(STAGE_NAME+information);
            super.sendMessage(information);
            // 流程终止
            return false;
        }


        // 运行新容器
        CmdResult runRet = SShUtil.exec(connection,formatDockerRunCmd(PubCommandRepo.DOCKER_RUN));
        if(runRet.fail()){
            // 命令执行出错
            information = "启动容器执行失败,流程终止,请检查.\n"+runRet.toString();
            log.info(STAGE_NAME+information);
            super.sendMessage(information);
            // 流程终止
            return false;
        }


        Long starTime = Instant.now().getEpochSecond();
        while (true){

            // 如果超时10秒 直接中断探测
            if((Instant.now().getEpochSecond()-starTime)>detectStartupStatusTimeOut){
                information = " 容器启动失败,请介入检查 ("+detectStartupStatusTimeOut +"秒内进程没有启动) , 流程终止";
                log.info("[PublishStage]"+information);
                super.sendMessage(information);
                // 终止流程
                return false;
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // 检测容器是否启动成功
            CmdResult psRet = SShUtil.exec(connection,formatVairableCmd(PubCommandRepo.DOCKER_PS_SERVICE));
            // 容器已经启动
            if(psRet.success()){
                information = " 已成功执行部署命令,容器进程已经启动,进入服务启动阶段,请等待服务启动完成...";
                log.info(STAGE_NAME+information);
                super.sendMessage(information);
                return true;
            }

        }


    }

    /**
     * 先判断容器是否存在
     * 如果存在,则执行stop命令
     * @param connection
     * @return
     */
    private boolean stopContainer(Connection connection) {
        CmdResult cmdRet;
        String information;

        String psCmd = super.formatVairableCmd(PubCommandRepo.DOCKER_PS_SERVICE);
        cmdRet = SShUtil.exec(connection,psCmd);

        // 容器不存在
        if(cmdRet.getRet()==1){
            return true;
        }

        // 容器存在 需要先停止容器
        if(cmdRet.getRet()==0){
            // 存在运行的镜像,则停止这个容器,停止服务
            String stopCmd = super.formatVairableCmd(PubCommandRepo.DOCKER_STOP);
            cmdRet = SShUtil.exec(connection,stopCmd);

            return cmdRet.success();
        }


        return false;
    }

    /**
     * 直接执行stop命令
     * @param connection
     * @return
     */
    private Pair<Boolean,CmdResult> stopContainerForce(Connection connection) {
        CmdResult cmdRet;
        // 直接执行stop命令
        String stopCmd = super.formatVairableCmd(PubCommandRepo.DOCKER_STOP);
        cmdRet = SShUtil.exec(connection,stopCmd);
        return errorAnalysis(cmdRet);
    }


    /**
     * 直接执行stop命令
     * @param connection
     * @return
     */
    private Pair<Boolean,CmdResult> rmContainerForce(Connection connection) {
        CmdResult cmdRet;
        // 直接执行stop命令
        String stopCmd = super.formatVairableCmd(PubCommandRepo.DOCKER_RM);
        cmdRet = SShUtil.exec(connection,stopCmd);
        return errorAnalysis(cmdRet);
    }

    private Pair<Boolean, CmdResult> errorAnalysis(CmdResult cmdRet) {
        if (cmdRet.success()) {
            return new Pair<>(true, cmdRet);
        } else {
            // 如果返回报错,判断一下输出结果是否是: Error response from daemon: No such container
            if (cmdRet.getStdOutList() != null && cmdRet.getStdOutList().size() > 0) {
                boolean ok = cmdRet.getStdOutList().get(0).contains("No such container");
                return new Pair<>(ok, cmdRet);
            }

            if (cmdRet.getStdErrList() != null && cmdRet.getStdErrList().size() > 0) {
                boolean ok = cmdRet.getStdErrList().get(0).contains("No such container");
                return new Pair<>(ok, cmdRet);
            }
            return new Pair<>(false, cmdRet);
        }
    }


//    /**
//     * 判断容器是否存在
//     * @param cmdResult
//     * @return
//     */
//    private boolean isContanerExist(CmdResult cmdResult){
//        // 容器存在
//        if(cmdResult.success()){
//            return true;
//        }
//
//        if(cmdResult.getStdErrList()!=null&&cmdResult.getStdErrList().size()>0){
//            boolean err = cmdResult.getStdErrList().get(0).contains("No such container");
//            if(err){
//                return false;
//            }
//        }
//
//        if(cmdResult.getStdOutList()!=null&&cmdResult.getStdOutList().size()>0){
//            boolean err = cmdResult.getStdOutList().get(0).contains("No such container");
//
//            if(err){
//                return false;
//            }
//        }
//        return false;
//    }




    @Override
    public String getStageName() {
        return STAGE_NAME;
    }


}
