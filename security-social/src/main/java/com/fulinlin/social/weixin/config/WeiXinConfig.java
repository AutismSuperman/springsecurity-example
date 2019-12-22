package com.fulinlin.social.weixin.config;

import com.fulinlin.social.support.SocialFactoryAdapter;
import com.fulinlin.properties.SocialProperties;
import com.fulinlin.social.qq.factory.QQConnectionFactory;
import com.fulinlin.social.weixin.factory.WeixinConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.UserIdSource;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;

/**
 * @program: spring-seruirty
 * @author: Fulin
 * @create: 2019-06-14 17:10
 **/
@Configuration
public class WeiXinConfig extends SocialFactoryAdapter {


    @Autowired
    private SocialProperties socialProperties;

    @Override
    protected ConnectionFactory<?> createConnectionFactory() {
        return new WeixinConnectionFactory(
                socialProperties.getWx().getProviderId(),
                socialProperties.getWx().getAppId(),
                socialProperties.getWx().getAppSecret());
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }
}
