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
@Component("servicesConfigQa")
@RefreshScope
@ConfigurationProperties(prefix = "services-config-qa")
@Data
public class ServicesConfigQa {

    /**
     * 服务列表
     */
    private List<Service> serviceList = new ArrayList<>(100);





    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Service {
        private String serviceName;
        private List<Replica> replicaList;
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
