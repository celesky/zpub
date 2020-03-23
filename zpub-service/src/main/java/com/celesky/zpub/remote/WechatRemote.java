package com.celesky.zpub.remote;

import com.google.common.base.Joiner;
import com.zuzuche.msa.base.util.CollectionUtil;
import com.zuzuche.msa.base.util.JsonUtil;
import com.zuzuche.msa.base.util.StringUtil;
import com.celesky.zpub.remote.dto.TokenDto;
import com.celesky.zpub.remote.dto.WechatMessageResult;
import com.celesky.zpub.remote.param.WechatMessageParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @desc: 企业微信
 * @author: panqiong
 * @date: 2019-01-10
 */
@Slf4j
@Component
public abstract class WechatRemote extends AbstractHttpInvoke{
    /**
     * wechat.url: https://qyapi.weixin.qq.com/cgi-bin
     * wechat.corpId: ww1326b2a5049175e5
     * wechat.robotAppId: 1000031
     * wechat.robotAppSecret: xxxx
     */
    @Value("${wechat.url}")
    private String url;

    @Value("${wechat.corpId}")
    private String corpId;

    private String appKey;

    private String appSecret;


    WechatRemote(){

    }

    void setConfig(String appKey,String appSecret){
        this.appKey = appKey;
        this.appSecret = appSecret;
    }
    

//    @Autowired
//    WechatCache wechatCache;


    /**
     * 额外的header设置 比如编码
     *
     * @param header
     */
    @Override
    protected void setHeader(HttpHeaders header) {

    }

    /**
     * 注意事项：
     * 开发者需要缓存access_token，用于后续接口的调用（注意：不能频繁调用gettoken接口，否则会受到频率拦截）。当access_token失效或过期时，需要重新获取。
     *
     * access_token的有效期通过返回的expires_in来传达，正常情况下为7200秒（2小时），有效期内重复获取返回相同结果，过期后获取会返回新的access_token。
     * 由于企业微信每个应用的access_token是彼此独立的，所以进行缓存时需要区分应用来进行存储。
     * access_token至少保留512字节的存储空间。
     * 企业微信可能会出于运营需要，提前使access_token失效，开发者应实现access_token失效时重新获取的逻辑。
     */
    protected TokenDto getNewToken(){
        TokenDto dto = null;
        try{
            String requestUrl = url+"/gettoken?corpid="+corpId+"&corpsecret="+ appSecret;
            String str = super.getJson(requestUrl);
            dto = JsonUtil.stringToObj(str,TokenDto.class);
        }catch (Exception e){
            log.error("[getToken]获取企业微信token失败",e.getMessage(),e);
            dto = TokenDto.builder()
                    .errcode(-1)
                    .errmsg("调用token接口异常")
                    .build();
        }
        return dto;
    }


    /**
     * 根据名称发送
     * @param userNameList
     * @param text
     * @return
     */
    public WechatMessageResult sendMessageByName(List<String> userNameList, String text){
        if(CollectionUtil.isEmpty(userNameList)){
            log.error("userNameList为空");
            return null;
        }
        WechatMessageParam.TextBean textBean = WechatMessageParam.TextBean
                .builder()
                .content(text)
                .build();
        String usernames = Joiner.on("|").join(userNameList);

        WechatMessageParam param = WechatMessageParam.builder()
                .agentid(Integer.parseInt(appKey))
                .msgtype("text")
                .safe(0)
                .toparty("")
                .totag("")
                .touser(usernames)
                .text(textBean)
                .build();

        return this.sendMessage(param);
    }
    /**
     * 根据名称发送
     * @param departList
     * @param text
     * @return
     */
    public WechatMessageResult sendMessageByDepart(List<String> departList,String text){
        if(CollectionUtil.isEmpty(departList)){
            log.error("userNameList为空");
            return null;
        }
        WechatMessageParam.TextBean textBean = WechatMessageParam.TextBean
                .builder()
                .content(text)
                .build();
        String departs = Joiner.on("|").join(departList);

        WechatMessageParam param = WechatMessageParam.builder()
                .agentid(Integer.parseInt(appKey))
                .msgtype("text")
                .safe(0)
                .toparty(departs)
                .totag("")
                .touser("")
                .text(textBean)
                .build();

        return this.sendMessage(param);
    }


    protected abstract String getCachedToken();

    protected abstract void setCachedToken(String token);


    /**
     * 发送消息给企业微信
     * @param param
     * @return
     */
    private WechatMessageResult sendMessage(WechatMessageParam param){
        WechatMessageResult result = null;

        try{
            String token = getCachedToken();
            if(StringUtil.isEmpty(token)){
                token = refreshToken();
            }
            String requestUrl = url+"/message/send?access_token="+token;
            String str = super.postJson(requestUrl,param);
            result = JsonUtil.stringToObj(str,WechatMessageResult.class);
        }catch (Exception e){
            log.error("[getToken]推送企业微信消息失败",e.getMessage(),e);
            result = WechatMessageResult.builder()
                    .errcode(-1)
                    .errmsg("调用token接口异常")
                    .build();
        }
        return result;
    }

    public String refreshToken(){
        TokenDto dto = getNewToken();
        if(dto.getErrcode()==0){
            setCachedToken(dto.getAccess_token());
            return dto.getAccess_token();
        }
        return "";
    }
}
