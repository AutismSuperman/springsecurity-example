package com.fulinlin.config;

import com.fulinlin.authentication.AppAuthenticationFailureHandler;
import com.fulinlin.authentication.AppAuthenticationSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * 这里是需要注入容器bean
 */
@Configuration
public class AppAuthenticationHandlerConfig {


    @Bean(name = "authenticationSuccessHandler")
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AppAuthenticationSuccessHandler();
    }

    @Bean(name = "authenticationFailureHandler")
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new AppAuthenticationFailureHandler();
    }
}
