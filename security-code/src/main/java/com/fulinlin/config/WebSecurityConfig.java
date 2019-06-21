package com.fulinlin.config;

import com.fulinlin.code.authentication.CustomAuthenticationFailureHandler;
import com.fulinlin.code.authentication.CustomAuthenticationSuccessHandler;
import com.fulinlin.code.config.SmsCodeAuthenticationSecurityConfig;
import com.fulinlin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
    @Autowired
    private UserService userService;


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
        //表单登陆配置
        http.formLogin()
                .failureHandler(customAuthenticationFailureHandler)
                .successHandler(customAuthenticationSuccessHandler)
                .loginPage("/login")
                .loginProcessingUrl("/authentication/form")
                .and();


        http.apply(smsCodeAuthenticationSecurityConfig)
                .and()
                .logout()
                .logoutUrl("/logout")
                .and()
                .authorizeRequests()
                // 如果有允许匿名的url，填在下面
                .antMatchers("/login", "/sms/**", "/authentication/form").permitAll()
                .anyRequest().authenticated();

        // 关闭CSRF跨域
        http.csrf().disable();
    }
}
