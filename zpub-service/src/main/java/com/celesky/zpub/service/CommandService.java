package com.celesky.zpub.service;

import com.google.common.base.Splitter;
import com.zuzuche.msa.redis.SpringContext;
import com.celesky.zpub.common.constant.WechatAppEnums;
import com.celesky.zpub.config.JenkinsConfig;
import com.celesky.zpub.service.command.AbstractWxCommand;
import com.celesky.zpub.wechatsdk.WechatInboundMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @desc:
 * @author: panqiong
 * @date: 2020-01-13
 */
@Service
@Slf4j
public class CommandService {
    private static String HELP_MSG= " 指令有问题?\n" +
            " 指令模板比如:\n" +
            " 发布 sms-service master (发布sms服务master分支)\n" +
            " history sms (查看发布的历史记录)\n" +
            " publish email imageName [fast]\n"+
            " silent 19 30 (将策略id为19的预警策略通知静默30分钟) \n" +
            " rmsilent 19 (取消策略id为19的静默模式\n"+
            " test sms 测试短信内容 (生产环境拨测短信)\n"+
            " test email 测试邮件内容 (生产环境拨测邮件)\n";


    @Autowired
    WechatMessageService messageService;

    @Autowired
    JenkinsConfig jenkinsConfig;

    /**
     * 指令模板
     * 发布 sms-service master
     * @param inboundMsg
     */
    public void dealCommand(WechatInboundMsg inboundMsg) {
        String userName = inboundMsg.getFromUserName();
        String content = inboundMsg.getContent();

        try{
            boolean interrupt = eggsInterpretor(userName,content);
            if(interrupt){
                return;
            }
            // 回复一条确认消息给用户
            // 发布人 发完回个消息给他
            List<String> list = Splitter.on(" ").splitToList(content.trim());
            if(list==null||list.size()==0){
                messageService.sendWechatMsg(inboundMsg.getFromUserName(),"呵呵,不太明白你想干什么!"+HELP_MSG, WechatAppEnums.assist);
                return;
            }

            String instruction = list.get(0);

            //特殊处理一下发布指令
            if("发布".equals(instruction)){
                instruction = "deploy";
            }

            AbstractWxCommand wxCommandExe = (AbstractWxCommand) SpringContext.getContext().getBean(instruction);

            if(wxCommandExe!=null){
                wxCommandExe.execute(userName,list);
            }else{
                messageService.sendWechatMsg(userName,"找不到指令处理逻辑,建议咨询一下", WechatAppEnums.assist);
            }

        }catch (NoSuchBeanDefinitionException e){
            log.error("指令解析异常:",e.getMessage(),e);
            messageService.sendWechatMsg(userName,"找不到指令处理逻辑,建议咨询一下", WechatAppEnums.assist);
        }catch (Exception e){
            log.error("指令解析异常:",e.getMessage(),e);
            // 回复一条确认消息给用户
            messageService.sendWechatMsg(userName,HELP_MSG, WechatAppEnums.assist);
            return;
        }
    }

    /**
     *
     * @param userName
     * @param content
     * @return 是否继续
     */
    private boolean eggsInterpretor(String userName,String content){
        if(content.contains("爱好")){
            messageService.sendWechatMsg(userName,"喜欢唱,跳,rap,篮球", WechatAppEnums.assist);
            return true;
        }else if(content.contains("坤")){
            messageService.sendWechatMsg(userName,"你打球像蔡徐坤", WechatAppEnums.assist);
            return true;
        }else if(content.contains("help")){
            messageService.sendWechatMsg(userName,HELP_MSG, WechatAppEnums.assist);
            return true;
        }else if(content.contains("jenkins")){
            messageService.sendWechatMsg(userName,jenkinsConfig.getJenkinsUrl(), WechatAppEnums.assist);
            return true;
        }
        return false;


    }
}
