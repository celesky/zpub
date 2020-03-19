package com.celesky.zpub.config;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;

import javax.annotation.Resource;

/**
 * @desc: 发布阶段的命令行
 * @author: panqiong
 * @date: 2019-12-09
 */
//@Component
//@Slf4j
public class PublishStageCmdConfigRefresh {

    @Resource
    org.springframework.cloud.context.scope.refresh.RefreshScope refreshScope;

    @Resource
    PublishStageCmdConfig publishStageCmdConfig;

    @ApolloConfigChangeListener(value = "publish-stage-cmd-list.yml",interestedKeyPrefixes = {"publish-stage-cmd-list"})
    public void onChange(ConfigChangeEvent changeEvent){

//        refreshScope.refresh("publishStageCmdConfig");
//
//        List<String> list = publishStageCmdConfig.getCommandList();
//
//        list.stream().forEach(System.out::println);
    }
}
