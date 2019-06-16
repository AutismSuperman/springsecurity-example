package com.fulinlin.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * @program: spring-seruirty
 * @author: Fulin
 * @create: 2019-06-14 17:00
 **/
@Configuration
@EnableConfigurationProperties(value = QQProperties.class)
@ConfigurationProperties(prefix = "social.qq")
public class QQProperties {
    private String appid;
    private String appSecret;
    private String providerId = "qq";

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }
}
