package com.celesky.zpub.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @desc:
 * @author: panqiong
 * @date: 2018/10/27
 */
@Component
@Slf4j
public class SpringBeanFactory implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    /**
     * 获取某个Bean的对象
     */
    public static <T> T getBean(Class<T> requiredType) {
        try {
            return applicationContext.getBean(requiredType);
        } catch (Exception e) {
            log.error("Spring getBean:" + requiredType, e.getMessage(),e);
        }
        return null;
    }

    /**
     * 获取某个Bean的对象
     */
    public static Object getBean(String beanName) {
        try {
            return applicationContext.getBean(beanName);
        } catch (Exception e) {
            log.error("Spring getBean:"+beanName, e.getMessage(),e);
        }
        return null;
    }


    /**
     * 获取某个Bean的对象
     */
    public static<T> T getBean(Class<T> requiredType, Object... args) throws BeansException{
        try {
            return applicationContext.getBean(requiredType,args);
        } catch (Exception e) {
            log.error("Spring getBean:" + requiredType, e.getMessage(),e);
        }
        return null;
    }

    /**
     * 获取接口bean的所有实例对象
     */
    public static<T> Map<String, T> getBeansOfType(Class<T> requiredType) throws BeansException{
        try {
            return applicationContext.getBeansOfType(requiredType);
        } catch (Exception e) {
            log.error("Spring getBeansOfType:" + requiredType, e.getMessage(),e);
        }
        return null;
    }



}
