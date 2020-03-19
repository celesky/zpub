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
public class ServicesConfigQaRefresh {

    @Resource
    RefreshScope refreshScope;

    @Resource
    ServicesConfigQa servicesConfigQa;

    @ApolloConfigChangeListener(value = "services-config-qa.yml",interestedKeyPrefixes = {"services-config-qa"})
    public void onChange(ConfigChangeEvent changeEvent){

        refreshScope.refresh("servicesConfigQa");

        List<ServicesConfigQa.Service> list = servicesConfigQa.getServiceList();

        list.stream().forEach(System.out::println);
    }
}
