package com.fulinlin.weixin.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lvhaibao
 * @description 自定义微信的服务提供商ID
 * @date 2019/1/4 0004 9:47
 */
@Data
@ConfigurationProperties(prefix = "system")
public class WeixinProperties {

    private String providerId = "weixin";

}

