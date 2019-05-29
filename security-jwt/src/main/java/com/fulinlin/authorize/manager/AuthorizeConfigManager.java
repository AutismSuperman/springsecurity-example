package com.fulinlin.authorize.manager;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * 拼接封装请求
 */
public interface AuthorizeConfigManager {

    void config(HttpSecurity httpSecurity) throws Exception;
}
