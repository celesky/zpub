package com.celesky.zpub.remote;

import com.celesky.zpub.common.utils.XmlParserUtil;
import com.celesky.zpub.jenkins.JenkinsBuildStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;


/**
 * @desc:
 * @author: panqiong
 * @date: 2019-05-04
 */
@Component
@Slf4j
public class JenkinsRemote extends AbstractHttpInvoke{
    /**
     * 额外的header设置 比如编码
     *
     * @param header
     */
    @Override
    protected void setHeader(HttpHeaders header) {

    }

    /**
     * 调用jenkins发布url
     * @param url
     */
    public void invokeBuild(String url){
        String result = super.getJson(url);
        log.info(result);
    }


    /**
     * 获取最近一次构建信息
     * @param url
     */
    public JenkinsBuildStatus getLastBuildInfo(String url){
        String result = super.getWithAuth(url);
        log.info(result);
        // 解析xml
        try {
            JenkinsBuildStatus status = XmlParserUtil.parseJenkinsBuildInfo(result);
            return status;
        } catch (Exception e) {
            log.error("getLastBuildInfo 解析xml报错",e.getMessage(),e);
        }
        return null;

    }


}
