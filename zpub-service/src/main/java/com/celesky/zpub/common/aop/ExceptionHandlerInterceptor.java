package com.celesky.zpub.common.aop;

import com.celesky.zpub.common.context.ContextThread;
import com.celesky.zpub.common.utils.DataUtil;
import com.zuzuche.msa.base.resp.RespResult;
import com.zuzuche.msa.base.resp.StatusServiceException;
import com.zuzuche.msa.base.util.JsonUtil;
import com.zuzuche.msa.base.util.StringUtil;
import com.zuzuche.msa.log.constants.MdcConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @desc SpringMVC aop ，异常处理
 * @author pan
 * @date 2018-09-25
 */
@Slf4j
public class ExceptionHandlerInterceptor extends HandlerInterceptorAdapter {
    /**
     * 运行环境
     */
    @Value("${env:not_configured}")
    private String env;

    /**
     * 服务名
     */
    @Value("${spring.application.name:not_configured}")
    private String project;

    /**
     * 实例id  命名规则为ip:port
     */
    @Value("${eureka.instance.instance-id:not_configured}")
    private String instanceId;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {


            String requestId = request.getParameter("_requestId");
            String fullRequestUrl = DataUtil.getRequstAddress(request);
            ContextThread.setRequestId(requestId);
            ContextThread.setResponse(response);
            ContextThread.setRequest(request);
            ContextThread.setFullRequestUrl(fullRequestUrl);
            String agentId = request.getParameter("_agentId");
            if(StringUtil.isNotEmpty(agentId) ) {
                MDC.put(MdcConstant.AGENT_ID, "_" + agentId);
            }
            MDC.put(MdcConstant.REQUEST_ID, requestId);
            MDC.put(MdcConstant.URI,request.getRequestURI().toString());

            // ip地址
            //// MDC.put("hostIp", NetUtils.getLocalAddress()==null?null:NetUtils.getLocalAddress().getHostAddress());
            MDC.put(MdcConstant.HOST_IP,instanceId);
            MDC.put(MdcConstant.ENV,env);
            MDC.put(MdcConstant.PROJECT,project);

            log.info("Request Begin, 协议：" + request.getScheme() + ",Controller is：" + getHandleMethod(handler) + ", Request Method：" + request.getMethod() + ", URI:" + fullRequestUrl);
        } catch (Exception e) {
            //ignore error
            log.warn("HandlerInterceptor preHandler 出现了一些异常",e,e.getMessage());
        }
        return true;
    }

    private String getHandleMethod(Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            return handlerMethod.getMethod().getDeclaringClass() + "." + handlerMethod.getMethod().getName() + "()";
        }

        return handler.getClass().getName();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        ContextThread.destoryContextThread();
        LocaleContextHolder.resetLocaleContext();
        MDC.clear();
        if (ex != null) {
            log.error("Request Begin, 协议：" + request.getScheme() + ",Controller is：" + handler.getClass().getName() + ", Request Method：" + request.getMethod() + ", URI:" + DataUtil.getRequstAddress(request));
            if (ex instanceof StatusServiceException) {
                log.info("statusException:{}", ex.getMessage());
                StatusServiceException exception = (StatusServiceException) ex;
                PrintWriter pw = null;
                try {
                    response.setCharacterEncoding("utf-8");

                    pw = response.getWriter();
                    response.reset();
                    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    pw.println(JsonUtil.objToStr(RespResult.error(exception.getCode(), exception.getMessage())));
                    pw.flush();
                } catch (IOException ioException) {
                    log.error("print response data error:" + ioException);
                } finally {
                    IOUtils.closeQuietly(pw);
                }
            } else {
                log.error("exception:{}-{}", ex.getMessage(), ex);
                PrintWriter pw = null;
                try {
                    //response.getWriter()之前设置utf-8
                    response.setCharacterEncoding("utf-8");

                    pw = response.getWriter();
                    response.reset();
                    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                    pw.println(JsonUtil.objToStr(RespResult.error("系统异常")));
                    pw.flush();
                } catch (IOException ioException) {
                    log.error("print response data error:" + ioException);
                } finally {
                    IOUtils.closeQuietly(pw);
                }
            }
        }


    }

}
