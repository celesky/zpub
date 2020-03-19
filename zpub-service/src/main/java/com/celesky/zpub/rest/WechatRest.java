package com.celesky.zpub.rest;

import com.celesky.zpub.rest.req.WechatInboundReq;
import com.celesky.zpub.rest.req.WechatValidationReq;
import com.celesky.zpub.service.CommandService;
import com.celesky.zpub.wechatsdk.WechatInboundMsg;
import com.celesky.zpub.service.WechatService;
import com.celesky.zpub.service.publish.JenkinsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @desc: 企业微信
 * @author: panqiong
 * @date: 2019-05-04
 */
@RestController
@RequestMapping("/expose/wechat")
@Slf4j
@Api(value = "企业微信相关接口", description = "企业微信", tags = {"wechat"})
public class WechatRest {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    WechatService wechatService;

    @Autowired
    JenkinsService jenkinsService;


    @Autowired
    CommandService commandService;

    @RequestMapping(value = "/validation" , method = RequestMethod.GET)
    @ApiOperation(value = "企业微信验证", notes = "企业微信验证")
    public void validation(HttpServletResponse response, WechatValidationReq validationReq){
        log.info(validationReq.toString());
        String result = wechatService.validation(validationReq);
        try {
            response.getWriter().print(result);
        } catch (IOException e) {
            log.error("validation 写response失败",e.getMessage(),e);
        }
    }

    /**
     * 这里url也叫做validation 实际上是上行信息
     * post是上行信息
     * get 是验证url
     *
     * @param inboundReq
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/validation" , method = RequestMethod.POST)
    @ApiOperation(value = "上行信息", notes = "上行信息")
    public String inboundMsg(WechatInboundReq inboundReq) throws Exception{
        log.info("上行企业微信信息:"+ inboundReq.toString());
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String str = "";
        StringBuilder wholeStr = new StringBuilder();
        //一行一行的读取body体里面的内容；
        while((str = reader.readLine()) != null){
            wholeStr.append(str);
        }
        wechatService.parseInbound(inboundReq,wholeStr.toString());
        return "";
    }


    /**
     * 这里url也叫做validation 实际上是上行信息
     * post是上行信息
     * get 是验证url
     *
     * @param inboundReq
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/mockMsg" , method = RequestMethod.POST)
    @ApiOperation(value = "模拟上行信息", notes = "模拟上行信息")
    public String mockMsg(WechatInboundMsg inboundReq) throws Exception{
        log.info("模拟上行企业微信信息:"+ inboundReq.toString());
        commandService.dealCommand(inboundReq);
        return "";
    }





}
