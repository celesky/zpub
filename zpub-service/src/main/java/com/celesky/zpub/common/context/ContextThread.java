package com.celesky.zpub.common.context;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 线程固化参数
 *
 * @author lizhifeng
 */
public class ContextThread {

    private static ThreadLocal<ContextRequest> threadLocal = new ThreadLocal<ContextRequest>();


    public static void destoryContextThread() {
        threadLocal.remove();
    }

    public static String getRequestId() {

        return threadLocal.get().getRequestId();
    }

    public static void setRequestId(String requestId) {
        ContextRequest request = getContextRequest();
        request.setRequestId(requestId);
        threadLocal.set(request);
    }

    public static String getFullRequestUrl() {

        return threadLocal.get().getFullRequestUrl();
    }


    public static void setFullRequestUrl(String fullRequestUrl) {
        ContextRequest request = getContextRequest();
        request.setFullRequestUrl(fullRequestUrl);
        threadLocal.set(request);
    }


    public static ContextRequest getContextRequest() {
        ContextRequest request = threadLocal.get();
        if (request == null) {
            return new ContextRequest();
        }
        return request;
    }

    public static HttpServletRequest getRequest() {

        return getContextRequest().getRequest();
    }

    public static void setRequest(HttpServletRequest request) {
        ContextRequest contextRequest = getContextRequest();
        contextRequest.setRequest(request);
        threadLocal.set(contextRequest);
    }


    public static HttpServletResponse getResponse() {

        return getContextRequest().getResponse();
    }

    public static void setResponse(HttpServletResponse response) {
        ContextRequest request = getContextRequest();
        request.setResponse(response);
        threadLocal.set(request);
    }
}
