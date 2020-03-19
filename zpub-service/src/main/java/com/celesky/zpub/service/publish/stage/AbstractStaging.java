package com.celesky.zpub.service.publish.stage;

import com.celesky.zpub.entity.PublishStagingLog;
import com.celesky.zpub.service.GroupMessageService;
import com.celesky.zpub.service.publish.PubStagingLogQueue;
import com.celesky.zpub.service.publish.vo.PublishPackit;
import com.celesky.zpub.common.enums.RobotEnums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @desc:
 * @author: panqiong
 * @date: 2019-12-06
 */
@Slf4j
public abstract class AbstractStaging {

    /**
     * 阶段性反馈
     */
    private static String GROUP_ROBOT_ID= RobotEnums.pubProgressFeedbackRobot.code();
    /**
     * 发布参数包
     */
    PublishPackit packit;
    /**
     * 当前的stage进度
     */
    Progress progress;

//    @Autowired
//    LiveCheckService liveCheckService;

    @Autowired
    GroupMessageService groupRobotService;



    public AbstractStaging packit(PublishPackit packit){
        this.packit = packit;
        return this;
    }
    public AbstractStaging progressStage(int total,int current){
        this.progress = new Progress(total,current);
        return this;
    }

    public abstract boolean execute();



    protected String getNodeInfo(){
        return "["+packit.getServiceName()+":"+ packit.getReplica().getHttpUri()+"]";
    }

    /**
     * 发送企业微信消息
     * @param information
     */
    public void sendMessage(String information){
        String info = "全自动发布流程引擎启动,正在发布中"+computeSpeed()+"\n"
                +"[当前阶段]"+getStageName()+"\n"
                +"[镜像名称]"+packit.getImageName()+"\n"
                +"[发布节点]"+getNodeInfo()+"\n"
                +"[处理信息]"+information+"\n";
        groupRobotService.sendGroupNotifyMsg(info,GROUP_ROBOT_ID);

        // 放进日志队列
        PublishStagingLog stagingLog = PublishStagingLog.builder()
                .publishInfo(info)
                .pubLogId(packit.getLogId())
                .stageName(getStageName())
                .createTime(LocalDateTime.now())
                .stageNo(progress.getCurrent())
                .build();
        PubStagingLogQueue.push(stagingLog);
    }

//    public String getSpeed(){
//        org.springframework.core.annotation.Order order = this.getClass().getAnnotation(org.springframework.core.annotation.Order.class);
//        int ov = order.value();
//    }

    private String computeSpeed() {
        int ov = progress.getCurrent();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        // 黑格子
        for(int i = 1; i <= ov; i++){
            if(i==1){
                sb.append("█");
            }else{
                sb.append(" █");
            }
        }
        // 空白
        int blank = progress.getTotal()-ov;
        for(int i = 1; i <=blank ; i++){
            sb.append("  _");
        }
        sb.append("]");
        sb.append(ov+"/"+ progress.getTotal());
        return sb.toString();
    }


    public abstract String getStageName();


    /**
     * 替换变量参数
     * @param command
     * @return
     */
    protected String formatVairableCmd(String command){
        // 参数替换
        command = command.replaceAll("\\{volume\\}",packit.getDockerRunOpt().getVolume());
        //command = command.replaceAll("\\{network\\}",packit.getDockerRunOpt().getNetwork());
        //command = command.replaceAll("\\{env\\}",packit.getDockerRunOpt().getEnv());
        command = command.replaceAll("\\{serviceName\\}",packit.getServiceName());
        command = command.replaceAll("\\{imageName\\}",packit.getImageName());
        return command;
    }

    /**
     * 替换变量参数
     * @param command
     * @return
     */
    protected String formatDockerRunCmd(String command){
        // 必填参数替换
        command = formatVairableCmd(command);

        StringBuilder optionsb = new StringBuilder("");
        // service级别的额外参数
        List<String> svcUltraOpts = packit.getDockerRunOpt().getUltraOpts();

        if(svcUltraOpts!=null&&svcUltraOpts.size()>0){
            for (String option : svcUltraOpts) {
                optionsb.append("--")
                        .append(option)
                        .append(" ");
            }
        }

        // replica级别的额外参数
        List<String> replicaUltraOpts = packit.getReplica().getUltraOpts();
        if(replicaUltraOpts!=null&&replicaUltraOpts.size()>0){
            for (String option : replicaUltraOpts) {
                optionsb.append("--")
                        .append(option)
                        .append(" ");
            }
        }
        // 额外参数替换
        command = command.replace("{_ultraOpts_}",optionsb.toString());

        return command;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Progress{
        /**
         * 总步骤数量
         */
        private int total;
        /**
         * 当前步骤编码
         */
        private int current;

    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("name=xxxx");
        list.add("abc=xxxx");
        list.add("efg=xxxx");
        StringBuilder opsb = new StringBuilder("");
        for (String option : list) {
            opsb.append("--")
                    .append(option)
                    .append(" ");
        }

        System.out.println("opsb = " + opsb.toString());

    }

}
