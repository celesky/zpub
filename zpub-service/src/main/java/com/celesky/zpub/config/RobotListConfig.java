package com.celesky.zpub.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @desc:
 * @author: panqiong
 * @date: 2019-11-15
 */
@Component("robotListConfig")
@RefreshScope
@ConfigurationProperties(prefix = "robot-list")
@Data
public class RobotListConfig {
    /**
     * 配置项列表
     */
    private List<Robot> robots = new ArrayList<>(100);

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Robot{
        private String name;
        private String url;
        private String desc;
    }


    public String getRobotUrl(String name){
        Optional<Robot> optional = robots.stream().filter(e->name.equals(e.getName())).findAny();
        if(optional.isPresent()){
            return optional.get().url;
        }
        return "";
    }

}
