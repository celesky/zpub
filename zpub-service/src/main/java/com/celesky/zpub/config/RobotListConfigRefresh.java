package com.celesky.zpub.config;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @desc:
 * @author: panqiong
 * @date: 2019-11-15
 */
@Component
@Slf4j
public class RobotListConfigRefresh {

    @Resource
    org.springframework.cloud.context.scope.refresh.RefreshScope refreshScope;

    @Resource
    RobotListConfig robotListConfig;

    @ApolloConfigChangeListener(value = "application.yml",interestedKeyPrefixes = {"robot-list"})
    public void onChange(ConfigChangeEvent changeEvent){

        refreshScope.refresh("robotListConfig");

        List<RobotListConfig.Robot> list = robotListConfig.getRobots();

        list.stream().forEach(System.out::println);
    }

}
