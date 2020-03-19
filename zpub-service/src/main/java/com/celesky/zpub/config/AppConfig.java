package com.celesky.zpub.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @desc:
 * @author: panqiong
 * @date: 2019-12-09
 */
public class AppConfig {

    /**
     * 应用列表
     */
    private List<Appcation> appList = new ArrayList<>(100);


    /**
     * 应用实体
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Appcation {
        /**
         * 应用服务名称
         */
        private String appName;
        /**
         * 服务运行的http端口
         */
        private String httpPort;

        /**
         * 副本列表
         */
        private List<Replica> replicaList;
    }



    /**
     * 应用实体
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Replica {
        /**
         * 副本所属 应用名称
         */
        private String appName;
        /**
         * 分配给副本的服务器ip
         */
        private String ip;

        /**
         * 分配给副本的服务器ssh端口
         */
        private int sshPort;
    }


}
