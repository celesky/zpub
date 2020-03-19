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
 * @desc: 服务配置
 * @author: panqiong
 * @date: 2019-09-12
 */
@Component("serverNodeConfig")
@RefreshScope
@ConfigurationProperties(prefix = "server-node-config")
@Data
public class ServerNodeConfig {

    /**
     * 服务列表
     */
    private List<Node> nodeList = new ArrayList<>(100);





    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Node {
        /**
         * 分配给副本实例的server Ip
         */
        private String ip;

        /**
         * 分配给副本实例的http端口
         */
        private int sshPort;

        /**
         * authType: pwd,pubkey
         */
        private String authType;

        /**
         * 路径
         */
        private String keyPath;

        /**
         * 账号
         */
        private String account;

        /**
         * 密码
         */
        private String password;



    }




    /**
     * 根据ip 获取服务器对象
     * @param ip
     * @return
     */
    public Node getServerNode(String ip){
         Optional<Node> optional = nodeList.stream()
                .filter(e->ip.equals(e.getIp()))
                .findFirst();
         if(optional.isPresent()){
             return optional.get();
         }else{
             return null;
         }
    }


}
