package com.celesky.zpub.service.publish.stage;

import ch.ethz.ssh2.Connection;
import com.celesky.zpub.config.ServerNodeConfig;
import com.celesky.zpub.service.publish.vo.CmdResult;
import com.zuzuche.msa.base.util.StringUtil;
import com.celesky.zpub.common.utils.SShUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @desc:
 * @author: panqiong
 * @date: 2019-12-06
 */
@Slf4j
@Component("PreCheckStage")
public class PreCheckStage extends AbstractStaging{

    private static String STAGE_NAME = "预检查阶段(preCheck)";

    @Autowired
    ServerNodeConfig serverNodeConfig;


    @Override
    public boolean execute() {


        String information = " 预检查通过";

        // 获取目标服务器配置
        ServerNodeConfig.Node node = serverNodeConfig.getServerNode(super.packit.getReplica().getIp());

        /**
         * 没有对应的机器节点配置信息
         */
        if(node==null){
            information = " 目标服务器node未配置,ip:"+super.packit.getReplica().getIp();
            log.error(STAGE_NAME+information);
            super.sendMessage(information);
            return false;
        }

        if(StringUtil.isEmpty(super.packit.getAutoTestUri())){
            information = " 服务未配置autoTestUri:,发布终止";
            log.error(STAGE_NAME+information);
            super.sendMessage(information);
            return false;
        }




        // 检查是否能连接上 以及镜像是否存在
        Connection connection = SShUtil.login(node);

        if(connection==null){
            super.sendMessage("预检查不通过,流程终止 : connection为空,未能成功打开连接!");
            return false;
        }

        // 镜像检查
        String cmd = super.formatVairableCmd("docker pull {imageName}");
        CmdResult cmdRet = SShUtil.exec(connection,cmd);
        if(cmdRet.fail()){
            // 命令执行出错
            information = " 拉取镜像出错,流程终止.\n"+cmdRet.toString();
            log.info(STAGE_NAME+information);
            super.sendMessage(information);
            // 流程终止
            return false;
        }


        // 回滚镜像保存
        cmd = super.formatVairableCmd("docker ps |grep {serviceName}");
        cmdRet = SShUtil.exec(connection,cmd);
        // 有结果
        if(cmdRet.success()){
            information = "当前节点正在运行的容器:\n"+cmdRet.getStdOutList().toString();
        }else{
            information = " 注意: 当前节点目前没有正在运行名称为"+super.packit.getServiceName()+"的容器 , 如果本次发布失败 , 将不会有自动回滚操作 ";
        }



        // 是否有正在运行的同名容器
//        if(cmdRet.getStdOutList()==null||cmdRet.getStdOutList().size()==0){
//            information = " 注意: 当前节点目前没有正在运行名称为"+super.packit.getServiceName()+"的容器 , 如果本次发布失败 , 将不会有自动回滚操作 ";
//            //log.info(STAGE_NAME+information);
//            //super.sendMessage(information);
//        }else{
//
//            information = "当前节点正在运行的容器:\n"+cmdRet.getStdOutList().toString();
//            log.info(STAGE_NAME+information);
            // 有正在运行的同名容器,保存其镜像名称用于回滚
//            List<DockerPsRet> psRetList = DockerPsRet.from(cmdRet.getStdOutList());
//            if(psRetList == null|| psRetList.size() ==0){
//                information = "解析回滚镜像名称时出现了一些问题, 请检查错误日志, 本次发布流程终止";
//                log.info(STAGE_NAME+information);
//                super.sendMessage(information);
//                return false;
//            }
//
//            if(psRetList.size() > 1){
//                information = "有多个正在运行的同名"+super.packit.getServiceName()+" 容器, 请先检查, 本次发布流程终止";
//                log.info(STAGE_NAME+information);
//                super.sendMessage(information);
//                return false;
//            }
//
//            DockerPsRet psRet = psRetList.get(0);
//            log.info("[PreCheckStage]当前节点正在运行容器:{} {} {} {}",psRet.getContainerId(),psRet.getImage(),psRet.getNames(),psRet.getStatus());

            //  todo 保存这个容器记录,用于回滚
//        }


        log.info(STAGE_NAME+information);
        super.sendMessage(information);

        return true;

    }





    @Override
    public String getStageName() {
        return STAGE_NAME;
    }

    public static void main(String[] args) {
        String cmd = "docker ps {serviceName}";
        cmd = cmd.replaceAll("\\{serviceName\\}","sms-service");
        System.out.println("cmd = " + cmd);
    }
}
