package com.celesky.zpub.remote;

import com.zuzuche.msa.base.resp.Status;
import com.zuzuche.msa.base.resp.StatusServiceCnException;
import com.zuzuche.msa.base.util.JsonUtil;
import com.zuzuche.msa.base.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @desc: 抽象http调用父类
 * 用feign的话解析麻烦,因为这些外部接口协议各不相同.
 * feign是声明似的 ,如果配置化的url不方便
 * @author: panqiong
 * @date: 2018/11/5
 */
@Slf4j
public abstract class AbstractHttpInvoke {


    @Autowired
    private RestTemplate restTemplate;


    /**
     * 默认UTF-8
     * 如果需要用json方式提交
     * 封装http header
     *
     * @return
     */
    protected HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAcceptCharset(Arrays.asList(StandardCharsets.UTF_8));
        return headers;
    }

    /**
     * 转换参数
     * @param postSmsParam
     */
    protected MultiValueMap<String, String> paramTransform(Object postSmsParam) {
        MultiValueMap<String, String> bodyParam = new LinkedMultiValueMap<>();
        String origin = JsonUtil.objToStr(postSmsParam);
        Map<String,String> raw = JsonUtil.stringToObj(origin,LinkedMultiValueMap.class);
        raw.forEach((k,v)->{
            bodyParam.add(k,v);
        });
        return bodyParam;
    }


    protected String postForm(String url, Object param){
        String result = null;
        try {
            /// log.info("[request]url:"+url+" param:"+param.toString());
            MultiValueMap<String, String> bodyParam = paramTransform(param);

            HttpHeaders headers = getHttpHeaders();
            //给接口给子类留填充headers
            setHeader(headers);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(bodyParam, headers);
            ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class);
            result = response.getBody();
            if(log.isDebugEnabled()){
                log.debug("[response] \n->[url]:"+url+"\n->[param]"+param.toString()+"\n->[result]:"+result);
            }

        }catch (ResourceAccessException e){
            log.error("[AbstractHttpInvoke-postForm]调用api资源报错 ",e.getCause(),e);
        }catch (Exception e){
            log.error("[AbstractHttpInvoke-postForm]调用api报错 ",e.getMessage(),e);
        }
        return result==null?"":result;
    }

    /**
     * json原文发送
     * @param url
     * @param param
     * @return
     */
    protected String postJson(String url,Object param){
        String result = null;
        if(param==null){
            param = new HashMap();
        }
        log.info("[request]url:"+url+" param:"+param.toString());
        HttpHeaders headers = getHttpHeaders();
        //给接口给子类留填充headers
        setHeader(headers);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(JsonUtil.objToStr(param), headers);
        ResponseEntity<String> response = restTemplate.postForEntity( url, request , String.class);
        result = response.getBody();
        if(log.isDebugEnabled()){
            log.debug("[response] \n->[url]:"+url+"\n->[param]"+param.toString()+"\n->[result]:"+result);
        }
        if(StringUtil.isEmpty(result)){
            throw new StatusServiceCnException(Status.OBJECT_NOT_EXIST,"接口返回result为空");
        }
        return result==null?"":result;
    }

    /**
     * json原文发送
     * @param url
     * @return
     */
    protected String getJson(String url){
        String result = null;
        log.info("[request]url:"+url);
        HttpHeaders headers = getHttpHeaders();
        //给接口给子类留填充headers
        setHeader(headers);
        headers.setContentType(MediaType.APPLICATION_JSON);
        //HttpEntity<String> request = new HttpEntity<>(JsonUtil.objToStr(param), headers);
        ResponseEntity<String> response = restTemplate.getForEntity(url,String.class);
        result = response.getBody();
        if(log.isDebugEnabled()){
            log.debug("[response] \n->[url]:"+url+"\n->[param] \n->[result]:"+result);
        }
        if(StringUtil.isEmpty(result)){
            throw new StatusServiceCnException(Status.OBJECT_NOT_EXIST,"接口返回result为空");
        }

        return result==null?"":result;
    }

    /**
     * 基本验证
     * @param url
     * @return
     */
    protected String getWithAuth(String url){
        String user = "panqiong";
        String password = "289289";
        String userMsg = user + ":" + password;
        String base64UserMsg = Base64.getEncoder().encodeToString(userMsg.getBytes());
        log.info(base64UserMsg);
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Agent", "curl/7.58.0");
        headers.add("Authorization ", base64UserMsg);
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        ResponseEntity<String> response = restTemplate.getForEntity(url,String.class);

        String result = response.getBody();
        if(log.isDebugEnabled()){
            log.debug("[response] \n->[url]:"+url+"\n->[param] \n->[result]:"+result);
        }
        if(StringUtil.isEmpty(result)){
            throw new StatusServiceCnException(Status.OBJECT_NOT_EXIST,"接口返回result为空");
        }
        return result;
    }


    /**
     * 额外的header设置 比如编码
     * @param header
     */
    protected abstract void setHeader(HttpHeaders header);


    public static void main(String[] args) {
        Map map = new HashMap();
        map.put("11",22);
        map.put("22",22);
        MultiValueMap<String, String> bodyParam = new LinkedMultiValueMap<>();
        String origin = JsonUtil.objToStr(map);
        Map<String,String> raw = JsonUtil.stringToObj(origin,LinkedMultiValueMap.class);
        raw.forEach((k,v)->{
            bodyParam.add(k,v);
        });

    }
}
