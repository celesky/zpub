package com.celesky.zpub.service.publish.stage;

import ch.ethz.ssh2.Connection;
import com.celesky.zpub.config.ServerNodeConfig;
import com.celesky.zpub.service.publish.vo.CmdResult;
import com.celesky.zpub.common.utils.SShUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @desc:
 * @author: panqiong
 * @date: 2019-12-06
 */
@Component("EndStage")
@Slf4j
public class EndStage extends AbstractStaging {

    private static String STAGE_NAME = "发布后阶段(EndStage)";

    @Autowired
    ServerNodeConfig serverNodeConfig;


    @Override
    public boolean execute() {

        String information = "自动测试完成";

        // 登录目标服务器
        ServerNodeConfig.Node node = serverNodeConfig.getServerNode(super.packit.getReplica().getIp());
        Connection connection = SShUtil.login(node);
        String cmd = super.formatVairableCmd("docker ps |grep {serviceName}");
        CmdResult cmdRet = SShUtil.exec(connection,cmd);
        log.info(STAGE_NAME+information);
        //CmdResult cmdRet = SShUtil.exec(connection,"tail -5 /data/docker/log/sentry-service/sentry-service.log");
        super.sendMessage("\n"+cmdRet.toString()+"\n|"+information);

        return true;
    }

    @Override
    public String getStageName() {
        return STAGE_NAME;
    }
}