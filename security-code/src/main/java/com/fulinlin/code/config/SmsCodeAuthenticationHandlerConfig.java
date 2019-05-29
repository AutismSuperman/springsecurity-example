package com.fulinlin.code.config;

import com.fulinlin.code.authentication.CustomAuthenticationFailureHandler;
import com.fulinlin.code.authentication.CustomAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * 这里是需要注入容器bean
 */
@Configuration
public class SmsCodeAuthenticationHandlerConfig {

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }
    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }
}
