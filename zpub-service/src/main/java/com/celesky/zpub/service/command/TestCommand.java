package com.celesky.zpub.service.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @desc: 拨测指令
 * test sms 这是一条测试短信 self
 * @author: panqiong
 * @date: 2019-09-24
 */
@Slf4j
@Component("test")
public class TestCommand  {

    private static AtomicLong atomicLong = new AtomicLong(0);

//    @Autowired
//    SmsRemote smsRemote;
//
//
//
//    @Autowired
//    EmailBusiService emailBusiService;
//
//    @Override
//    public void execute(String userName, List<String> paramList) {
//        if(paramList.size()<2){
//            messageService.sendWechatMsg(userName,"缺少参数,如测试短信,正确格式: test sms 这是一条测试短信", WechatAppEnums.assist);
//            return;
//        }
//
//        String type = paramList.get(1);
//        String content = "测试内容:"+atomicLong.getAndIncrement();
//        // self:从服务自身发, zcs:从zcs网关发 , api: 从api网关发
//        String channel = "self";
//        if(paramList.size()>=3){
//            content = paramList.get(2);
//        }
//
//        if(paramList.size()>=4){
//            channel = paramList.get(3);
//        }
//
//        // 短信测试
//        if("sms".equals(type.toLowerCase())){
//            smsTest(userName,content,channel);
//        }else if ("email".equals(type.toLowerCase())){
//            emailTest(userName,content,channel);
//        }else{
//            messageService.sendWechatMsg(userName,"未配置测试模块", WechatAppEnums.assist);
//        }
//    }
//
//    /**
//     * 短信业务测试
//     * @param userName 测试人
//     * @param content 测试短信内容
//     * @param channel 测试短信渠道
//     * @return
//     */
//    private void smsTest(String userName,String  content, String channel){
//        SentryUserEntity entity = userDao.queryByName(userName);
//        if(entity==null|| StringUtil.isEmpty(entity.getPhone())){
//            messageService.sendWechatMsg(userName,"未配置接收号码", WechatAppEnums.assist);
//            return ;
//        }
//        SmsParam smsParam = SmsParam.builder()
//                .mobiles(entity.getPhone())
//                .content(content)
//                .type("1")
//                // 1表示国内，2表示国际
//                .regionType("1")
//                .signType("1")
//                //.batch()
//                .admin("test")
//                .from(userName)
//                .build();
//        //.extraParam()
//        //.uniqueId("")
//        //.isSToT("");
//        try{
//            PhpResult phpResult = smsRemote.sendSms(smsParam);
//            log.info("[短信下发结果]"+phpResult.toString());
//            messageService.sendWechatMsg(userName,"调用结果:"+phpResult.toString(), WechatAppEnums.assist);
//        }catch (Exception e){
//            log.error("[短信下发异常]:"+e.getMessage());
//            messageService.sendWechatMsg(userName,"调用异常:"+e.getMessage(), WechatAppEnums.assist);
//        }
//
//    }
//
//    /**
//     * 邮件业务测试
//     * @return
//     */
//    private void emailTest(String userName,String  content, String channel){
//        try{
//            String email = userName+"@zuzuche.com";
//            SentryUserEntity entity = userDao.queryByName(userName);
//            if(entity!=null && StringUtil.isNotEmpty(entity.getEmail())){
//                email = entity.getEmail();
//            }
//
//            MailSendParam param = MailSendParam.builder()
//                    .body(content)
//                    .title(content)
//                    .to_email(email)
//                    .build();
//            RespResult<MailSendDto> result = emailBusiService.sendEmail(param);
//            messageService.sendWechatMsg(userName,"调用结果:"+result.toString(), WechatAppEnums.assist);
//
//        }catch (Exception e){
//            log.error("[邮件下发异常]:"+e.getMessage());
//            messageService.sendWechatMsg(userName,"调用异常:"+e.getMessage(), WechatAppEnums.assist);
//        }
//
//
//    }






}
