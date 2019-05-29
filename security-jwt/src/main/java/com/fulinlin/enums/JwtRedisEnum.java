package com.fulinlin.enums;

/**
 * Redis存储jwt的key前缀
 *
 */
public enum JwtRedisEnum {

    /**
     * token 的key前缀
     */
    TOKEN_KEY_PREFIX("jwt:"),

    /***
     * authentication的key
     */
    AUTHENTICATION_KEY_PREFIX("authentication:")
    ;

    private String value;


    JwtRedisEnum(String value) {
        this.value = value;
    }

    /**
     * 获取key
     *
     * @param username：xxx@163.com
     * @param randomKey：xxxxxx
     * @return
     */
    public static String getTokenKey(String username, String randomKey) {
        return TOKEN_KEY_PREFIX.value + username + ":" + randomKey;
    }

    /**
     * 获取身份认证key
     *
     * @param username：用户名
     * @return
     */
    public static String getAuthenticationKey(String username, String randomKey) {
        return AUTHENTICATION_KEY_PREFIX.value + username + ":" + randomKey;
    }
}
