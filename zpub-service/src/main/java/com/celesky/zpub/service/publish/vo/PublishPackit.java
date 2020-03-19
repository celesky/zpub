package com.celesky.zpub.service.publish.vo;

import com.celesky.zpub.config.ServicesConfig;
import com.celesky.zpub.service.publish.stage.AbstractStaging;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * @desc: 发布参数包,包含发布所需的所有参数信息
 * @author: panqiong
 * @date: 2019-12-06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublishPackit {
    /**
     * 本次发布的记录id
     */
    Integer logId;
    /**
     * 服务名称
     */
    String serviceName;

    /**
     * 镜像名称
     */
    String imageName;


    /**
     * 反注册 流量下线地址
     */
    String unregisterUri;

    /**
     * 自动测试的地址
     */
    String autoTestUri;

    /**
     * 目标部署机副本
     */
    ServicesConfig.Replica replica;


    /**
     * 启动命令参数组
     */
    ServicesConfig.DockerRunOpt dockerRunOpt;


    /**
     * 发布步骤
     */
    List<AbstractStaging> stagingList;

}
