package com.fulinlin.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * @program: spring-seruirty
 * @author: Fulin
 * @create: 2019-06-14 17:00
 **/
@Data
@Component
@EnableConfigurationProperties(value = QQProperties.class)
@ConfigurationProperties(prefix = "social.qq")
public class QQProperties {
    private String appid;
    private String appSecret;
    private String providerId = "qq";
}
