package com.fulinlin.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @program: SpringSecurity
 * @author: fulin
 * @create: 2019-06-16 21:00
 **/
@Configuration
@EnableConfigurationProperties(value = SocialProperties.class)
@ConfigurationProperties(prefix = "social")
public class SocialProperties {
    private QQProperties qq = new QQProperties();
    private WeixinProperties wx = new WeixinProperties();

    public QQProperties getQq() {
        return qq;
    }

    public void setQq(QQProperties qq) {
        this.qq = qq;
    }

    public WeixinProperties getWx() {
        return wx;
    }

    public void setWx(WeixinProperties wx) {
        this.wx = wx;
    }
}
