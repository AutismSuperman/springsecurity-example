package com.fulinlin.config;

import com.fulinlin.authentication.FailureAuthenticationHandler;
import com.fulinlin.authentication.SuccessAuthenticationHandler;
import com.fulinlin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private FailureAuthenticationHandler failureAuthenticationHandler;
    @Autowired
    private SuccessAuthenticationHandler successAuthenticationHandler;
    @Autowired
    private UserService userService;


    /**
     * 注入身份管理器bean
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 加密解密 这里使用明文
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
                    public String encode(CharSequence charSequence) {
                        return charSequence.toString();
                    }

                    @Override
                    public boolean matches(CharSequence charSequence, String s) {
                        return s.equals(charSequence.toString());
                    }
                });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.httpBasic()  //httpBasic 登录
        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/authentication/form") // 自定义登录路径
                .failureHandler(failureAuthenticationHandler) // 自定义登录失败处理
                .successHandler(successAuthenticationHandler) // 自定义登录成功处理
                .and()
                .logout()
                .logoutUrl("/logout")
                .and()
                .authorizeRequests()// 对请求授权
                .antMatchers("/login", "/authentication/require",
                        "/authentication/form").permitAll()// 这些页面不需要身份认证,其他请求需要认证
                .anyRequest() // 任何请求
                .authenticated()//; // 都需要身份认证
                .and().rememberMe().
                rememberMeCookieName("reberm")
                .tokenValiditySeconds(3600)
                .and()
                .csrf().disable();// 禁用跨站攻击
    }


}