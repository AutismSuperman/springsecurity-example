package com.fulinlin.config;

import com.fulinlin.authentication.FailureAuthenticationHandler;
import com.fulinlin.authentication.SuccessAuthenticationHandler;
import com.fulinlin.service.UserService;
import com.fulinlin.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final FailureAuthenticationHandler failureAuthenticationHandler;
    private final SuccessAuthenticationHandler successAuthenticationHandler;

    private final PersistentTokenRepository persistentTokenRepository;

    private final UserService userService;

    public WebSecurityConfig(UserService userService, FailureAuthenticationHandler failureAuthenticationHandler, SuccessAuthenticationHandler successAuthenticationHandler, PersistentTokenRepository persistentTokenRepository) {
        this.userService = userService;
        this.failureAuthenticationHandler = failureAuthenticationHandler;
        this.successAuthenticationHandler = successAuthenticationHandler;
        this.persistentTokenRepository = persistentTokenRepository;
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
        // 这里要说明一下  .anyRequest().authenticated() 是不能使用rememberMe 功能的
        //
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
                        "/authentication/form")
                .permitAll() .anyRequest().authenticated()
//                .and().rememberMe().rememberMeCookieName("reberm").tokenValiditySeconds(3600)  //使用 cookie的形势去做记住我
                .and().rememberMe().tokenValiditySeconds(3600).tokenRepository(persistentTokenRepository).userDetailsService(userService)
                .and()
                .csrf().disable();// 禁用跨站攻击
    }
}