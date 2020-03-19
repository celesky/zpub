package com.celesky.zpub.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @desc: 发布阶段的命令行
 * @author: panqiong
 * @date: 2019-12-09
 */
//@Component("publishStageCmdConfig")
//@RefreshScope
//@ConfigurationProperties(prefix = "publish-stage-cmd-list")
//@Data
public class PublishStageCmdConfig {

    /**
     * 服务列表
     */
    private List<String> commandList = new ArrayList<>(100);

}
