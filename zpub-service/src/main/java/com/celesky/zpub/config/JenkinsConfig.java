package com.celesky.zpub.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @desc:
 * @author: panqiong
 * @date: 2019-12-11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component("jenkinsConfig")
public class JenkinsConfig {

    private  String jenkinsUrl = "http://jks-repositories.zuzuche.com/view/%E5%AE%A2%E6%9C%8D%E6%8A%80%E6%9C%AF%E6%B5%8B%E8%AF%95";

    //private  String templateUrl = "http://jks-repositories.zuzuche.com/view/客服技术测试/job/{service_name}/buildWithParameters?token=don't_modify_this&branch={branch}&cause={cause}";

    private  String templateBuildUrl = "http://jks-repositories.zuzuche.com/view/客服技术测试/job/{service_name}-docker/buildWithParameters?token=don't_modify_this&branch={branch}&imageName={imageName}&cause={cause}";

    //curl http://jks-repositories.zuzuche.com/job/stream-service/lastBuild/api/xml --user panqiong:289289
    private  String buildStatusUrl = "http://jks-repositories.zuzuche.com/job/{service_name}-docker/lastBuild/api/xml";


    private  String jobUrl = "http://jks-repositories.zuzuche.com/view/%E5%AE%A2%E6%9C%8D%E6%8A%80%E6%9C%AF%E6%B5%8B%E8%AF%95/job/{service_name}-docker";


    private  String harborBaseName ="harbor-release.zuzuche.net/zzc/{service_name}:{label}";


    private String qaServerIp = "192.168.100.96";
}
