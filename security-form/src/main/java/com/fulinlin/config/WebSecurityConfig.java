package com.fulinlin.config;

import com.fulinlin.authentication.FailureAuthenticationHandler;
import com.fulinlin.authentication.SuccessAuthenticationHandler;
import com.fulinlin.service.UserService;
import com.fulinlin.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final FailureAuthenticationHandler failureAuthenticationHandler;
    private final SuccessAuthenticationHandler successAuthenticationHandler;


    private final UserService userService;

    public WebSecurityConfig(UserService userService, FailureAuthenticationHandler failureAuthenticationHandler, SuccessAuthenticationHandler successAuthenticationHandler) {
        this.userService = userService;
        this.failureAuthenticationHandler = failureAuthenticationHandler;
        this.successAuthenticationHandler = successAuthenticationHandler;
    }

    /**
     * 注入身份管理器bean
     *
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 加密解密 这里是自定义MD5
     * security内置了多种加密
     * 可以直接new 接口出来
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(
                new PasswordEncoder() {

                    @Override
                    public String encode(CharSequence rawPassword) {
                        return MD5Util.encode((String) rawPassword);
                    }

                    @Override
                    public boolean matches(CharSequence rawPassword, String encodedPassword) {
                        return encodedPassword.equals(MD5Util.encode((String) rawPassword));
                    }
                });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.httpBasic()  //httpBasic 登录
        http.formLogin()
                .failureHandler(failureAuthenticationHandler) // 自定义登录失败处理
                .successHandler(successAuthenticationHandler) // 自定义登录成功处理
                .and()
                .logout()
                .logoutUrl("/logout")
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/authentication/form") // 自定义登录路径
                .and()
                .authorizeRequests()// 对请求授权
                .antMatchers("/login", "/authentication/require",
                        "/authentication/form").permitAll()// 这些页面不需要身份认证,其他请求需要认证
                .anyRequest() // 任何请求
                .authenticated()//; // 都需要身份认证
                .and()
                .csrf().disable();// 禁用跨站攻击
    }


}