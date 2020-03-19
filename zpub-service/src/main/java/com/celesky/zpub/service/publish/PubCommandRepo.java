package com.celesky.zpub.service.publish;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 变量解释:
 *
 * #{imageName} :镜像名称,jib打包之后,可以从日志中看到,如: Built and pushed image as harbor-release.zuzuche.net/zzc/sms-service:20191205_021658
 * #{serviceName} :服务名称,也会用作容器名称,如sms-service
 *
 * @desc: 命令对象
 * @author: panqiong
 * @date: 2019-12-05
 */
public class PubCommandRepo {

    public static final String DOCKER_RUN =
                    "docker run -dit " +
                            "--name {serviceName} " +
                            "--volume {volume} " +
//                            "--network {network} " +
//                            "--cpu-period=1000000 " +
//                            "--cpu-quota=1000000 " +
//                            "--env '{env}'  " +
                            "{_ultraOpts_}"+
                            "{imageName}";

    public static final String DOCKER_PULL = "docker pull {imageName}";

    public static final String DOCKER_STOP = "docker stop {serviceName}";

    public static final String DOCKER_RM = "docker rm {serviceName}";

    public static final String DOCKER_PS_SERVICE = "docker ps | grep {serviceName}";




    private static List<String> commandList = new ArrayList<>();

    public static void init() {
        // 打印当前运行的容器
        commandList.add("docker ps");

        // 拉取镜像
        commandList.add("docker pull {imageName}");

        // 停止容器 停服
        commandList.add("docker stop {serviceName}");

        // 删除原容器
        commandList.add("docker rm {serviceName}");

        // 根据镜像启动容器
        commandList.add(DOCKER_RUN);

        // 打印当前运行的容器
        commandList.add("docker ps");
    }

    public static List<String> getCommandList(){
        if(commandList.size()==0){
            init();
        }
        return commandList;
    }




    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    @Builder
    public static class Command{
        /**
         * 命令行
         */
        private String cmdStr;

        /**
         * 如果命令执行失败,是否需要中断流程
         * true: 若执行失败,必须终止整个流程
         * false: 若执行失败,可以继续流程
         */
        private boolean interrupt;

    }


}
