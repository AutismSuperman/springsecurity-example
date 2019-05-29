package com.fulinlin.authorize.Provider;

import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;

/**
 * 这里是添加各种过滤权限的可以维护到yml里
 */
@Component
@Order(Byte.MIN_VALUE + 1)
public class CoreAuthorizeConfigProvider implements AuthorizeConfigProvider {

    @Override
    public void config(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests().antMatchers(
                // 读取配置文件，放开自定义路径的权限。
                getPermitUrls()
        ).permitAll();
    }

    public String[] getPermitUrls() {
        return new String[]{"/login", "/authentication"};
    }
}
