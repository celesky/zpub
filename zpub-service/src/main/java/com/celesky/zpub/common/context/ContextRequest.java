package com.celesky.zpub.common.context;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author:pan
 * @date:2018-08-09
 */
@Data
public class ContextRequest {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private String requestId;
    private String fullRequestUrl;
}
