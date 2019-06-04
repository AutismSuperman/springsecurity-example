package com.fulinlin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @program: SpringSecurity
 * @author: fulin
 * @create: 2019-06-04 14:13
 **/
@SpringBootApplication
public class SsoClient2Application {
    /**
     * 获取当前登录的Authentication信息，SpringSecurity会自动注入
     *
     * @param user
     * @return
     */
    @GetMapping("/user")
    public Authentication user(Authentication user) {
        return user;
    }

    public static void main(String[] args) {
        SpringApplication.run(SsoClient2Application.class, args);
    }
}
