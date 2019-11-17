package com.fulinlin.properties;

import lombok.Data;

/**
 * Jwt的基本配置
 */
@Data
public class JwtProperties {
    /**
     * 默认前面秘钥
     */
    private String secret = "defaultSecret";

    /**
     * token默认有效期时长，1小时
     */
    private Long expiration = 3600L;
    /**
     * token默认有效期时长，1个半小时
     */
    private Long refreshExpiration = 5400L;

    /**
     * token的唯一标记
     */
    private String md5Key = "randomKey";

    /**
     * token还剩余多长时间就自动刷新下，默认是600s
     */
    private Long autoRefreshTokenExpiration = 600L;

    /**
     * GET请求是否需要进行Authentication请求头校验，true：默认校验；false：不拦截GET请求
     */
    private boolean preventsGetMethod = true;
}
