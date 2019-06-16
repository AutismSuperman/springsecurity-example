package com.fulinlin.social.weixin.config;

import com.fulinlin.social.support.SocialFactoryAdapter;
import com.fulinlin.properties.SocialProperties;
import com.fulinlin.social.qq.factory.QQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;

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
        return new QQConnectionFactory(
                socialProperties.getWx().getProviderId(),
                socialProperties.getWx().getAppId(),
                socialProperties.getWx().getAppSecret());
    }
}
