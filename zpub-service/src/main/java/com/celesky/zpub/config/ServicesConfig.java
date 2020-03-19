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
@Component("servicesConfig")
@RefreshScope
@ConfigurationProperties(prefix = "services-config")
@Data
public class ServicesConfig {

    /**
     * 服务列表
     */
    private List<Service> serviceList = new ArrayList<>(100);





    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Service {
        /**
         * 服务名
         */
        private String serviceName;
        /**
         * 反注册地址
         */
        @Builder.Default
        private String unregisterUri="/actuator/unregister";

        /**
         * 自动测试的地址
         */
        private String autoTestUri="/actuator/autoTest";

        /**
         * 副本列表
         */
        private List<Replica> replicaList;

        /**
         * docker运行参数
         */
        private DockerRunOpt dockerRunOpt;
    }



    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Replica {
        /**
         * 分配给副本实例的server Ip
         */
        private String ip;

        /**
         * 分配给副本实例的http端口
         */
        private int httpPort;

        /**
         * 副本级别的docker启动配置,有时候需要针对不同的机器做不同的配置
         */
        private List<String> ultraOpts;


        public String getHttpUri(){
            return ip+":"+httpPort;
        }

        @Override
        public String toString() {
            return "{" +
                    "ip='" + ip + '\'' +
                    ", httpPort=" + httpPort +
                    '}';
        }
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DockerRunOpt {
        private String name;
        private String volume;
        private String network;
        private String env;
        private List<String> ultraOpts;
    }





    /**
     * 根据服务名称后去发布主机列表
     * @param serviceName
     * @return
     */
    public List<Replica> getReplicaList(String serviceName){
         Optional<Service> optional = serviceList.stream()
                .filter(e->serviceName.equals(e.getServiceName()))
                .findFirst();
         if(optional.isPresent()){
             return optional.get().getReplicaList();
         }else{
             return null;
         }
    }

    /**
     * 根据服务名称后去发布主机列表
     * @param serviceName
     * @return
     */
    public Service getServiceInfo(String serviceName){
        Optional<Service> optional = serviceList.stream()
                .filter(e->serviceName.equals(e.getServiceName()))
                .findFirst();
        if(optional.isPresent()){
            return optional.get();
        }else{
            return null;
        }
    }


    /**
     * 根据服务名称后去发布主机列表
     * @param serviceName
     * @return
     */
    public DockerRunOpt getServiceRunCmd(String serviceName){
        Optional<Service> optional = serviceList.stream()
                .filter(e->serviceName.equals(e.getServiceName()))
                .findFirst();
        if(optional.isPresent()){
            return optional.get().getDockerRunOpt();
        }else{
            return null;
        }
    }


}
