package com.fulinlin.config;

import com.fulinlin.code.config.SmsCodeAuthenticationSecurityConfig;
import com.fulinlin.service.UserService;
import com.fulinlin.util.MD5Util;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    private final UserService userService;

    public WebSecurityConfig(SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig, UserService userService) {
        this.smsCodeAuthenticationSecurityConfig = smsCodeAuthenticationSecurityConfig;
        this.userService = userService;
    }


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
        http.apply(smsCodeAuthenticationSecurityConfig).and().authorizeRequests()
                // 如果有允许匿名的url，填在下面
                .antMatchers("/sms/**", "/authentication/form").permitAll()
                .anyRequest().authenticated()
                .and()
                // 设置登陆页
                .formLogin().loginPage("/login").loginProcessingUrl("/authentication/form")
                // 设置登陆成功页
                .defaultSuccessUrl("/").permitAll()
                .and()
                .logout().permitAll();

        // 关闭CSRF跨域
        http.csrf().disable();
    }
}
