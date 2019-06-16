package com.fulinlin.social.qq.api;

import com.alibaba.fastjson.JSON;
import com.fulinlin.social.qq.model.QQUserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @program: spring-seruirty
 * @author: Fulin
 * @create: 2019-06-14 14:52
 **/
public class QQImpl extends AbstractOAuth2ApiBinding implements QQ {


    /**
     * 这些通过 QQ互联开放平台查看QQ文档可以看到
     */
    private static final String URL_GET_OPENID = "https://graph.qq.com/oauth2.0/me?access_token=%s";

    /**
     * 获取用户的基本信息URL，可以查看 QQ互联开放平台 看到，本来有三个参数，其中一个access_token会交给AbstractOAuth2ApiBinding处理，AbstractOAuth2ApiBinding会把access_token
     * 这个参数加进去，所以这里就没有
     */
    private static final String URL_GET_USERINFO = "https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";


    private String appId;

    private String openId;


    /**
     * @param accessToken
     */
    public QQImpl(String accessToken, String appId) {
        // 默认情况下，也就是AbstractOAuth2ApiBinding中只有一个参数的构造函数，使用的策略是TokenStrategy.AUTHORIZATION_HEADER
        // 即将access_token放到请求头中，而qq要求将access_token放到参数中，所以需要使用TokenStrategy.ACCESS_TOKEN_PARAMETER策略
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
        this.appId = appId;
        //补全获取openid 的url
        String url = String.format(URL_GET_OPENID, accessToken);
        // 发送get请求获取openId
        String result = getRestTemplate().getForObject(url, String.class);
        //截取openid
        this.openId = StringUtils.substringBetween(result, "\"openid\":\"", "\"}");

    }

    @Override
    public QQUserInfo getUserInfo() {
        //补全获取userinfo的url
        String url = String.format(URL_GET_USERINFO, appId, openId);
        // 获取用户信息
        String result = getRestTemplate().getForObject(url, String.class);
        QQUserInfo qqUserInfo = JSON.parseObject(result, QQUserInfo.class);
        if (qqUserInfo != null) {
            qqUserInfo.setOpenId(openId);
            return qqUserInfo;
        } else {
            throw new RuntimeException("获取用户信息失败");
        }
    }

    /**
     * 默认注册的StringHttpMessageConverter字符集为ISO-8859-1，而微信返回的是UTF-8的，所以覆盖了原来的方法。
     */
    @Override
    protected List<HttpMessageConverter<?>> getMessageConverters() {
        List<HttpMessageConverter<?>> messageConverters = super.getMessageConverters();
        messageConverters.remove(0);
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return messageConverters;
    }

}
