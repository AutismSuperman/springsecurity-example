package com.fulinlin.authorize.Provider;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

/**
 * 用来管理所有的Provider
 * Provider 的顺序根据 @Order 来判断先后顺序
 */
public interface AuthorizeConfigProvider {

    void config(HttpSecurity httpSecurity) throws Exception;


}
