package com.fulinlin.properties;

/**
 * Jwt的基本配置
 *
 */
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
     * token的唯一标记，目前用于redis里存储，解决同账号，同时只能单用户登录的情况
     */
    private String md5Key = "randomKey";

    /**
     * token还剩余多长时间就自动刷新下，默认是600s
     */
    private Long autoRefreshTokenExpiration = 600L;

    /**
     * 判断是否开启允许多人同账号同时在线，若不允许的话则将上一个人T掉，默认false，不T掉，允许多人登录，true：T掉
     */
    private boolean preventsLogin = false;

    /**
     * GET请求是否需要进行Authentication请求头校验，true：默认校验；false：不拦截GET请求
     */
    private boolean preventsGetMethod = true;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    public String getMd5Key() {
        return md5Key;
    }

    public void setMd5Key(String md5Key) {
        this.md5Key = md5Key;
    }

    public Long getAutoRefreshTokenExpiration() {
        return autoRefreshTokenExpiration;
    }

    public void setAutoRefreshTokenExpiration(Long autoRefreshTokenExpiration) {
        this.autoRefreshTokenExpiration = autoRefreshTokenExpiration;
    }

    public boolean isPreventsLogin() {
        return preventsLogin;
    }

    public void setPreventsLogin(boolean preventsLogin) {
        this.preventsLogin = preventsLogin;
    }

    public boolean isPreventsGetMethod() {
        return preventsGetMethod;
    }

    public void setPreventsGetMethod(boolean preventsGetMethod) {
        this.preventsGetMethod = preventsGetMethod;
    }
}
