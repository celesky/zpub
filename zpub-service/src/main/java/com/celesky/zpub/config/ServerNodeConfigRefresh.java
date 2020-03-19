package com.celesky.zpub.config;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @desc: 服务配置
 * @author: panqiong
 * @date: 2019-09-12
 */
@Component
@Slf4j
public class ServerNodeConfigRefresh {

    @Resource
    org.springframework.cloud.context.scope.refresh.RefreshScope refreshScope;

    @Resource
    ServicesConfig servicesConfig;

    @ApolloConfigChangeListener(value = "server-node-config.yml",interestedKeyPrefixes = {"server-node-config"})
    public void onChange(ConfigChangeEvent changeEvent){

        refreshScope.refresh("serverNodeConfig");

        List<ServicesConfig.Service> list = servicesConfig.getServiceList();

        list.stream().forEach(System.out::println);
    }

}
