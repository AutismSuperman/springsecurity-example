package com.fulinlin.enums;

/**
 * URL 枚举
 */
public enum JwtUrlEnum {

    /**
     * 退出登录
     */
    LOGOUT("jwtLogout");

    private String url;


    JwtUrlEnum(String url) {
        this.url = url;
    }

    public String url() {
        return url;
    }
}
