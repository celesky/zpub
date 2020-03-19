package com.celesky.zpub.service.publish.vo;

import com.google.common.base.Splitter;
import com.zuzuche.msa.base.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @desc: docker ps 命令输出结果模型
 * @author: panqiong
 * @date: 2019-12-16
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Slf4j
public class DockerPsRet {
    private String containerId;
    private String image;
    private String command;
    private String created;
    private String status;
    private String ports;
    private String names;

    public static DockerPsRet from(String ret){
        if(StringUtil.isEmpty(ret)){
            return null;
        }
        ret = ret.replaceAll("\\\\s+","_");
        List<String> attrList = Splitter.on("_").splitToList(ret);
        if(attrList.size()<7){
            log.error("[DockerPsRet]命令结果转换对象时,输出结果的列数量异常,预计7列,实际只有"+attrList.size()+"列");
            return null;
        }

        DockerPsRet psRet = trans(attrList);
        return psRet;
    }



    public static List<DockerPsRet> from(List<String> retList){
        if(retList==null || retList.size()==0){
            return null;
        }
        List<DockerPsRet> psRetList = new ArrayList<>();
        for(String ret : retList){
            ret = ret.replaceAll("\\\\s+","_");
            List<String> attrList = Splitter.on("_").splitToList(ret);
            if(attrList.size()<7){
                log.error("[DockerPsRet]命令结果转换对象时,输出结果的列数量异常,预计7列,实际只有"+attrList.size()+"列");
                return null;
            }
            DockerPsRet psRet = trans(attrList);
            psRetList.add(psRet);
        }

        return psRetList;
    }



    private static DockerPsRet trans(List<String> attrList) {
        return DockerPsRet.builder()
                .containerId(attrList.get(0))
                .image(attrList.get(1))
                .command(attrList.get(2))
                .created(attrList.get(3))
                .status(attrList.get(4))
                .ports(attrList.get(5))
                .names(attrList.get(6))
                .build();
    }

    public static void main(String[] args) {
        String s = "9f1c14495071        harbor-release.zuzuche.net/zzc/sentry-service:docker_20191212_100627        \"/sbin/tini -- /bi...\"   14 hours ago        Up 14 hours                             sentry-service";

        String ret = s.replaceAll("\\s{2,}","#");
        System.out.println("ret = " + ret);
    }

}
