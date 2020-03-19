package com.celesky.zpub.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @desc:
 * @author: panqiong
 * @date: 2018/11/5
 */
@Configuration
public class RestTemplateConfig {

    /**
     * 构建resttemplate
     * @param factory
     * @return
     */
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory){
        RestTemplate restTemplate =  new RestTemplate(factory);
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        // 修改StringHttpMessageConverter的默认编码为UTF-8
        converters.stream().forEach(converter->{
            if(converter instanceof StringHttpMessageConverter){
                ((StringHttpMessageConverter)converter).setDefaultCharset(StandardCharsets.UTF_8);
            }
        });
        return restTemplate;
    }

    /**
     * resttemplate默认30秒
     * @return
     */
    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory(){
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(30000);
        factory.setConnectTimeout(30000);
        return factory;
    }


}
