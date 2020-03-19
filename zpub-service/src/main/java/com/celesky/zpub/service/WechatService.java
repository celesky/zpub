package com.celesky.zpub.service;

import com.celesky.zpub.config.JenkinsConfig;
import com.celesky.zpub.entity.WechatMessageEntity;
import com.celesky.zpub.mapper.WechatMessageMapper;
import com.celesky.zpub.rest.req.WechatInboundReq;
import com.celesky.zpub.rest.req.WechatValidationReq;
import com.celesky.zpub.wechatsdk.AesException;
import com.celesky.zpub.wechatsdk.WXBizMsgCrypt;
import com.celesky.zpub.wechatsdk.WechatInboundMsg;
import com.zuzuche.msa.base.util.BeanConverter;
import com.zuzuche.msa.base.util.StringUtil;
import com.celesky.zpub.common.constant.WechatAppEnums;
import com.celesky.zpub.common.utils.XmlParserUtil;
import com.celesky.zpub.service.publish.JenkinsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @desc:
 * @author: panqiong
 * @date: 2019-05-04
 */
@Service
@Slf4j
public class WechatService implements InitializingBean {


    @Value("${wechat.corpId}")
    private String corpId;

    @Value("${wechat.assistToken}")
    private String assistToken;

    @Value("${wechat.assistEncodingAESKey}")
    private String assistEncodingAESKey;


    @Value("${wechat.productAppId}")
    private String productAppId;

    WXBizMsgCrypt wxcpt = null;


    @Autowired
    JenkinsConfig jenkinsConfig;

    @Autowired
    WechatMessageService messageService;


    @Autowired
    JenkinsService jenkinsService;


    @Autowired
    CommandService commandService;

    @Autowired
    WechatMessageMapper wechatMessageMapper;






    @Override
    public void afterPropertiesSet() throws Exception {
        wxcpt = new WXBizMsgCrypt(assistToken, assistEncodingAESKey, corpId);
    }


    /**
     * 企业微信验证
     * @param req
     * @return
     */
    public String validation(WechatValidationReq req){



        // String sVerifyMsgSig = HttpUtils.ParseUrl("msg_signature");
        String sVerifyMsgSig = req.getMsg_signature();
        // String sVerifyTimeStamp = HttpUtils.ParseUrl("timestamp");
        String sVerifyTimeStamp = req.getTimestamp();
        // String sVerifyNonce = HttpUtils.ParseUrl("nonce");
        String sVerifyNonce = req.getNonce();
        // String sVerifyEchoStr = HttpUtils.ParseUrl("echostr");
        String sVerifyEchoStr = req.getEchostr();

        try {

            String sEchoStr = wxcpt.VerifyURL(sVerifyMsgSig, sVerifyTimeStamp,
                    sVerifyNonce, sVerifyEchoStr);
            log.info("verifyurl echostr: " + sEchoStr);
            // 验证URL成功，将sEchoStr返回
            // HttpUtils.SetResponse(sEchoStr);
            return sEchoStr;
        } catch (AesException e) {
            log.error("AesException异常:",e.getCode());
        }

        return null;
    }

    /**
     * 上行信息解析
     * @param req
     * @return
     */
    public void parseInbound(WechatInboundReq req, String sReqData){
        // String sReqMsgSig = HttpUtils.ParseUrl("msg_signature");
        String sReqMsgSig = req.getMsg_signature();
        // String sReqTimeStamp = HttpUtils.ParseUrl("timestamp");
        String sReqTimeStamp = req.getTimestamp();
        // String sReqNonce = HttpUtils.ParseUrl("nonce");
        String sReqNonce = req.getNonce();
        // post请求的密文数据
        // sReqData = HttpUtils.PostData();
        //String sReqData = "<xml><ToUserName><![CDATA[wx5823bf96d3bd56c7]]></ToUserName><Encrypt><![CDATA[RypEvHKD8QQKFhvQ6QleEB4J58tiPdvo+rtK1I9qca6aM/wvqnLSV5zEPeusUiX5L5X/0lWfrf0QADHHhGd3QczcdCUpj911L3vg3W/sYYvuJTs3TUUkSUXxaccAS0qhxchrRYt66wiSpGLYL42aM6A8dTT+6k4aSknmPj48kzJs8qLjvd4Xgpue06DOdnLxAUHzM6+kDZ+HMZfJYuR+LtwGc2hgf5gsijff0ekUNXZiqATP7PF5mZxZ3Izoun1s4zG4LUMnvw2r+KqCKIw+3IQH03v+BCA9nMELNqbSf6tiWSrXJB3LAVGUcallcrw8V2t9EL4EhzJWrQUax5wLVMNS0+rUPA3k22Ncx4XXZS9o0MBH27Bo6BpNelZpS+/uh9KsNlY6bHCmJU9p8g7m3fVKn28H3KDYA5Pl/T8Z1ptDAVe0lXdQ2YoyyH2uyPIGHBZZIs2pDBS8R07+qN+E7Q==]]></Encrypt><AgentID><![CDATA[218]]></AgentID></xml>";

        WechatInboundMsg inboundMsg = null;
        try {
            String xmlMsg = wxcpt.DecryptMsg(sReqMsgSig, sReqTimeStamp, sReqNonce, sReqData);
            log.info("after decrypt msg: " + xmlMsg);
            // 解析出明文xml标签的内容进行处理
            inboundMsg = XmlParserUtil.parseInboundMessage(xmlMsg);
            log.info("[微信消息]"+inboundMsg.toString());

        } catch (Exception e) {
            // 解密失败，失败原因请查看异常
            log.error("解析失败,inbound消息解析失败",e.getCause(),e);
            return;
        }

        if(inboundMsg==null){
            log.error("inboundMsg为空或者inboundMsg.getContent为空");
            return;
        }
        try{
            WechatMessageEntity entity = BeanConverter.copy(inboundMsg,WechatMessageEntity.class);
            entity.setCtime(LocalDateTime.now());
            wechatMessageMapper.insert(entity);
        }catch (Exception e){
            log.info("[保存消息异常]",e.getMessage(),e);
        }

        if(StringUtil.isEmpty(inboundMsg.getContent())){
            return ;
        }

        // 如果是从产品助理app发过来的消息
        log.info(inboundMsg.getAgentId()+":"+productAppId );

        if(inboundMsg.getAgentId().equals(productAppId)){
            messageService.sendWechatMsg(inboundMsg.getFromUserName(),"感谢您的反馈，我们会及时跟进处理~处理进度您可以在wiki中查看：http://wiki.tools.zuzuche.com/pages/viewpage.action?pageId=48202614", WechatAppEnums.product_robot);
        }else{
            // 从发布助手发过来的消息 解析指令
            commandService.dealCommand(inboundMsg);
        }

    }







}
