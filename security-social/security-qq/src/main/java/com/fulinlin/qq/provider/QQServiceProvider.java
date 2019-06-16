package com.fulinlin.qq.provider;

import com.fulinlin.qq.api.QQ;
import com.fulinlin.qq.api.QQImpl;
import com.fulinlin.qq.template.QQOAuth2Template;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

/**
 * 微信的OAuth2流程处理器的提供器
 * 供spring social的connect体系调用
 *
 * @author fulin
 * @description
 */
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {

    private String appId;

    /**
     * 流程中的第一步：导向认证服务器的url
     */
    private static final String URL_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";

    /**
     * 申请令牌的url
     */
    private static final String URL_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";

    /**
     * 给QQOAuth2Template
     * 让QQOAuth2Template 处理 accessToke
     * 顺便吧appId 传过来赋值
     */
    public QQServiceProvider(String appId, String appSecret) {
        super(new QQOAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));
        this.appId = appId;
    }

    @Override
    public QQ getApi(String accessToken) {
        // QQImpl必须是多例的，不能把QQImpl声明为@Component组件（这是视频中的原话，不是可以指明@Scope吗？）
        // accessToken抽象类会直接传进来
        return new QQImpl(accessToken, appId);
    }

}
