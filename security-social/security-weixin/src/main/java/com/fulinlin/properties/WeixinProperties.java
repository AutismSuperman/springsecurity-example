package com.fulinlin.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lvhaibao
 * @description 自定义微信的服务提供商ID
 * @date 2019/1/4 0004 9:47
 */

@Configuration
@EnableConfigurationProperties(WeixinProperties.class)
@ConfigurationProperties(prefix = "social.wx")
public class WeixinProperties {
    private String appId;
    private String appSecret;
    private String providerId = "weixin";

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
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

