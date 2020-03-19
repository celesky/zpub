package com.celesky.zpub.config;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @desc:
 * @author: panqiong
 * @date: 2019-09-16
 */
@Component
@Slf4j
public class ServicesConfigRefresh {

    @Resource
    RefreshScope refreshScope;

    @Resource
    ServicesConfig servicesConfig;

    @ApolloConfigChangeListener(value = "services-config.yml",interestedKeyPrefixes = {"services-config"})
    public void onChange(ConfigChangeEvent changeEvent){

        refreshScope.refresh("servicesConfig");

        List<ServicesConfig.Service> list = servicesConfig.getServiceList();

        list.stream().forEach(System.out::println);
    }
}
