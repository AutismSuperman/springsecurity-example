package com.fulinlin.social.qq.factory;

import com.fulinlin.social.qq.adapter.QQAdapter;
import com.fulinlin.social.qq.api.QQ;
import com.fulinlin.social.qq.provider.QQServiceProvider;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

/**
 * @program: spring-seruirty
 * @author: Fulin
 * @create: 2019-06-14 16:38
 **/
public class QQConnectionFactory extends OAuth2ConnectionFactory<QQ> {
    /**
     * providerId：使用来区分服务商的
     * 比如 QQ  WX
     *
     * @param providerId
     * @param appId
     * @param appSecret
     */
    public QQConnectionFactory(String providerId, String appId, String appSecret) {
        super(providerId, new QQServiceProvider(appId, appSecret), new QQAdapter());
    }
}
