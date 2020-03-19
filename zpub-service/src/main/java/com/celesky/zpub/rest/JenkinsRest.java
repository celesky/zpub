package com.celesky.zpub.rest;

import com.zuzuche.msa.base.resp.RespResult;
import com.celesky.zpub.config.ServicesConfig;
import com.celesky.zpub.jenkins.JenkinsBuildStatus;
import com.celesky.zpub.service.publish.JenkinsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @desc:
 * @author: panqiong
 * @date: 2019-05-05
 */
@RestController
@RequestMapping("/jenkins")
@Slf4j
@Api(value = "jenkins回调", description = "jenkins回调", tags = {"jenkins回调"})
public class JenkinsRest {

    @Autowired
    JenkinsService jenkinsService;

    @Autowired
    ServicesConfig config;

    @RequestMapping(value = "/getBuildStatus" , method = RequestMethod.GET)
    @ApiOperation(value = "获取构建信息", notes = "获取构建信息")
    public RespResult<JenkinsBuildStatus> getBuildStatus(String serviceName){
        List<ServicesConfig.Replica> list = config.getReplicaList(serviceName);
        list.stream().forEach(System.out::println);


        JenkinsBuildStatus status = jenkinsService.getJenkinLastBuildStatus(serviceName);
        return RespResult.success(status);
    }





}
