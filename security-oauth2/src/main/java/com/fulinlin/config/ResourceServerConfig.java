package com.fulinlin.config;

import com.fulinlin.authentication.FailureAuthenticationHandler;
import com.fulinlin.authentication.SuccessAuthenticationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * @program: SpringSecurity
 * @author: fulin
 * @create: 2019-06-03 19:04
 **/
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final SuccessAuthenticationHandler successAuthenticationHandler;
    private final FailureAuthenticationHandler failureAuthenticationHandler;

    public ResourceServerConfig(SuccessAuthenticationHandler successAuthenticationHandler, FailureAuthenticationHandler failureAuthenticationHandler) {
        this.successAuthenticationHandler = successAuthenticationHandler;
        this.failureAuthenticationHandler = failureAuthenticationHandler;
    }



    @Override
    public void configure(HttpSecurity http) throws Exception {
        //表单登录 方式
        http.formLogin()
                .loginPage("/login")
                //登录需要经过的url请求
                .loginProcessingUrl("/authentication/form")
                .successHandler(successAuthenticationHandler)
                .failureHandler(failureAuthenticationHandler);


        http.authorizeRequests()
                .antMatchers("/oauth/token").permitAll()
                .anyRequest()
                .permitAll()
                .and()
                //关闭跨站请求防护
                .csrf().disable();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        super.configure(resources);
    }

    //    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.formLogin().permitAll();
////        http.logout().permitAll();
////        http
////                .authorizeRequests()
////                .antMatchers("/oauth/token").permitAll()
////                .anyRequest()
////                .permitAll()
////                .and()
////                //关闭跨站请求防护
////                .csrf().disable();
////    }
}
